package com.loggersoft.performance;

public class DcfSegment {
    private final Position start;
    private final Position end;
    private final String clustId;

    public DcfSegment(Position start, Position end, String clustId) {
        this.start = start;
        this.end = end;
        this.clustId = clustId;
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }

    public String getClustId() {
        return clustId;
    }

    public double getLength() {
        return start.getDistance(end);
    }
}
