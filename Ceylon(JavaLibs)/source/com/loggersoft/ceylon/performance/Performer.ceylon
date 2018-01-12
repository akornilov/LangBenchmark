import java.util { HashMap, ArrayList, List, Map }
import java.io { File, IOException }

shared class Performer(String? directory) {
    File dir = File(if (exists directory) then directory else ".");

    Map<String, List<DcfSegment>> segments = HashMap<String, List<DcfSegment>>();
    variable Position? pos = null;
    Float radius = 5000.0;

    Boolean accept(File dir, String name) => name.lowercased.endsWith(".dcf");

    shared void start() {
        if (exists list = dir.listFiles(accept)) {
            for (f in list) {
                print("Processing ``f.name``...");
                try {
                    DcfFile(f).processFile(segmentRetreived, invalidLine);
                } catch (IOException e) {
                    print(e.message);
                }
            }
        }
        print("Found ``segments.size()`` segments;");
    }

    Boolean segmentRetreived(DcfSegment segment) {
        value position = pos else (pos = segment.start);
        if (segment.start.distance(position) <= radius || segment.end.distance(position) <= radius) {
            if (exists list = segments[segment.clustId]) {
                list.add(segment);
            } else {
                value list = ArrayList<DcfSegment>();
                list.add(segment);
                segments[segment.clustId] = list;
            }
        }
        return true;
    }

    void invalidLine(String line)
            => print("Invalid DCF line: ``line``");
}
