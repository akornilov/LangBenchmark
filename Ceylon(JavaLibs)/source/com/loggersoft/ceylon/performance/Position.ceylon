import ceylon.math.float { toRadians, sin, cos, sqrt, atan2 }

class Position(shared Float lat, shared Float lon) {

    shared Boolean valid => -90.0 <= lat <= 90.0 && -180.0 <= lon <= 180.0;

    shared Float distance(Position other)
            => let(lat1 = toRadians(lat),
                   lon1 = toRadians(lon),
                   lat2 = toRadians(other.lat),
                   lon2 = toRadians(other.lon),
                   sinLat = sin((lat2 - lat1) / 2.0),
                   sinLon = sin((lon2 - lon1) / 2.0),
                   a = sinLat * sinLat + cos(lat1) * cos(lat2) * sinLon * sinLon)
                   2.0 * atan2(sqrt(a), sqrt(1.0 - a));
}