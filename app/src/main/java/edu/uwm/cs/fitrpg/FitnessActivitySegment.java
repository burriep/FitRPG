package edu.uwm.cs.fitrpg;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class FitnessActivitySegment {
    private static List<Location> locations = new ArrayList<>();
    private long startTime;
    private long stopTime;
    private double distance;
    private double topSpeed;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long time) {
        if (stopTime > 0 && time > stopTime) {
            throw new IllegalArgumentException();
        }
        startTime = time;
    }

    public boolean isStarted() {
        return startTime > 0;
    }

    public long getStopTime() {
        return stopTime;
    }

    public void setStopTime(long time) {
        if (!isStarted() || time < startTime) {
            throw new IllegalArgumentException();
        }
        stopTime = time;
    }

    public boolean isStopped() {
        return stopTime > 0;
    }

    public double getDistance() {
        return distance;
    }

    public long getDuration() {
        return stopTime - startTime;
    }

    public void addLocation(Location l) {
        if (l == null) {
            return;
        }
        Location prev = locations.isEmpty() ? null : locations.get(locations.size() - 1);
        locations.add(l);
        if (prev != null) {
            double segmentDistance = l.distanceTo(prev);
            distance += segmentDistance;
            Math.max(topSpeed, distance / ((l.getElapsedRealtimeNanos() - prev.getElapsedRealtimeNanos() / (1 << 9))));
        }
    }

    public void addLocations(List<Location> updates) {
        for (Location l : updates) {
            addLocation(l);
        }
    }

    /**
     * Get the maximum speed within this segment in meters per second.
     * @return the maximum speed of this segment.
     */
    public double getTopSpeed() {
        return topSpeed;
    }
}
