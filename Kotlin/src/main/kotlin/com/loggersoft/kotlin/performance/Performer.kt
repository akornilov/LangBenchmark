package com.loggersoft.kotlin.performance

import java.io.File
import java.io.IOException

class Performer(directory: String?){
    private val dir: File = File(directory ?: ".")
    private val segments: MutableMap<String, MutableList<DcfSegment>> = mutableMapOf()
    private val radius = 5000.0
    private var pos: Position? = null

    fun start() {
        dir.listFiles { _, filename -> filename.toLowerCase().endsWith(".dcf") }?.forEach {
            println("Processing ${it.name}...")
            try {
                DcfFile(it).processFile(::segmentRetrieved, ::invalidLine)
            } catch (e: IOException) {
                println(e.localizedMessage)
            }
        }
        println("Found ${segments.size} segments;")
    }

    private fun segmentRetrieved(segment: DcfSegment): Boolean {
        pos = pos ?: segment.start
        val position = pos ?: return true
        if (segment.start.getDistance(position) <= radius || segment.end.getDistance(position) <= radius) {
            segments.getOrPut(segment.clustId, { mutableListOf() }).add(segment)
        }
        return true
    }

    private fun invalidLine(line: String)
            = println("Invalid DCF line: $line")
}
