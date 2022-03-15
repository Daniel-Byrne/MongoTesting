package com.daniel.mongo.recorder;

public class Recorder {

    public static String operation;
    public static int cycleCount = 0;
    public static float secondsElapsed = 0;

    public static void record(RecordableAction action) {

        operation = action.name();

        long startTime = System.currentTimeMillis();

        action.run();

        long finishTime = System.currentTimeMillis();

        cycleCount++;
        secondsElapsed += sec(startTime, finishTime);
    }

    private static float sec(long s, long f) {
        return (f - s) / 1000f;
    }

    public static void print() {
        final float averageSeconds = secondsElapsed / cycleCount;
        System.out.println("Cycle count:  " + cycleCount + ". Total seconds: " + secondsElapsed);
        System.out.println(operation + " average time taken: " + averageSeconds + " seconds.");
    }

}
