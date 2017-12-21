
class Position {
    Float lat
    Float lon

    new make(Float lat, Float lon) {
        this.lat = lat
        this.lon = lon
    }

    Bool isValid() {
        lat >= -90.0f && lat <= 90.0f && lon >= -180.0f && lon <= 180.0f
    }

    Float getDistance(Position other) {
        lat1 := lat.toRadians()
        lon1 := lon.toRadians()
        lat2 := other.lat.toRadians()
        lon2 := other.lon.toRadians()
        sinLat := ((lat2 - lat1) / 2.0f).sin()
        sinLon := ((lon2 - lon1) / 2.0f).sin()
        a := sinLat * sinLat + lat1.cos() * lat2.cos() * sinLon * sinLon
        return 2.0f * Float.atan2(a.sqrt(), (1.0f - a).sqrt())
    }

}
