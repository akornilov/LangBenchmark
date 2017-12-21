import ceylon.file { Path, Directory, parsePath, current }
import ceylon.collection { HashMap, MutableList, ArrayList, MutableMap }

shared class Performer(String? directory) {
    Path dir = if (exists directory) then parsePath(directory) else current;

    MutableMap<String, MutableList<DcfSegment>> segments = HashMap<String, MutableList<DcfSegment>>();
    variable Position? pos = null;
    Float radius = 5000.0;

    shared void start() {
        if (is Directory res = dir.resource) {
            for (f in res.files("*.dcf")) {
                print("Processing ``f.name``...");
                DcfFile(f).processFile(segmentRetreived, invalidLine);
            }
        }
        print("Found ``segments.size`` segments;");
    }

    Boolean segmentRetreived(DcfSegment segment) {
        value position = pos else (pos = segment.start);
        if (segment.start.distance(position) <= radius || segment.end.distance(position) <= radius) {
            value list = segments[segment.clustId];
            if (exists list) {
                list.add(segment);
            } else {
                segments[segment.clustId] = ArrayList<DcfSegment> { segment };
            }
        }
        return true;
    }

    void invalidLine(String line)
            => print("Invalid DCF line: ``line``");
}