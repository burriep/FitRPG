package edu.uwm.cs.fitrpg;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.uwm.cs.fitrpg.activity.Home;
import edu.uwm.cs.fitrpg.model.FitnessActivity;
import edu.uwm.cs.fitrpg.model.FitnessActivityType;
import edu.uwm.cs.fitrpg.model.FitnessActivityUnit;
import edu.uwm.cs.fitrpg.model.FitnessChallengeLevel;


/**
 * Created by Jason on 11/8/17.
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
    private DatabaseHelper db;

    //constructor for player - the player's usr_id will always be 1
    public RpgChar() {
        this.db = new DatabaseHelper(Home.appCon);
        boolean exists = dbPull(1);

        if (!exists) {
            this.id = 1;
            this.currentHealth = 0;
            this.name = "";
            this.currentNode = 0;
            this.strength = 0;
            this.speed = 0;
            this.dexterity = 0;
            this.stamina = 0;
            this.endurance = 0;
            dbPush();
        }

    }

    //constructor for enemies - use UNIQUE id's for new enemies, otherwise db constraint violation
    //  create enemy character, set attributes, then push manually
    public RpgChar(int x) {
        this.db = new DatabaseHelper(Home.appCon);
        boolean exists = dbPull(x);

        if (!exists) {
            this.id = x;
            this.currentHealth = 0;
            this.name = "";
            this.currentNode = 0;
            this.strength = 0;
            this.speed = 0;
            this.dexterity = 0;
            this.stamina = 0;
            this.endurance = 0;
            dbPush();
        }

    }

    /*|||||||||||||||||||||||||||||||||||||||||||||||||| DATABASE METHODS ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||*/
    //this method will pull the RpgChar's stat-set from the db
    public boolean dbPull(int id) {
        Log.d("DBG", "in Char dbPull");
        boolean ret = false;

        this.name = db.getName(id);
        this.strength = Integer.parseInt(db.getStrength(id));
        this.stamina = Integer.parseInt(db.getStamina(id));
        this.endurance = Integer.parseInt(db.getEndurance(id));
        this.speed = Integer.parseInt(db.getSpeed(id));
        this.dexterity = Integer.parseInt(db.getDexterity(id));
        this.currentNode = Integer.parseInt(db.getNodePosition(id));
        this.id = id;

        if (this.name != null) {
            ret = true;
            Log.d("SCS", "Pull was successful");
        } else {
            Log.d("ERR", "Could not pull data from db");
        }

        return ret;

    }

    //this method will push the RpgChar's current stat-set to the db
    public void dbPush() {
        Log.d("DBG", "in Char dbPush");
        //successful sets return 0, otherwise it returns -1
        int retStatus = 0;

        db.getReadableDatabase();
        this.db.createChar(this.id, this.currentNode, this.name, this.strength, this.speed, this.dexterity, this.endurance, this.stamina);

        retStatus += db.setStrength(this.strength, this.id);
        retStatus += db.setStamina(this.stamina, this.id);
        retStatus += db.setSpeed(this.speed, this.id);
        retStatus += db.setEndurance(this.endurance, this.id);
        retStatus += db.setDexterity(this.dexterity, this.id);
        retStatus += db.setCurrentNode(this.currentNode, this.id);

        if (retStatus == 0) {
            Log.d("SCS", "Push was successful - sum(all sets) = 0");
        } else {
            Log.d("ERR", "Could not push data to db - now trying to create character");
        }

    }

    /*|||||||||||||||||||||||||||||||||||||||||||||||||| GETTERS AND SETTERS ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||*/
    public void setStrength(int x) {

        this.strength = x;
    }

    public void setEndurance(int x) {

        this.endurance = x;
    }

    public void setSpeed(int x) {

        this.speed = x;
    }

    public void setDexterity(int x) {

        this.dexterity = x;
    }

    public void setStamina(int x) {

        this.stamina = x;
    }

    public void setHealth(int x) {

        this.currentHealth = x;
    }

    public void setName(String x) {

        this.name = x;
    }

    public void setCurrentNode(int x) {
        this.currentNode = x;
    }

    public int getStrength() {

        return this.strength;
    }

    public int getEndurance() {

        return this.endurance;
    }

    public int getSpeed() {

        return this.speed;
    }

    public int getDexterity() {

        return this.dexterity;
    }

    public int getStamina() {

        return this.stamina;
    }

    public int getHealth() {

        return this.currentHealth;
    }

    public String getName() {
        return this.name;
    }

    public int getCurrentNode() {
        return this.currentNode;
    }

    public int getId() {
        return this.id;
    }

    /*/////////////////////////////////////////////////////////////////////////////////////////////*/

    public void updateStatsFromActivities(Date startDate, Date endDate) {
        // calculate stat increase
        int[] updatedStats = peekStatsFromActivities(startDate, endDate);
        // update stats
        strength += updatedStats[0];
        endurance += updatedStats[1];
        dexterity += updatedStats[2];
        speed += updatedStats[3];
        stamina += updatedStats[4];
    }

    public int[] peekStatsFromActivities(Date startDate, Date endDate) {
        SQLiteDatabase readDb = db.getReadableDatabase();
        List<FitnessActivity> activities = FitnessActivity.getAllByDate(readDb, 1, startDate, endDate);
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

    public void updateChallengeStatsFromActivities(Date startDate, Date endDate, FitnessChallengeLevel challenge) {
        // calculate stat increase
        int[] updatedStats = peekChallengeStatsFromActivities(startDate, endDate, challenge);
        // update stats
        strength += updatedStats[0];
        endurance += updatedStats[1];
        dexterity += updatedStats[2];
        speed += updatedStats[3];
        stamina += updatedStats[4];
    }

    public int[] peekChallengeStatsFromActivities(Date startDate, Date endDate, FitnessChallengeLevel challenge) {
        boolean completedChallenge = challengeIsCompleted(startDate, endDate, challenge);
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

    public boolean challengeIsCompleted(Date startDate, Date endDate, FitnessChallengeLevel challenge) {
        SQLiteDatabase readDb = db.getReadableDatabase();
        FitnessActivityType type = challenge.getActivityType();
        List<FitnessActivity> activities = FitnessActivity.getAllByDateType(readDb, 1, startDate, endDate, type.getId());
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
