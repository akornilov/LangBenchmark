package com.loggersoft.kotlin.performance

import java.io.File
import java.io.FilenameFilter
import java.io.IOException

class Performer(directory: String?) : FilenameFilter {
    private val dir: File = File(directory ?: ".")
    private val segments: MutableMap<String, MutableList<DcfSegment>> = HashMap()
    private val radius = 5000.0
    private var pos: Position? = null

    override fun accept(dir: File, filename: String): Boolean
            = filename.toLowerCase().endsWith(".dcf")

    fun start() {
        val files = dir.listFiles(this)
        if (files != null) {
            for (f in files) {
                println("Processing ${f.name}...")
                try {
                    DcfFile(f).processFile(::segmentRetrieved, ::invalidLine)
                } catch (e: IOException) {
                    println(e.localizedMessage)
                }
            }
        }
        println("Found ${segments.size} segments;")
    }

    private fun segmentRetrieved(segment: DcfSegment): Boolean {
        pos = pos ?: segment.start
        val position = pos ?: return true
        if (segment.start.getDistance(position) <= radius || segment.end.getDistance(position) <= radius) {
            segments.getOrPut(segment.clustId, { ArrayList() }).add(segment)
        }
        return true
    }

    private fun invalidLine(line: String)
            = println("Invalid DCF line: $line")
}
