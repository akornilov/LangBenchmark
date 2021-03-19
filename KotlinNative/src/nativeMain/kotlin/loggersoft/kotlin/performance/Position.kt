package loggersoft.kotlin.performance

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.atan2
import kotlin.math.sqrt
import kotlin.math.PI

class Position(var lat: Double, var lon: Double) {
    fun isValid()
            = lat >= -90.0 && lat <= 90 && lon >= -180 && lon <= 180

    fun getDistance(other: Position): Double {
        val lat1 = toRadians(lat)
        val lon1 = toRadians(lon)
        val lat2 = toRadians(other.lat)
        val lon2 = toRadians(other.lon)
        val sinLat = sin((lat2 - lat1) / 2.0)
        val sinLon = sin((lon2 - lon1) / 2.0)
        val a = sinLat * sinLat + cos(lat1) * cos(lat2) * sinLon * sinLon
        return 2.0 * atan2(sqrt(a), sqrt(1.0 - a))
    }

    private inline fun toRadians(angle: Double) = PI / 180.0 * angle
}