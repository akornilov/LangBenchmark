
internal class Performer {
    new make(Str? dir) {
        this.directory = (dir != null ? dir + "/" : "./").toUri().toFile()
    }

    Void start() {
        segmentRetreived := |DcfSegment segment->Bool| {
            if (pos == null) {
                pos = segment.start
            }
            if (segment.start.getDistance(pos) <= radius || segment.end.getDistance(pos) <= radius) {
                list := segments[segment.clustId]
                if (list == null) {
                    list = List.make(DcfSegment#, 0)
                    segments[segment.clustId] = list
                }
                list.add(segment)
            }
            return true
        }

        invalidLine := |Str line| {
            echo("Invalid DCF line: $line")
        }

        directory.list.each { 
            if (it.ext == "dcf") {
                echo("Processing '$it.name'...")
                DcfFile.make(it).processFile(segmentRetreived, invalidLine)
            }
        }

        echo("Found ${segments.size()} segments;")
    }

    private const Float radius := 5000.0f
    private File directory
    private Position? pos := null
    private [Str:DcfSegment[]] segments := Map.make(Str:DcfSegment[]#)
}
