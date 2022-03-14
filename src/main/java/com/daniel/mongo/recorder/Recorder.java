package com.daniel.mongo.recorder;

public class Recorder {

    public static void record(RecordableAction action) {

        long startTime = System.currentTimeMillis();

        action.run();

        long finishTime = System.currentTimeMillis();

        System.out.println(action.name() + " Execution time: " + sec(startTime, finishTime));
    }

    private static String sec(long s, long f) {
        return (f - s) / 1000L + " Seconds";
    }
    
}
