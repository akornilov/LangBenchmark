package com.loggersoft.kotlin.performance

class DcfSegment(val start: Position, val end: Position, val clustId: String) {
    val length: Double = start.getDistance(end)
}