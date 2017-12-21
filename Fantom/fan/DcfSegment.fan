class DcfSegment {
    Position start
    Position end
    Str clustId
    Float length

    new make(Position start, Position end, Str clustId) {
        this.start = start
        this.end = end
        this.clustId = clustId
        length = start.getDistance(end)
    }
}
