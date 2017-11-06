package edu.uwm.cs.fitrpg;


import java.util.ArrayList;

public class FitnessEntry {
    private String fitnessType;
    private String startTime, endTime;
    private int reps, sets;
    private float distance;
    private long duration;

    public FitnessEntry(String type) {
        this.fitnessType = type;
        startTime = "";
        endTime = "";
        reps = 0;
        sets = 0;
        distance = 0;
        duration = 0;
    }

    public String getFitnessType() {
        return fitnessType;
    }

    public void setStartTime(String start) {
        this.startTime = start;
    }

    public void setEndTime(String end) {
        this.endTime = end;
    }

    public void setReps(int r) {
        this.reps = r;
    }

    public void setSets(int s) {
        this.sets = s;
    }

    public void setDistance(float d) {
        this.distance = d;
    }

    public void setDuration(Long d) {
        this.duration = d;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getReps() {
        return reps;
    }

    public int getSets() {
        return sets;
    }

    public float getDistance() {
        return distance;
    }

    public Long getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "" + fitnessType + " " + startTime + " " + endTime;
    }
}
