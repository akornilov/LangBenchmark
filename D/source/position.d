@safe

import std.math;

public class Position {
    private double lat;
    private double lon;

    public this(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public bool isValid() const {
        return (lat >= -90.0 && lat <= 90.0 && lon >= -180.0 && lon <= 180.0);
    }

    public double getLat() const {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() const {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getDistance(in Position other) const {
        immutable auto lat1 = toRadians(lat);
        immutable auto lon1 = toRadians(lon);
        immutable auto lat2 = toRadians(other.getLat());
        immutable auto lon2 = toRadians(other.getLon());
        immutable auto sinLat = sin((lat2 - lat1) / 2.0);
        immutable auto sinLon = sin((lon2 - lon1) / 2.0);
        immutable auto a = sinLat * sinLat + cos(lat1) * cos(lat2) * sinLon * sinLon;
        return 2.0 * atan2(sqrt(a), sqrt(1.0 - a));
    }

    private static double toRadians(double angle) {
        return PI / 180.0 * angle;
    }
}

