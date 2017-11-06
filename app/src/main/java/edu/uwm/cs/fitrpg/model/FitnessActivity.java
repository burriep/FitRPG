package edu.uwm.cs.fitrpg.model;

import android.location.Location;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class FitnessActivity implements Serializable {
    private static final int NS_IN_MS = 1000000;
    private static final int MS_IN_SECOND = 1000;
    private long startTime;
    private long stopTime;
    private double cachedDistance; // in meters
    private long cachedDuration; // in ms
    private double cachedTopSpeed; // in meters / s

    private Location lastLocation;
    private long segmentStartTime; // in ms
    private double segmentDistance; // in meters
    private double segmentTopSpeed; // in meters / s

    public void start() {
        if (isTracking()) {
            throw new IllegalStateException("Already started.");
        }
        segmentStartTime = android.os.SystemClock.elapsedRealtime();
        if (startTime <= 0) {
            startTime = segmentStartTime;
        }
    }

    public void stop() {
        if (!isTracking()) {
            throw new IllegalStateException("Already stopped.");
        }
        stopTime = android.os.SystemClock.elapsedRealtime();
        cachedDistance += segmentDistance;
        cachedDuration += (stopTime - segmentStartTime);
        cachedTopSpeed = Math.max(cachedTopSpeed, segmentTopSpeed);
        // reset for next segment
        lastLocation = null;
        segmentStartTime = 0;
        segmentDistance = 0;
        segmentTopSpeed = 0;
    }

    public boolean isTracking() {
        return segmentStartTime > 0;
    }

    public boolean hasBeenStarted() {
        return startTime > 0;
    }

    public void addLocation(Location l) {
        if (!isTracking()) {
            throw new IllegalStateException();
        }
        // Location.getElapsedRealtimeNanos() is used because it is guaranteed to be monotonically non-decreasing, unlike Location.getTime()
        long locationTimestamp = l.getElapsedRealtimeNanos() / NS_IN_MS;
        // only consider location updates during the activity.
        if (locationTimestamp >= startTime) {
            if (lastLocation != null) {
                long timeDifference = (l.getElapsedRealtimeNanos() - lastLocation.getElapsedRealtimeNanos()) / NS_IN_MS;
                // only consider location updates which are came after the previous location update.
                if (timeDifference >= 0) {
                    // find distance between this location and the last location
                    double latestDistance = l.distanceTo(lastLocation);
                    segmentDistance += latestDistance;
                    // avoid divide-by-zero
                    if (timeDifference > 0) {
                        // calculate the speed needed to travel that distance in that time, in meters per second
                        double latestTopSpeed = (latestDistance * MS_IN_SECOND) / timeDifference;
                        segmentTopSpeed = Math.max(segmentTopSpeed, latestTopSpeed);
                    }
                }
            }
            lastLocation = l;
        }
    }

    public void addLocations(List<Location> locations) {
        for (Location l : locations) {
            addLocation(l);
        }
    }

    public double getDistance() {
        return cachedDistance + segmentDistance;
    }

    public long getDuration() {
        long d = cachedDuration;
        if (isTracking()) {
            d += (android.os.SystemClock.elapsedRealtime() - segmentStartTime);
        }
        return d;
    }

    public double getAverageSpeed() {
        double dur = getDuration();
        return (dur > 0) ? (getDistance() * MS_IN_SECOND / dur) : 0;
    }

    public double getTopSpeed() {
        return Math.max(cachedTopSpeed, segmentTopSpeed);
    }
}
