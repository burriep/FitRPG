package edu.uwm.cs.fitrpg.model;

public enum PhysicalActivityTrackingMode {
    TIME(0),
    DISTANCE(1),
    REPS(2),
    TIME_DISTANCE(3),
    TIME_REPS(4),
    DISTANCE_REPS(5),
    TIME_DISTANCE_REPS(6);

    private final int value;

    private PhysicalActivityTrackingMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
