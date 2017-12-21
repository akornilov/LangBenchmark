package com.loggersoft.scala.performance

import java.io.File
import io.Source

class DcfFile(private val file: File) {
  def processFile(segmentRetrieved: (DcfSegment) => Boolean, invalidLine: (String) => Unit): Unit = {
    for (line <- Source.fromFile(file, "UTF-8").getLines()) {
      val token = line.trim
      if (!token.isEmpty && token.startsWith("SEG:")) {
        val segment = processLine(line)
        if (segment != null) {
          if (!segmentRetrieved(segment)) return
        } else {
          invalidLine(line)
        }
      }
    }
  }

  private def processLine(line: String): DcfSegment = {
    val parts = line.split(":")
    if (parts.length < 6) return null
    val index = parts.indexWhere((s: String) => s.startsWith("ClustID"), 5)
    if (index < 0 || index + 1 > parts.length) return null
    try {
      return new DcfSegment(new Position(parts(2).toDouble, parts(1).toDouble),
                            new Position(parts(4).toDouble, parts(3).toDouble),
                            parts(index + 1))
    } catch  {
      case e: NumberFormatException => return null
    }
  }
}
