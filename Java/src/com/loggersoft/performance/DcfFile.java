package com.loggersoft.performance;

import java.io.*;

public class DcfFile {
    private File file;
    private static final String CLUST_ID = "ClustID";

    public interface IEventHandler {

        boolean segmentRetrieved(DcfSegment segment);

        void invalidLine(String line);
    }

    public DcfFile(File file) {
        this.file = file;
    }

    public void processFile(IEventHandler handler) throws IOException {
        try(BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String line;
            while ((line = in.readLine()) != null) {
                final String token = line.trim();
                if (!token.startsWith("SEG:")) {
                    continue;
                }
                final String parts[] = line.split(":");
                if (parts.length < 6) {
                    handler.invalidLine(line);
                    continue;
                }
                int clustIdIndex = -1;
                for (int index = 5; index < parts.length; index++) {
                    if (parts[index].startsWith(CLUST_ID)) {
                        clustIdIndex = index;
                        break;
                    }
                }
                if (clustIdIndex < 0 || clustIdIndex + 1 >= parts.length) {
                    handler.invalidLine(line);
                    continue;
                }
                try {
                    if (!handler.segmentRetrieved(new DcfSegment(new Position(Double.parseDouble(parts[2]), Double.parseDouble(parts[1])),
                                                                 new Position(Double.parseDouble(parts[4]), Double.parseDouble(parts[3])),
                                                                 parts[clustIdIndex + 1]))) {
                        break;
                    }
                } catch (NumberFormatException e) {
                    handler.invalidLine(line);
                    continue;
                }
            }
        }
    }
}
