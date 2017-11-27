package edu.uwm.cs.fitrpg.model;


import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import edu.uwm.cs.fitrpg.DatabaseHelper;

public class User {

    private String name;
    private int userID;
    private int weight, height;
    private String lastUpdateDate = null;
    public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public User(String name, int id) {
        this.name = name;
        userID = id;
    }

    // **NOTE** Need to create user table to hold user stats


    public String getName() {
        return name;
    }

    public int getUserID() {
        return userID;
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void updateName(String newName) {
        name = newName;
    }

    public void updateUserID(int newID) {
        userID = newID;
    }

    public void setName(String n) {
        name = n;
    }

    public void setWeight(int w) {
        weight = w;
    }

    public void setHeight(int h) {
        height = h;
    }

    public void setLastUpdateDate(String d){
       String date = d;
        try {
            date = (new SimpleDateFormat(ISO_DATE_TIME_FORMAT)).parse(d).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        lastUpdateDate = date;
    }

    public void updateUser(DatabaseHelper db) {
        db.createUser(name, weight, height, lastUpdateDate);
        db.close();
    }

    @Override
    public String toString() {
        return "ID: " + getUserID() + ": " + getName() + " Weight: " + getWeight() + ", Height: " + getHeight();
    }
}
