package com.loggersoft.dcf.converter

import java.io.*

private const val EXT_MP = ".mp"
private const val EXT_DCF = ".dcf"
private const val DEFAULT_ENCODING = "UTF-8"
private const val BLOCK_POLYLINE = "POLYLINE"

private val regexpWayId = Regex("""^\s*;\s*WayID\s*=\s*(\d+)\s*$""")
private val regexpBlock = Regex("""^\s*\[\s*(\w+)\s*]\s*$""")
private val regexpData = Regex("""^\s*Data\d\s*=\s*(.+)$""")
private val regexpPosition = Regex("""\s*\(\s*(\d+\.\d+)\s*,\s*(\d+\.\d+)\s*\)\s*""")

internal class Converter(private val inputDirectory: String,
                         private val outputDirectory: String? = null) : FilenameFilter {

    fun start() {
        val files: Array<File>? = File(inputDirectory).listFiles(this)
        val output = File(outputDirectory ?: ".")
        if (!output.exists() && !output.mkdirs()) {
            println("Error: Failed to create output directory!")
            return
        }
        if (files != null) {
            for (file in files) {
                processFile(file, File(output, file.nameWithoutExtension + EXT_DCF))
            }
        } else {
            println("Source files not found.")
        }
    }

    override fun accept(dir: File?, name: String?): Boolean {
        return name != null && name.toLowerCase().endsWith(EXT_MP)
    }

    private fun processFile(input: File, output: File) {
        println("Converting '${input.name}' to '$output'...")
        BufferedReader(InputStreamReader(FileInputStream(input), DEFAULT_ENCODING)).use { reader ->
            PrintWriter(OutputStreamWriter(FileOutputStream(output), DEFAULT_ENCODING)).use { writer ->

                fun processBlock(line: String, clustId: String?) {
                    val dataMatch = regexpData.matchEntire(line)
                    if (dataMatch != null) {
                        val positions = regexpPosition.findAll(dataMatch.groupValues[1])
                        var prevPos: MatchResult? = null
                        for (position in positions) {
                            if (prevPos != null) {
                                writer.println("SEG:${prevPos.groupValues[2]}:${prevPos.groupValues[1]}:${position.groupValues[2]}:${position.groupValues[1]}:ClustID:$clustId")
                            }
                            prevPos = position
                        }
                    }
                }

                var currentWayId: String? = null
                var isBlock = false
                for(line in reader.lineSequence()) {
                    val blockMatch = regexpBlock.matchEntire(line)
                    if (blockMatch != null) {
                        if (blockMatch.groupValues[1].toUpperCase() == BLOCK_POLYLINE) {
                            isBlock = currentWayId != null
                        } else {
                            currentWayId = null
                            isBlock = false
                        }

                    } else if (isBlock) {
                        processBlock(line, currentWayId)

                    } else {
                        val wayIdMatch = regexpWayId.matchEntire(line)
                        if (wayIdMatch != null) {
                            currentWayId = wayIdMatch.groupValues[1]
                        }
                    }
                }
            }
        }
        if (output.length() <= 0) {
            output.delete()
        }
    }
}