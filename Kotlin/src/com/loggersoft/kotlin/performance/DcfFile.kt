package com.loggersoft.kotlin.performance

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

class DcfFile(private val file: File) {

    fun processFile(segmentRetrieved: (DcfSegment) -> Boolean, invalidLine: (String) -> Unit) {
        BufferedReader(InputStreamReader(FileInputStream(file), "UTF-8")).use {
            while (true) {
                val line = it.readLine() ?: break
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
    }
}