package edu.uwm.cs.fitrpg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Jason on 10/24/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "fitrpg.db";
    private static final int DATABASE_VERSION = 3;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        //QLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//<<<<<<< Updated upstream
//        db.execSQL("create table fr_act (act_id INTEGER(2) PRIMARY KEY, act_dsc VARCHAR(50))");
//        db.execSQL("create table fr_char (usr_id INTEGER(3) PRIMARY KEY, usr_nam VARCHAR(13), nd_pos INTEGER(2), a_str INTEGER(2), a_spd INTEGER(2), a_sta INTEGER(2), a_dex INTEGER(2), a_end INTEGER(2))");
//        db.execSQL("create table fr_hst (act_num INTEGER(4) PRIMARY KEY, " +
//                "act_id INTEGER(2) NOT NULL, " +
//                "usr_id INTEGER(3) NOT NULL, " +
//                "s_tme TEXT NOT NULL, " +
//                "e_tme TEXT, " +
//                "dist REAL NOT NULL, " +
//                "dur INTEGER(4) NOT NULL, " +
//                "t_spd REAL NOT NULL, " +
//                "sets INTEGER(3) NOT NULL, " +
//                "reps INTEGER(3) NOT NULL)");
        //db.execSQL("create table fr_map (usr_id INTEGER(3) PRIMARY KEY, map_id INTEGER(2) PRIMARY KEY, nd_id INTEGER(2) PRIMARY KEY, nd_cmp INTEGER(1) DEFAULT 0)");
//=======
        db.execSQL("create table fr_act (act_id INTEGER(2) PRIMARY KEY, " +
                "act_dsc VARCHAR(50))");

        db.execSQL("create table fr_char (usr_id INTEGER(3) PRIMARY KEY, " +
                "usr_nam VARCHAR(13), " +
                "nd_pos INTEGER(2), " +
                "a_str INTEGER(2), " +
                "a_spd INTEGER(2), " +
                "a_sta INTEGER(2), " +
                "a_dex INTEGER(2), " +
                "a_end INTEGER(2))");
//>>>>>>> Stashed changes

        db.execSQL("create table fr_hst (act_num INTEGER(4) PRIMARY KEY, " +
                "act_id INTEGER(2) NOT NULL, " +
                "usr_id INTEGER(3) NOT NULL, " +
                "act_type TEXT, " +
                "s_tme TEXT NOT NULL, " +
                "e_tme TEXT, " +
                "dist REAL NOT NULL, " +
                "dur INTEGER(4) NOT NULL, " +
                "t_spd REAL NOT NULL, " +
                "sets INTEGER(3) NOT NULL, " +
                "reps INTEGER(3))");

        db.execSQL("create table fr_map (usr_id INTEGER(3) PRIMARY KEY, " +
                "map_id INTEGER(2), " +
                "nd_id INTEGER(2), " +
                "nd_cmp INTEGER(1) DEFAULT 0)");
                //"PRIMARY KEY(usr_id, map_id, nd_id)");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists fr_act");
        db.execSQL("drop table if exists fr_char");
        db.execSQL("drop table if exists fr_hst");
        db.execSQL("drop table if exists fr_map");

        onCreate(db);
    }

    public String getStamina() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select a_sta from fr_char";
        sqlQuery += " where nd_pos = (SELECT MAX(nd_pos) from fr_char)";
        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            db.close();
            return "" + c.getInt(5);
        }
        else {
            db.close();
            return "" + 0;
        }
    }

    public String getSpeed() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select a_spd from fr_char";
        sqlQuery += " where nd_pos = (SELECT MAX(nd_pos) from fr_char)";
        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            db.close();
            return "" + c.getInt(4);
        }
        else {
            db.close();
            return "" + 0;
        }
    }

    public String getStrength() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select a_str from fr_char";
        sqlQuery += " where nd_pos = (SELECT MAX(nd_pos) from fr_char)";
        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            db.close();
            return "" + c.getInt(3);
        }
        else {
            db.close();
            return "" + 0;
        }
    }

    public String getEndurance() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select a_end from fr_char";
        sqlQuery += " where nd_pos = (SELECT MAX(nd_pos) from fr_char)";
        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            db.close();
            return "" + c.getInt(7);
        }
        else {
            db.close();
            return "" + 0;
        }
    }

    public String getDexterity() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select a_dex from fr_char";
        sqlQuery += " where nd_pos = (SELECT MAX(nd_pos) from fr_char)";
        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            db.close();
            return "" + c.getInt(6);
        }
        else {
            db.close();
            return "" + 0;
        }
    }

    public void addTimeBasedData(String type, String start, String end){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("act_id", 1);
        values.put("usr_id", 1);
        values.put("act_type", type);
        values.put("s_tme", start);
        values.put("e_tme", end);
        values.put("dist", 0);
        values.put("dur", 0);
        values.put("t_spd", 0);
        values.put("sets", 0);
        values.put("reps", 0);
        db.insert("fr_hst", null, values);
        db.close();
    }

    public ArrayList<FitnessEntry> getFitnessHistory() {
        ArrayList<FitnessEntry> historyList = new ArrayList<FitnessEntry>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sqlQuery = "select * from fr_hst";

        FitnessEntry fe = null;

        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            do {
                fe = new FitnessEntry(c.getString(3));
                fe.setStartTime(c.getString(4));
                fe.setEndTime(c.getString(5));
                fe.setDistance(c.getFloat(6));
                fe.setReps(c.getInt(10));
                historyList.add(fe);
            } while(c.moveToNext());
        }

        return historyList;
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
