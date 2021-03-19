@safe

import std.stdio;
import std.file;

import file;
import segment;
import position;

public class Performer : IEventHandler {
    private immutable double radius = 5000.0;
    private Position pos;
    private DcfSegment[][string] segments;

    public void start(string path) {
        foreach (d; dirEntries(path != null ? path : "", "*.{dcf}", SpanMode.shallow)) {
            writeln("Processing '", d.name, "'...");
            try {
                new DcfFile(d.name).processFile(this);
            } catch(StdioException e) {
                writeln(e.msg);
            }
        }
        writefln("Found %d segments;", segments.length);
    }

    public bool segmentRetrieved(DcfSegment segment) {
        if (!pos) {
            pos = segment.getStart();
        }
        if (segment.getStart().getDistance(pos) <= radius || segment.getEnd().getDistance(pos) <= radius) {
            segments[segment.getClustId] ~= segment;
        }
        return true;
    }

    public void invalidLine(string line) {
        writeln("Invalid DCF line: ", line);
    }
}

