package edu.uwm.cs.fitrpg;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import edu.uwm.cs.fitrpg.model.FitnessActivity;
import edu.uwm.cs.fitrpg.model.FitnessActivityType;
import edu.uwm.cs.fitrpg.model.FitnessActivityUnit;
import edu.uwm.cs.fitrpg.model.FitnessChallengeLevel;

import static edu.uwm.cs.fitrpg.util.Utils.ISO_DATE_TIME_FORMAT;

/**
 * Created by Jason on 11/8/17.
 * <p>
 * This class will represent the playable character - either the user's character, or an enemy character
 */
public class RpgChar {
    private int id;
    private String name;
    private int currentNode;
    private int currentHealth;
    private int strength;
    private int speed;
    private int dexterity;
    private int stamina;
    private int endurance;
    private int loopCount;
    private int currentMap;
    private Date lastCheckedTime;
    private int currentChallengeID;
    private int challengeDestinationNode;
    private int challengeFlag;  //0 is none, 1 is movement, 2 is boss

    public RpgChar() {
        currentChallengeID = -1;
    }

    public RpgChar(Cursor cursor) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.US);
        id = cursor.getInt(cursor.getColumnIndexOrThrow("usr_id"));
        name = cursor.getString(cursor.getColumnIndexOrThrow("usr_nam"));
        currentNode = cursor.getInt(cursor.getColumnIndexOrThrow("nd_pos"));
        currentHealth = 0;
        strength = cursor.getInt(cursor.getColumnIndexOrThrow("a_str"));
        speed = cursor.getInt(cursor.getColumnIndexOrThrow("a_spd"));
        stamina = cursor.getInt(cursor.getColumnIndexOrThrow("a_sta"));
        dexterity = cursor.getInt(cursor.getColumnIndexOrThrow("a_dex"));
        endurance = cursor.getInt(cursor.getColumnIndexOrThrow("a_end"));
        loopCount = cursor.getInt(cursor.getColumnIndexOrThrow("loop_cnt"));
        currentMap = cursor.getInt(cursor.getColumnIndexOrThrow("map_id"));
        currentChallengeID = cursor.getInt(cursor.getColumnIndexOrThrow("current_challenge_id"));
        challengeDestinationNode = cursor.getInt(cursor.getColumnIndexOrThrow("challenge_dest_node"));
        challengeFlag = cursor.getInt(cursor.getColumnIndexOrThrow("challenge_flag"));
        try {
            lastCheckedTime = dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow("last_check_time")));
        } catch (ParseException e) {
            lastCheckedTime = Calendar.getInstance().getTime();
        }
    }

    /**
     * This method will pull the RpgChar's stat-set from the db.
     *
     * @param db
     * @param id
     * @return
     */
    public static RpgChar get(SQLiteDatabase db, int id) {
        String sqlQuery = "SELECT usr_id,usr_nam,nd_pos,a_str,a_spd,a_sta,a_dex,a_end,loop_cnt,map_id,last_check_time,current_challenge_id,challenge_dest_node,challenge_flag FROM fr_char WHERE usr_id = ?";
        String[] selectionArgs = {Integer.toString(id)};
        Cursor c = db.rawQuery(sqlQuery, selectionArgs);
        RpgChar u = null;
        if (c.moveToNext()) {
            u = new RpgChar(c);
        }
        c.close();
        return u;
    }

    /**
     * This method will delete an RpgChar as identified by id.
     *
     * @param db
     * @param id
     */
    public static void delete(SQLiteDatabase db, int id) {
        db.delete("fr_char", "usr_id = ?", new String[]{Integer.toString(id)});
    }

    /**
     * This method will push the RpgChar's current stat-set to the db. If id > 0, this will be the new id, otherwise a new id will automatically be set.
     *
     * @param db
     * @return
     */
    public boolean create(SQLiteDatabase db) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.US);
        ContentValues values = new ContentValues();
        if (id > 0) {
            values.put("usr_id", id);
        }
        values.put("usr_nam", name);
        values.put("nd_pos", currentNode);
        // currentHealth;
        values.put("a_str", strength);
        values.put("a_spd", speed);
        values.put("a_sta", stamina);
        values.put("a_dex", dexterity);
        values.put("a_end", endurance);
        values.put("loop_cnt", loopCount);
        values.put("map_id", currentMap);
        values.put("last_check_time", lastCheckedTime == null ? "" : dateFormat.format(lastCheckedTime));
        id = (int) db.insert("fr_char", null, values);
        return id > 0;
    }

    /**
     * This method will push the RpgChar's current stat-set to the db, as identified by the id.
     *
     * @param db
     */
    public void update(SQLiteDatabase db) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.US);
        ContentValues values = new ContentValues();
        values.put("usr_nam", name);
        values.put("nd_pos", currentNode);
        // currentHealth;
        values.put("a_str", strength);
        values.put("a_spd", speed);
        values.put("a_sta", stamina);
        values.put("a_dex", dexterity);
        values.put("a_end", endurance);
        values.put("loop_cnt", loopCount);
        values.put("map_id", currentMap);
        values.put("last_check_time", lastCheckedTime == null ? "" : dateFormat.format(lastCheckedTime));
        db.update("fr_char", values, "usr_id = ?", new String[]{Integer.toString(id)});
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String x) {

        this.name = x;
    }

    public int getCurrentNode() {
        return this.currentNode;
    }

    public void setCurrentNode(int x) {
        this.currentNode = x;
    }

    public int getHealth() {

        return this.currentHealth;
    }

    public void setHealth(int x) {

        this.currentHealth = x;
    }

    public void setCurrentChallengeID(int x) {this.currentChallengeID = x;}

    public void setChallengeFlag(int x){this.challengeFlag = x;}

    public void setChallengeDestinationNode(int x){this.challengeDestinationNode = x;}

    public int getStrength() {

        return this.strength;
    }

    public void setStrength(int x) {

        this.strength = x;
    }

    public int getSpeed() {

        return this.speed;
    }

    public void setSpeed(int x) {

        this.speed = x;
    }

    public int getDexterity() {

        return this.dexterity;
    }

    public void setDexterity(int x) {

        this.dexterity = x;
    }

    public int getStamina() {

        return this.stamina;
    }

    public void setStamina(int x) {

        this.stamina = x;
    }

    public int getEndurance() {

        return this.endurance;
    }

    public void setEndurance(int x) {

        this.endurance = x;
    }

    public Date getLastCheckedTime() {
        return lastCheckedTime;
    }

    public void setLastCheckedTime(Date x) {
        this.lastCheckedTime = x;
    }

    public int getCurrentChallengeID(){return this.currentChallengeID;}

    public int getChallengeFlag(){return this.challengeFlag;}

    public int getChallengeDestinationNode(){return this.challengeDestinationNode;}

    public int getLoopCount() {
        return this.loopCount;
    }

    public void setLoopCount(int x) {
        this.loopCount = x;
    }

    public int getCurrentMap() {
        return this.currentMap;
    }

    public void setCurrentMap(int x) {
        this.currentMap = x;
    }

    public void updateStatsFromActivities(SQLiteDatabase db, Date startDate, Date endDate) {
        // calculate stat increase
        int[] updatedStats = peekStatsFromActivities(db, startDate, endDate);
        // update stats
        strength += updatedStats[0];
        endurance += updatedStats[1];
        dexterity += updatedStats[2];
        speed += updatedStats[3];
        stamina += updatedStats[4];
    }

    public int[] peekStatsFromActivities(SQLiteDatabase db, Date startDate, Date endDate) {
        List<FitnessActivity> activities = FitnessActivity.getAllByDate(db, 1, startDate, endDate);
        // group activities by type so that smaller activities can get added together
        Map<FitnessActivityType, List<FitnessActivity>> groups = new HashMap<>(activities.size());
        for (FitnessActivity activity : activities) {
            FitnessActivityType type = activity.getType();
            if (groups.containsKey(type)) {
                groups.get(type).add(activity);
            } else {
                List<FitnessActivity> list = new LinkedList<>();
                list.add(activity);
                groups.put(type, list);
            }
        }
        int[] returnVal = new int[5];
        for (Map.Entry<FitnessActivityType, List<FitnessActivity>> entry : groups.entrySet()) {
            FitnessActivityType type = entry.getKey();
            double distance = 0;
            int duration = 0;
            int reps = 0;
            for (FitnessActivity activity : entry.getValue()) {
                distance += activity.getDistance();
                duration += activity.getDuration();
                reps += activity.getSets() * activity.getRepetitions();
            }
            int iterations = 0;
            switch (type.getImpactIntervalUnit()) {
                case FitnessActivityUnit.TIME:
                    iterations = duration / type.getImpactInterval();
                    break;
                case FitnessActivityUnit.DISTANCE:
                    iterations = (int) Math.floor(distance / type.getImpactInterval());
                    break;
                case FitnessActivityUnit.REPS:
                    iterations = reps / type.getImpactInterval();
                    break;
                default:
                    break;
            }
            // Strength
            returnVal[0] += iterations * type.getMuscleStrengthImpact();
            // Endurance
            returnVal[1] += iterations * type.getAerobicImpact();
            // Dexterity
            returnVal[2] += iterations * type.getFlexibilityImpact();
            // Speed
            returnVal[3] += iterations * type.getBoneStrengthImpact();
        }
        if (returnVal[0] > 0 && returnVal[1] > 0 && returnVal[2] > 0 && returnVal[3] > 0) {
            // ensure variety to get stamina points
            returnVal[4] += (returnVal[0] + returnVal[1] + returnVal[2] + returnVal[3]) / 4;
        }
        return returnVal;
    }

    public void updateChallengeStatsFromActivities(SQLiteDatabase db, Date startDate, Date endDate, FitnessChallengeLevel challenge) {
        // calculate stat increase
        int[] updatedStats = peekChallengeStatsFromActivities(db, startDate, endDate, challenge);
        // update stats
        strength += updatedStats[0];
        endurance += updatedStats[1];
        dexterity += updatedStats[2];
        speed += updatedStats[3];
        stamina += updatedStats[4];
    }

    public int[] peekChallengeStatsFromActivities(SQLiteDatabase db, Date startDate, Date endDate, FitnessChallengeLevel challenge) {
        boolean completedChallenge = challengeIsCompleted(db, startDate, endDate, challenge);
        int[] returnVal = new int[5];
        if (completedChallenge) {
            // Strength
            returnVal[0] += 1;
            // Endurance
            returnVal[1] += 1;
            // Dexterity
            returnVal[2] += 1;
            // Speed
            returnVal[3] += 1;
            // Stamina
            returnVal[4] += 1;
        }
        return returnVal;
    }

    public boolean challengeIsCompleted(SQLiteDatabase db, Date startDate, Date endDate, FitnessChallengeLevel challenge) {
        FitnessActivityType type = challenge.getActivityType();
        List<FitnessActivity> activities = FitnessActivity.getAllByDateType(db, 1, startDate, endDate, type.getId());
        double distance = 0;
        int duration = 0;
        int reps = 0;
        for (FitnessActivity activity : activities) {
            distance += activity.getDistance();
            duration += activity.getDuration();
            reps += activity.getSets() * activity.getRepetitions();
        }
        boolean completedChallenge = false;
        switch (type.getImpactIntervalUnit()) {
            case FitnessActivityUnit.TIME:
                completedChallenge = duration >= challenge.getLevel();
                break;
            case FitnessActivityUnit.DISTANCE:
                completedChallenge = distance >= challenge.getLevel();
                break;
            case FitnessActivityUnit.REPS:
                completedChallenge = reps >= challenge.getLevel();
                break;
            default:
                break;
        }
        return completedChallenge;
    }
}
