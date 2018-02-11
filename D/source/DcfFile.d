@safe

import std.file;
import std.stdio;
import std.conv;
import std.algorithm.mutation;
import std.algorithm.searching;
import std.array;

import DcfSegment;
import Position;

public interface IEventHandler {
    bool segmentRetrieved(DcfSegment segment);

    void invalidLine(string line);
}

public class DcfFile {
    private const string CLUST_ID = "ClustID";

    private const string filename;

    public this(in string filename) {
        this.filename = filename;
    }

    public void processFile(IEventHandler handler) {
        foreach (line; File(filename).byLine()) {
            scope const auto token = to!string(line).strip(' ');
            if (!token.startsWith("SEG:")) {
                continue;
            }
            scope auto parts = token.split(':');
            if (parts.length < 6) {
                handler.invalidLine(token);
                continue;
            }
            auto clustIdIndex = -1;
            for (auto index = 5; index < parts.length; index++) {
                if (parts[index].startsWith(CLUST_ID)) {
                    clustIdIndex = index;
                    break;
                }
            }
            if (clustIdIndex < 0 || clustIdIndex + 1 >= parts.length) {
                handler.invalidLine(token);
                continue;
            }
            try {
                if (!handler.segmentRetrieved(new DcfSegment(new Position(parse!double(parts[2]), parse!double(parts[1])),
                                                             new Position(parse!double(parts[4]), parse!double(parts[3])),
                                                             parts[clustIdIndex + 1]))) {
                    break;
                }
            } catch (ConvException e) {
                handler.invalidLine(token);
                continue;
            }
        }
    }
}

