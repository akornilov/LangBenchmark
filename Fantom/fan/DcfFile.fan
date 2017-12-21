class DcfFile {

    new make(File file) {
        this.file = file
    }

    Void processFile(|DcfSegment segment->Bool| segmentRetreived, |Str line| invalidLine) {
        file.eachLine {
            if (it.trim().startsWith("SEG:")) {
                segment := processLine(it)
                if (segment == null) {
                    invalidLine(it)
                } else {
                    segmentRetreived(segment)
                }
            }
        }
    }

    private DcfSegment? processLine(Str line) {
        trim := line.trim()
        parts := trim.split(':')
        if (parts.size < 6) {
            return null;
        }
        lat1 := parts[2].toFloat()
        if (lat1 == null) {
            return null
        }
        lon1 := parts[1].toFloat()
        if (lon1 == null) {
            return null
        }
        lat2 := parts[4].toFloat()
        if (lat2 == null) {
            return null
        }
        lon2 := parts[3].toFloat()
        if (lon2 == null) {
            return null
        }
        index := -1
        for (Int i := 5; i < parts.size; i++) {
            if (parts[i].startsWith("ClustID")) {
                index = i;
                break
            }
        }
        if (index < 0 || index + 1 >parts.size) {
            return null
        }
        return DcfSegment.make(Position.make(lat1, lon1), Position.make(lat1, lon1), parts[index + 1])
    }

    private File file
}
