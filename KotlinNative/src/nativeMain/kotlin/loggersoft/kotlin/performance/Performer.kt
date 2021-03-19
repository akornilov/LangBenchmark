package loggersoft.kotlin.performance

import kotlinx.cinterop.*
import platform.posix.*

private const val DcfSuffix = ".dcf"

class Performer(directory: String?) {
    private val dir: String = directory ?: "."
    private val segments: MutableMap<String, MutableList<DcfSegment>> = HashMap()
    private val radius = 5000.0
    private var pos: Position? = null

    fun start() {
        val files = opendir(dir)
        if (files != null) {
            var result = readdir(files)
            while (result != null) {
                val filename = result.pointed.d_name.toKString()
                if (filename.toLowerCase().endsWith(DcfSuffix)) {
                    println("Processing ${filename}...")
                    DcfFile("$dir/$filename").processFile(::segmentRetrieved, ::invalidLine)
                }
                result = readdir(files)
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
