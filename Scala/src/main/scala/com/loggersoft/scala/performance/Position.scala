package com.loggersoft.scala.performance

import math.toRadians
import math.sin
import math.cos
import math.sqrt
import math.atan2

class Position(val lat: Double, val lon: Double) {

  def isValid()
    = lat >= -90.0 && lat <= 90 && lon >= -180 && lon <= 180

  def getDistance(other: Position): Double = {
    val lat1 = toRadians(lat)
    val lon1 = toRadians(lon)
    val lat2 = toRadians(other.lat)
    val lon2 = toRadians(other.lon)
    val sinLat = sin((lat2 - lat1) / 2.0)
    val sinLon = sin((lon2 - lon1) / 2.0)
    val a = sinLat * sinLat + cos(lat1) * cos(lat2) * sinLon * sinLon
    return 2.0 * atan2(sqrt(a), sqrt(1.0 - a))
  }
}
