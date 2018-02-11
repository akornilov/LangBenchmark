package com.loggersoft.performance;

public class Position {
    private double lat;
    private double lon;

    public Position(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public boolean isValid() {
        return (lat >= -90.0 && lat <= 90.0 && lon >= -180.0 && lon <= 180.0);
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getDistance(Position other) {
        final double lat1 = Math.toRadians(lat);
        final double lon1 = Math.toRadians(lon);
        final double lat2 = Math.toRadians(other.getLat());
        final double lon2 = Math.toRadians(other.getLon());
        final double sinLat = Math.sin((lat2 - lat1) / 2.0);
        final double sinLon = Math.sin((lon2 - lon1) / 2.0);
        final double a = sinLat * sinLat + Math.cos(lat1) * Math.cos(lat2) * sinLon * sinLon;
        return 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
    }
}
