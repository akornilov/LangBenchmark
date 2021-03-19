@safe

import position;

public class DcfSegment {
    private Position start;
    private Position end;
    private const string clustId;
    private const double length;

    public this(Position start, Position end, string clustId) {
        this.start = start;
        this.end = end;
        this.clustId = clustId;
        length = start.getDistance(end);
    }

    public Position getStart(){
        return start;
    }

    public Position getEnd() {
        return end;
    }

    public string getClustId() const {
        return clustId;
    }

    public double getLength() const {
        return length;
    }
}
