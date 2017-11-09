package edu.uwm.cs.fitrpg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Point;
import android.util.Log;


import java.util.ArrayList;

/**
 * Created by Jason on 10/24/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "fitrpg.db";
    private static final int DATABASE_VERSION = 3;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
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
                "map_id INTEGER(2) PRIMARY KEY, " +
                "nd_id INTEGER(2) PRIMARY KEY, " +
                "nd_cmp INTEGER(1) PRIMARY KEY DEFAULT 0, " +
                "nd_x_pos INTEGER DEFAULT 0, "+
                "nd_y_pos INTEGER DEFAULT 0)");

        db.execSQL("create table fr_path (map_id INTEGER(2) PRIMARY KEY, " +
                "nd_a INTEGER(2) PRIMARY KEY, " +
                "nd_b INTEGER(2) PRIMARY KEY)");
        //nd_a is the first node in the pair, b is the second


        ContentValues values = new ContentValues();
        values.put("map_id", 1);
        values.put("nd_a", 33);
        values.put("nd_b", 22);
        Log.i("joopy", values.toString());
       // db.insert("fr_path", null, values);
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
        db.execSQL("drop table if exists fr_path");

        onCreate(db);
    }

    public String getStamina(int x) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select a_sta from fr_char";
        sqlQuery += " where usr_id = " + x + "";
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

    public String getName(int x) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select usr_nam from fr_char";
        sqlQuery += " where usr_id = " + x + "";
        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            db.close();
            return c.getString(1);
        }
        else {
            db.close();
            return "" + 0;
        }
    }

    public String getSpeed(int x) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select a_spd from fr_char";
        sqlQuery += " where usr_id = " + x + "";
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

    public String getStrength(int x) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select a_str from fr_char";
        sqlQuery += " where usr_id = " + x + "";
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

    public String getEndurance(int x) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select a_end from fr_char";
        sqlQuery += " where usr_id = " + x + "";
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

    public String getDexterity(int x) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select a_dex from fr_char";
        sqlQuery += " where usr_id = " + x + "";
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

        try{
            db.insert("fr_hst", null, values);
            db.close();
        }
        catch(SQLiteException e)
        {
            db.close();
            Log.e("Insert Error", "Error inserting into db");
        }
    }

    //get the screen x,y coordinates for the specified node from db
    public Point getNodeCoord(int m_id, int n_id, int u_id)
    {
        Point retCoord = new Point(0,0);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("fr_map", new String[]{"nd_x_pos","nd_y_pos"},"usr_id = " + u_id + " AND map_id = " + m_id +
                                        " AND  nd_id = " + n_id,null,null,null,null );
        if(cursor.moveToFirst())
        {
            db.close();
            retCoord = new Point(cursor.getInt(0), cursor.getInt(1));
            return retCoord;
        }
        else
        {
             db.close();
             return null; //error occurred
        }
    }

    //todo
//    public void setNodeCoord(Pair coord)
//    {
//        SQLiteDatabase db = this.getReadableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("nd_x_pos", first(coord));
//        db.update("fr_char", values, "usr_id = " + id + "", null);
//        db.close();
//    }


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

    //db setter
    public void setStrength(int str, int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("a_str", str);

        try{
            db.update("fr_char", values, "usr_id = " + id + "", null);
            db.close();
        }
        catch(SQLiteException e)
        {
            db.close();
            Log.e("Insert Error", "Error inserting into db");
        }

    }

    //db setter
    public void setSpeed(int spd, int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("a_spd", spd);

        try{
            db.update("fr_char", values, "usr_id = " + id + "", null);
            db.close();
        }
        catch(SQLiteException e)
        {
            db.close();
            Log.e("Insert Error", "Error inserting into db");
        }
    }

    //db setter
    public void setDexterity(int dex, int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("a_dex", dex);
        db.update("fr_char", values, "usr_id = " + id + "", null);
        db.close();
    }

    //db setter
    public void setEndurance(int end, int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("a_end", end);

        try{
            db.update("fr_char", values, "usr_id = " + id + "", null);
            db.close();
        }
        catch(SQLiteException e)
        {
            db.close();
            Log.e("Insert Error", "Error inserting into db");
        }
    }

    //db setter
    public void setStamina(int sta, int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("a_sta", sta);
        try{
            db.update("fr_char", values, "usr_id = " + id + "", null);
            db.close();
        }
        catch(SQLiteException e)
        {
            db.close();
            Log.e("Insert Error", "Error inserting into db");
        }
    }

    //this updates the character's current node postition in the db
    public void setCurrentNode(int nd, int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("nd_pos", nd);
        try{
            db.update("fr_char", values, "usr_id = " + id + "", null);
            db.close();
        }
        catch(SQLiteException e)
        {
            db.close();
            Log.e("Insert Error", "Error inserting into db");

        }
    }
}
