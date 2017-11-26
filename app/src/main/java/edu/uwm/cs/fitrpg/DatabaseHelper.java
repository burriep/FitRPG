package edu.uwm.cs.fitrpg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import edu.uwm.cs.fitrpg.model.FitnessActivityType;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "fitrpg.db";
    private static final int DATABASE_VERSION = 5;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table fr_act (" +
                "_id INTEGER PRIMARY KEY, " +
                "act_nam VARCHAR(13) UNIQUE, " +
                "act_dsc VARCHAR(50), " +
                "act_mode INTEGER(1), " +
                "act_aero INTEGER(2), " +
                "act_flex INTEGER(2), " +
                "act_musc INTEGER(2), " +
                "act_bone INTEGER(2))");

        FitnessActivityType.init(db);

        db.execSQL("create table fr_char (usr_id INTEGER(3) PRIMARY KEY, " +
                "usr_nam VARCHAR(13), " +
                "nd_pos INTEGER(2), " +
                "a_str INTEGER(2), " +
                "a_spd INTEGER(2), " +
                "a_sta INTEGER(2), " +
                "a_dex INTEGER(2), " +
                "a_end INTEGER(2))");

        db.execSQL("create table fr_hst (" +
                "_id INTEGER PRIMARY KEY, " +
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

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
