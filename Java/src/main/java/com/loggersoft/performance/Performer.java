package com.loggersoft.performance;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Performer implements DcfFile.IEventHandler, FilenameFilter {
    private File directory;

    private final Map<String, List<DcfSegment> > segments = new HashMap<String, List<DcfSegment> >();
    private Position pos;
    private double radius = 5000.0;

    public Performer(String directory) {
        this.directory = new File(directory != null ? directory : ".");
    }

    public void start() {
        final File[] files = directory.listFiles(this);
        if (files != null) {
            for (File file : files) {
                System.out.format("Processing '%s'...%n", file.getName());
                try {
                    new DcfFile(file).processFile(this);
                } catch (IOException e) {
                    System.err.println(e.getLocalizedMessage());
                }
            }
        }
        System.out.format("Found %d segments;%n", segments.size());
    }

    @Override
    public boolean segmentRetrieved(DcfSegment segment) {
        if (pos == null) {
            pos = segment.getStart();
        }
        if (segment.getStart().getDistance(pos) <= radius || segment.getEnd().getDistance(pos) <= radius) {
            List<DcfSegment> list = segments.get(segment.getClustId());
            if (list == null) {
                list = new ArrayList<DcfSegment>();
                segments.put(segment.getClustId(), list);
            }
            list.add(segment);
        }
        return true;
    }

    @Override
    public void invalidLine(String line) {
        System.err.println("Invalid DCF line: " + line);
    }

    @Override
    public boolean accept(File file, String s) {
        return s.toLowerCase().endsWith(".dcf");
    }
}
