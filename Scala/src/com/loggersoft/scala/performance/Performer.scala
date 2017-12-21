package com.loggersoft.scala.performance

import java.io.File
import java.io.FilenameFilter

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap

class Performer(val directory: String) extends FilenameFilter {
  private val dir: File = new File(if (directory != null) directory else ".")
  private val segments = new HashMap[String, ArrayBuffer[DcfSegment]]()
  private var pos: Position = null
  private val radius: Double = 5000.0

  def start() = {
    val files = dir.listFiles(this)
    if (files != null) {
      for (f <- files) {
        println(s"Processing '${f.getName()}'...")
        new DcfFile(f).processFile(segmentRetrieved _, invalidLine _)
      }
    }
    println(s"Found ${segments.size} segments;")
  }

  private def segmentRetrieved(segment: DcfSegment): Boolean = {
    if (pos == null) {
      pos = segment.start
    }
    if (segment.start.getDistance(pos) <= radius || segment.end.getDistance(pos) <= radius) {
      var list = segments.getOrElse(segment.clustId, null)
      if (list == null) {
        list = new ArrayBuffer[DcfSegment]
        segments.put(segment.clustId, list)
      }
      list += segment
    }
    return true
  }

  private def invalidLine(line: String)
    = println(s"Invalid DCF line: $line")

  override def accept(file: File, s: String): Boolean = s.endsWith(".dcf")
}
