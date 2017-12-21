package com.loggersoft.performance;

public class Main {

    public static void main(String[] args) {
        System.out.println("Start...");
        final long startTime = System.currentTimeMillis();
        new Performer(args.length > 0 ? args[0] : null).start();
        System.out.format("Finish: %dms%n", System.currentTimeMillis()- startTime);
    }
}
