package edu.uwm.cs.fitrpg.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.uwm.cs.fitrpg.util.Utils;

public class FitnessChallengeLevel {
    private int userId;
    private int fitnessTypeId;
    private int level;
    private int levelIncrement;
    private FitnessActivityType activityType;

    public static void initUser(SQLiteDatabase db, int userId) {
        List<FitnessActivityType> types = FitnessActivityType.getAll(db);
        for (FitnessActivityType type : types) {
            ContentValues values = new ContentValues();
            values.put("usr_id", userId);
            values.put("act_id", type.getId());
            values.put("ch_level", type.getImpactInterval());
            values.put("ch_increment", type.getImpactIntervalIncrement());
            db.insert("fr_challenge", null, values);
        }
    }

    public static FitnessChallengeLevel get(SQLiteDatabase db, int userId, int fitnessTypeId) {
        String[] selectionArgs = {Integer.toString(userId), Integer.toString(fitnessTypeId)};
        Cursor cursor = db.rawQuery("SELECT usr_id, act_id, ch_level, ch_increment FROM fr_challenge WHERE usr_id = ? AND act_id = ?;", selectionArgs);

        FitnessChallengeLevel level = null;
        while (cursor.moveToNext()) {
            level = new FitnessChallengeLevel();
            level.userId = cursor.getInt(cursor.getColumnIndexOrThrow("usr_id"));
            level.fitnessTypeId = cursor.getInt(cursor.getColumnIndexOrThrow("act_id"));
            level.level = cursor.getInt(cursor.getColumnIndexOrThrow("ch_level"));
            level.levelIncrement = cursor.getInt(cursor.getColumnIndexOrThrow("ch_increment"));
        }
        cursor.close();
        return level;
    }

    /**
     * Get a list of up to <code>maxChallengeCount</code> random FitnessChallengeLevel objects and
     * their associated FitnessActivityType. No duplicates will be returned.
     *
     * @param db                Database to read from
     * @param userId            User ID of user to look for challenges of
     * @param maxChallengeCount The maximum number of challenges to return. Must be positive.
     *                          If this number is greater than or equal to the number of challenges,
     *                          then all challenges will be returned in a random order.
     *                          Else, this number of challenges will be returned.
     * @return A new List of FitnessChallengeLevel, never null, never containing null items,
     * and all of the linked FitnessActivityType objects are also never null.
     */
    public static List<FitnessChallengeLevel> getRandomChallenges(SQLiteDatabase db, int userId, int maxChallengeCount) {
        Random r = new Random();
        List<FitnessActivityType> types = FitnessActivityType.getAll(db);
        List<FitnessChallengeLevel> challenges = new ArrayList<>(maxChallengeCount);
        int size = types.size();
        maxChallengeCount = Math.min(maxChallengeCount, size);
        while (challenges.size() < maxChallengeCount) {
            int index = r.nextInt(size);
            FitnessActivityType t = types.get(index);
            if (t != null) {
                FitnessChallengeLevel l = get(db, userId, t.getId());
                if (l != null) {
                    l.setActivityType(t);
                    challenges.add(l);
                }
                types.set(index, null);
            }
        }
        return challenges;
    }

    public static void increaseAllChallengeLevels(SQLiteDatabase db, int userId) {
        if (userId > 0) {
            db.execSQL("UPDATE fr_challenge SET ch_level = ch_level + ch_increment WHERE usr_id = " + userId + ";");
        }
    }

    public static void decreaseAllChallengeLevels(SQLiteDatabase db, int userId) {
        if (userId > 0) {
            db.execSQL("UPDATE fr_challenge SET ch_level = ch_level - ch_increment WHERE usr_id = " + userId + ";");
        }
    }

    public FitnessActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(FitnessActivityType activityType) {
        this.activityType = activityType;
    }

    public int getFitnessTypeId() {
        return fitnessTypeId;
    }

    public void setFitnessTypeId(int fitnessTypeId) {
        this.fitnessTypeId = fitnessTypeId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevelIncrement() {
        return levelIncrement;
    }

    public void setLevelIncrement(int levelIncrement) {
        this.levelIncrement = levelIncrement;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        String s;
        if (activityType != null) {
            switch (activityType.getImpactIntervalUnit()) {
                case FitnessActivityUnit.TIME:
                    s = Utils.formatDuration(level);
                    break;
                case FitnessActivityUnit.DISTANCE:
                    s = level + "m";
                    break;
                case FitnessActivityUnit.REPS:
                    s = level + " reps";
                    break;
                default:
                    s = "";
                    break;
            }
        } else {
            s = Integer.toString(level);
        }
        return s;
    }
}
