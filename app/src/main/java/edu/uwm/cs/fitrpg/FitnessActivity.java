package edu.uwm.cs.fitrpg;

import android.location.Location;

import java.util.LinkedList;
import java.util.List;

public class FitnessActivity {
    private List<FitnessActivitySegment> segments = new LinkedList<>();
    private double cachedDistance;
    private double cachedDuration;
    private double cachedTopSpeed;

    public void start() {
        FitnessActivitySegment segment = new FitnessActivitySegment();
        segments.add(segment);
        segment.setStartTime(android.os.SystemClock.elapsedRealtimeNanos());
    }

    public void stop() {
        if (segments.isEmpty()) {
            throw new IllegalStateException();
        }
        FitnessActivitySegment segment = segments.get(segments.size() - 1);
        if (!segment.isStarted() || segment.isStopped()) {
            throw new IllegalStateException();
        }
        segment.setStopTime(android.os.SystemClock.elapsedRealtimeNanos());
        cachedDistance += segment.getDistance();
        cachedDuration += segment.getDuration();
        cachedTopSpeed = Math.max(cachedTopSpeed, segment.getTopSpeed());
    }

    public void addLocation(Location l) {
        if (segments.isEmpty()) {
            throw new IllegalStateException();
        }
        FitnessActivitySegment segment = segments.get(segments.size() - 1);
        if (!segment.isStarted() || segment.isStopped()) {
            throw new IllegalStateException();
        }
        segment.addLocation(l);
    }

    public void addLocations(List<Location> locations) {
        if (segments.isEmpty()) {
            throw new IllegalStateException();
        }
        FitnessActivitySegment segment = segments.get(segments.size() - 1);
        if (!segment.isStarted() || segment.isStopped()) {
            throw new IllegalStateException();
        }
        segment.addLocations(locations);
    }

    public double getDistance() {
        double d = cachedDistance;
        if (!segments.isEmpty()) {
            FitnessActivitySegment segment = segments.get(segments.size() - 1);
            if (!segment.isStopped()) {
                d += segment.getDistance();
            }
        }
        return d;
    }

    public double getDuration() {
        double d = cachedDuration;
        if (!segments.isEmpty()) {
            FitnessActivitySegment segment = segments.get(segments.size() - 1);
            if (!segment.isStopped()) {
                d += segment.getDuration();
            }
        }
        return d;
    }

    public double getAverageSpeed() {
        double dur = getDuration();
        return (dur > 0) ? getDistance() / dur : 0;
    }

    public double getTopSpeed() {
        double s = cachedTopSpeed;
        if (!segments.isEmpty()) {
            FitnessActivitySegment segment = segments.get(segments.size() - 1);
            if (!segment.isStopped()) {
                s = Math.max(s, segment.getTopSpeed());
            }
        }
        return s;
    }
}
