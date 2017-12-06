package edu.uwm.cs.fitrpg;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Date;
import java.util.List;

import edu.uwm.cs.fitrpg.activity.Home;
import edu.uwm.cs.fitrpg.model.FitnessActivity;
import edu.uwm.cs.fitrpg.model.FitnessActivityType;


/**
 * Created by Jason on 11/8/17.
 *
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
    private DatabaseHelper db;
    private int loopCount;
    private int currentMap;

    //constructor for player - the player's usr_id will always be 1
    public RpgChar() {
        this.db = new DatabaseHelper(Home.appCon);
        boolean exists = dbPull(1);

        if (!exists) {
            this.id = 1;
            this.currentHealth = 0;
            this.name = "";
            this.currentNode = 2;
            this.strength = 0;
            this.speed = 0;
            this.dexterity = 0;
            this.stamina = 0;
            this.endurance = 0;
            this.loopCount =0;
            this.currentMap = 0;
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
            this.loopCount =0;
            this.currentMap = 0;
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
        this.loopCount = Integer.parseInt(db.getLoopCount(id));
        this.currentMap = Integer.parseInt(db.getCurrentMap(id));
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
        this.db.createChar(this.id, this.currentNode, this.name, this.strength, this.speed, this.dexterity, this.endurance, this.stamina, this.loopCount);

        retStatus += db.setStrength(this.strength, this.id);
        retStatus += db.setStamina(this.stamina, this.id);
        retStatus += db.setSpeed(this.speed, this.id);
        retStatus += db.setEndurance(this.endurance, this.id);
        retStatus += db.setDexterity(this.dexterity, this.id);
        retStatus += db.setCurrentNode(this.currentNode, this.id);
        retStatus += db.setLoopCount(this.loopCount, this.id);
        retStatus += db.setCurrentMap(this.currentMap, this.id);

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

    public void setLoopCount(int x)
    {
        this.loopCount = x;
    }

    public void setCurrentMap(int x)
    {
        this.currentMap = x;
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

    public int getLoopCount(){ return this.loopCount;}

    public int getCurrentMap(){return this.currentMap;}

    /*/////////////////////////////////////////////////////////////////////////////////////////////*/

    public void updateStatsFromActivities(Date startDate, Date endDate) {
        // TODO: care about the userId for the activities
        SQLiteDatabase readDb = db.getReadableDatabase();
        List<FitnessActivity> activities = FitnessActivity.getAllByDate(readDb, startDate, endDate);
        updateStatsFromActivities(activities);
    }

    public int[] peekStatsFromActivities(Date startDate, Date endDate)
    {
        SQLiteDatabase readDb = db.getReadableDatabase();
        List<FitnessActivity> activities = FitnessActivity.getAllByDate(readDb, startDate, endDate);
        return peekStatsFromActivities(activities);
    }

    public void updateStatsFromActivities(List<FitnessActivity> activities) {
        int[] updatedStats = peekStatsFromActivities(activities);
        strength += updatedStats[0];
        endurance += updatedStats[1];
        dexterity += updatedStats[2];
        speed += updatedStats[3];
    }

    public void updateStaminaFromActivities(Date startDate, Date endDate) {
        // TODO: care about the userId for the activities
        SQLiteDatabase readDb = db.getReadableDatabase();
        List<FitnessActivity> activities = FitnessActivity.getAllByDate(readDb, startDate, endDate);
        updateStaminaFromActivities(activities);
    }

    public void updateStaminaFromActivities(List<FitnessActivity> activities) {
        int[] updatedStats = peekStatsFromActivities(activities);
        stamina += updatedStats[4];
    }

    public int[] peekStatsFromActivities(List<FitnessActivity> activities)
    {
        int[] returnVal = new int[5];
        for(int i = 0; i < returnVal.length; i++)
        {
            returnVal[i] = 0;
        }
        for (FitnessActivity activity : activities) {
            FitnessActivityType type = activity.getType();
            // TODO: improve this calculation
            returnVal[0] += type.getMuscleStrengthImpact(); //Strength
            returnVal[1] += type.getAerobicImpact();        //Endurance
            returnVal[2] += type.getFlexibilityImpact();    //Dexterity
            returnVal[3] += type.getBoneStrengthImpact();   //Speed
        }
        boolean variety = returnVal[0] > 0 && returnVal[1] > 0 && returnVal[2] > 0 && returnVal[3] > 0;
        // TODO: improve this calculation
        if(variety) {
            returnVal[4] += Math.floor((returnVal[0] + returnVal[1] + returnVal[2] + returnVal[3])/4);
        }
        return returnVal;
    }
}
