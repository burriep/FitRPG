package edu.uwm.cs.fitrpg.model;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.uwm.cs.fitrpg.DatabaseHelper;

import static edu.uwm.cs.fitrpg.util.Utils.ISO_DATE_TIME_FORMAT;

public class User {

    private String name;
    private int userID;
    private int weight, height;
    private Date lastUpdateDate;
    private boolean fitnessReminders;
    private Date fitnessReminderDate;

    public User() {}

    public User(String name, int id) {
        this.name = name;
        userID = id;
    }

    public User(Cursor cursor) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.US);
        userID = cursor.getInt(cursor.getColumnIndexOrThrow("usr_id"));
        name = cursor.getString(cursor.getColumnIndexOrThrow("usr_nam"));
        weight = cursor.getInt(cursor.getColumnIndexOrThrow("usr_weight"));
        height = cursor.getInt(cursor.getColumnIndexOrThrow("usr_height"));
        fitnessReminders = cursor.getInt(cursor.getColumnIndexOrThrow("usr_remind")) == 1;
        try {
            fitnessReminderDate = dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow("usr_remindTime")));
            lastUpdateDate = dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow("a_date")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public boolean isFitnessReminders() {
        return fitnessReminders;
    }

    public void setFitnessReminders(boolean remind) {
        this.fitnessReminders = remind;
    }

    public Date getFitnessReminderDate() {
        return fitnessReminderDate;
    }

    public void setFitnessReminderDate(Date date) {
        this.fitnessReminderDate = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int w) {
        weight = w;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int h) {
        height = h;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date d) {
        lastUpdateDate = d;
    }

    public void updateName(String newName) {
        name = newName;
    }

    public void updateUserID(int newID) {
        userID = newID;
    }

    public static User get(SQLiteDatabase db, int userID) {
        String sqlQuery = "SELECT usr_id,usr_nam,usr_weight,usr_height,usr_remind,usr_remindTime,a_date FROM fr_user WHERE usr_id = ?";
        String[] selectionArgs = {Integer.toString(userID)};
        Cursor c = db.rawQuery(sqlQuery, selectionArgs);
        User u = null;
        if(c.moveToNext()) {
            u = new User(c);
        }
        c.close();
        return u;
    }

    public boolean create(SQLiteDatabase db) {
        SimpleDateFormat format = new SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.ENGLISH);
        ContentValues values = new ContentValues();
        if (userID > 0) {
            values.put("usr_id", userID);
        }
        values.put("usr_nam", name);
        values.put("usr_weight", weight);
        values.put("usr_height", height);
        values.put("usr_remind", fitnessReminders ? 1 : 0);
        values.put("usr_remindTime", format.format(fitnessReminderDate));
        values.put("a_date", format.format(lastUpdateDate));

        try {
            userID = (int) db.insertOrThrow("fr_user", null, values);
            Log.d("SCS", "User.create() successful");
        } catch(SQLiteException e) {
            Log.e("ERR", "User.create() failed");
            Log.e("SYSMSG", e.getMessage());
        }
        return userID > 0;
    }

    public void update(SQLiteDatabase db) {
        SimpleDateFormat format = new SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.ENGLISH);
        ContentValues values = new ContentValues();
        values.put("usr_nam", name);
        values.put("usr_weight", weight);
        values.put("usr_height", height);
        values.put("usr_remind", fitnessReminders ? 1 : 0);
        values.put("usr_remindTime", format.format(fitnessReminderDate));
        values.put("a_date", format.format(lastUpdateDate));
        String[] whereArgs = {Integer.toString(userID)};

        try {
            db.update("fr_user", values, "usr_id = ?", whereArgs);
            Log.d("SCS", "User.update() successful");
        } catch(SQLiteException e) {
            Log.e("ERR", "User.update() failed");
            Log.e("SYSMSG", e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "ID: " + getUserID() + ": " + getName() + " Weight: " + getWeight() + ", Height: " + getHeight();
    }
}
