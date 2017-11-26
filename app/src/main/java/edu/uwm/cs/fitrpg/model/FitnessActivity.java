package edu.uwm.cs.fitrpg.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class FitnessActivity implements Serializable {
    public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private static final int NS_IN_MS = 1000000;
    private static final int MS_IN_SECOND = 1000;
    private int _id;
    private int act_id;
    private FitnessActivityType type;
    private int usr_id;
    private Date startDate;
    private Date stopDate;
    private long startTime;
    private double cachedDistance; // in meters
    private int cachedDuration; // in ms
    private double cachedTopSpeed; // in meters / s
    private int sets;
    private int repetitions;

    private Location lastLocation;
    private long segmentStartTime; // in ms
    private double segmentDistance; // in meters
    private double segmentTopSpeed; // in meters / s

    public static FitnessActivity get(SQLiteDatabase db, int id) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                "_id",
                "act_id",
                "usr_id",
                "s_tme",
                "e_tme",
                "dist",
                "dur",
                "t_spd",
                "sets",
                "reps"
        };
        // Filter results WHERE "title" = 'My Title'
        String selection = "_id = ?";
        String[] selectionArgs = {Integer.toString(id)};

        // How you want the results sorted in the resulting Cursor
        String sortOrder = "_id DESC";
        Cursor cursor = db.query("fr_hst", projection, selection, selectionArgs, null, null, sortOrder);
        FitnessActivity fa = null;
        while (cursor.moveToNext()) {
            fa = new FitnessActivity();
            fa._id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            fa.act_id = cursor.getInt(cursor.getColumnIndexOrThrow("act_id"));
            fa.usr_id = cursor.getInt(cursor.getColumnIndexOrThrow("usr_id"));
            try {
                fa.startDate = (new SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.US)).parse(cursor.getString(cursor.getColumnIndexOrThrow("s_tme")));
                fa.stopDate = (new SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.US)).parse(cursor.getString(cursor.getColumnIndexOrThrow("e_tme")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            fa.cachedDistance = cursor.getDouble(cursor.getColumnIndexOrThrow("dist"));
            fa.cachedDuration = cursor.getInt(cursor.getColumnIndexOrThrow("dur"));
            fa.cachedTopSpeed = cursor.getDouble(cursor.getColumnIndexOrThrow("t_spd"));
            fa.repetitions = cursor.getInt(cursor.getColumnIndexOrThrow("reps"));
            fa.sets = cursor.getInt(cursor.getColumnIndexOrThrow("sets"));
        }
        cursor.close();
        return fa;
    }

    public static List<FitnessActivity> getAllByDate(SQLiteDatabase db, Date startDate, Date endDate) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                "_id",
                "act_id",
                "usr_id",
                "s_tme",
                "e_tme",
                "dist",
                "dur",
                "t_spd",
                "sets",
                "reps"
        };
        // Filter results WHERE "title" = 'My Title'
        String selection = "s_tme >= ? AND s_tme < ?";
        String[] selectionArgs = {
                (new SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.US)).format(startDate),
                (new SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.US)).format(endDate)
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = "s_tme DESC, _id DESC";
        Cursor cursor = db.query("fr_hst", projection, selection, selectionArgs, null, null, sortOrder);
        List<FitnessActivity> activities = new LinkedList<>();
        while (cursor.moveToNext()) {
            FitnessActivity fa = new FitnessActivity();
            fa._id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            fa.act_id = cursor.getInt(cursor.getColumnIndexOrThrow("act_id"));
            fa.usr_id = cursor.getInt(cursor.getColumnIndexOrThrow("usr_id"));
            try {
                fa.startDate = (new SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.US)).parse(cursor.getString(cursor.getColumnIndexOrThrow("s_tme")));
                fa.stopDate = (new SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.US)).parse(cursor.getString(cursor.getColumnIndexOrThrow("e_tme")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            fa.cachedDistance = cursor.getDouble(cursor.getColumnIndexOrThrow("dist"));
            fa.cachedDuration = cursor.getInt(cursor.getColumnIndexOrThrow("dur"));
            fa.cachedTopSpeed = cursor.getDouble(cursor.getColumnIndexOrThrow("t_spd"));
            fa.repetitions = cursor.getInt(cursor.getColumnIndexOrThrow("reps"));
            fa.sets = cursor.getInt(cursor.getColumnIndexOrThrow("sets"));
            activities.add(fa);
        }
        cursor.close();
        return activities;
    }

    public void start() {
        if (isTracking()) {
            throw new IllegalStateException("Already started.");
        }
        segmentStartTime = android.os.SystemClock.elapsedRealtime();
        if (startTime <= 0) {
            startTime = segmentStartTime;
            startDate = Calendar.getInstance().getTime();
        }
    }

    public void stop() {
        if (!isTracking()) {
            throw new IllegalStateException("Already stopped.");
        }
        stopDate = Calendar.getInstance().getTime();
        long stopTime = android.os.SystemClock.elapsedRealtime();
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
        if (!type.tracksDistance()) {
            return;
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

    public void setDistance(double distance) {
        this.cachedDistance = distance;
    }

    public long getDuration() {
        long d = cachedDuration;
        if (isTracking()) {
            d += (android.os.SystemClock.elapsedRealtime() - segmentStartTime);
        }
        return d;
    }

    public void setDuration(int duration) {
        this.cachedDuration = duration;
    }

    public double getAverageSpeed() {
        double dur = getDuration();
        return (dur > 0) ? (getDistance() * MS_IN_SECOND / dur) : 0;
    }

    public double getTopSpeed() {
        return Math.max(cachedTopSpeed, segmentTopSpeed);
    }

    public void setTopSpeed(double topSpeed) {
        this.cachedTopSpeed = topSpeed;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public FitnessActivityType getType() {
        return type;
    }

    public void setType(FitnessActivityType type) {
        this.type = type;
        if (this.type != null) {
            act_id = this.type.getId();
        }
    }

    public FitnessActivityType getType(SQLiteDatabase db) {
        if (type == null && act_id > 0) {
            type = FitnessActivityType.get(db, act_id);
        }
        return type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStopDate() {
        return stopDate;
    }

    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }

    public int getAccountId() {
        return act_id;
    }

    public void setAccountId(int act_id) {
        this.act_id = act_id;
    }

    public int getUserId() {
        return usr_id;
    }

    public void setUserId(int usr_id) {
        this.usr_id = usr_id;
    }

    public boolean create(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("act_id", act_id);
        values.put("usr_id", usr_id);
        values.put("act_type", "Tracked FitnessOverview");
        values.put("s_tme", (new SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.US)).format(startDate));
        values.put("e_tme", (new SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.US)).format(stopDate));
        values.put("dist", getDistance());
        values.put("dur", getDuration());
        values.put("t_spd", getTopSpeed());
        values.put("sets", getSets());
        values.put("reps", getRepetitions());
        _id = (int) db.insert("fr_hst", null, values);
        return _id > 0;
    }
}
