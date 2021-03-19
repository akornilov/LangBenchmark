package loggersoft.kotlin.performance

import kotlinx.cinterop.*
import platform.posix.*

class DcfFile(private val fileName: String) {

    fun processFile(segmentRetrieved: (DcfSegment) -> Boolean, invalidLine: (String) -> Unit) {
        val file = fopen(fileName, "r")
        if (file == null) {
            perror("cannot open input file $fileName")
            return
        }
        try {
            memScoped {
                val bufferLength = 64 * 1024
                val buffer = allocArray<ByteVar>(bufferLength)
                while(true) {
                    val line = fgets(buffer, bufferLength, file)?.toKString()
                    if (line == null) break
                    val token = line.trim()
                    if (!token.startsWith("SEG:")) continue
                    val parts = token.split(':')
                    if (parts.size < 6) {
                        invalidLine(line)
                        continue
                    }
                    val clustIdIndex = (5 until parts.size).firstOrNull { parts[it].startsWith("ClustID") }
                            ?: -1
                    if (clustIdIndex < 0 || clustIdIndex + 1 >= parts.size) {
                        invalidLine(line)
                        continue
                    }
                    try {
                        if (!segmentRetrieved(DcfSegment(Position(parts[2].toDouble(), parts[1].toDouble()),
                                        Position(parts[4].toDouble(), parts[3].toDouble()),
                                        parts[clustIdIndex + 1]))) {
                            break
                        }
                    } catch (e: NumberFormatException) {
                        invalidLine(line)
                    }
                }
            }
        } finally {
            fclose(file)
        }
    }
}
