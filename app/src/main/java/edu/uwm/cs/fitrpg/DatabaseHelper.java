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
    public static Context x;


    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.x = context;

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

        db.execSQL("create table fr_map (usr_id INTEGER(3), " +
                "map_id INTEGER(2), " +
                "nd_id INTEGER(2), " +
                "nd_cmp INTEGER(1) DEFAULT 0, " +
                "nd_x_pos INTEGER DEFAULT 0, "+
                "nd_y_pos INTEGER DEFAULT 0, PRIMARY KEY(usr_id,map_id,nd_id))");

        db.execSQL("create table fr_path (map_id INTEGER(2), " +
                "nd_a INTEGER(2), " +
                "nd_b INTEGER(2), PRIMARY KEY(map_id,nd_a,nd_b))");
        //nd_a is the first node in the pair, b is the second
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
            return "" + c.getInt(0);
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
            return c.getString(0);
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
            return "" + c.getInt(0);
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

        Log.d("SQLString", sqlQuery);

        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            db.close();
            return "" + c.getInt(0);
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
            return "" + c.getInt(0);
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
            return "" + c.getInt(0);
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
                                        " AND  nd_id = " + n_id + "",null,null,null,null );
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


    public void setNodeCoord(Point coord, int map_id, int nd_id, int z)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("nd_x_pos", coord.x);
        values.put("nd_y_pos", coord.y);
        db.update("fr_map", values, "usr_id = " + z + " and map_id = " + map_id + " and nd_id = " + nd_id + "", null);
        db.close();
    }

    public void changeNodeStatus(int status, int map_id, int nd_id, int z)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("nd_cmp", status);
        db.update("fr_map", values, "usr_id = " + z + " and map_id = " + map_id + " and nd_id = " + nd_id + "", null);
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

    //db setter
    public void setStrength(int str, int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("a_str", str);

        Log.d("Values are:", "str: " + str + ", id: " + id);
        try{
          int jason =  db.update("fr_char", values, "usr_id = " + id + "", null);
            Log.d("rows", ""+jason);
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

    //this updates the RpgChar's current node postition in the db
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

    public void createChar(int usrId, int nd_pos, String usrName, int a_str, int a_spd, int a_dex, int a_end, int a_sta)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("usr_id", usrId);
        values.put("usr_nam", usrName);
        values.put("nd_pos", nd_pos);
        values.put("a_str", a_str);
        values.put("a_spd", a_spd);
        values.put("a_dex", a_dex);
        values.put("a_end", a_end);
        values.put("a_sta", a_sta);

        try{
            db.insert("fr_char", null, values);
            db.close();
        }
        catch(SQLiteException e)
        {
            db.close();
            Log.e("Insert Error", "Error inserting into db");
        }
    }

    public void createNode(int usr_id, int map_id, int nd_id, int nd_cmp, int nd_x_pos, int nd_y_pos)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("usr_id", usr_id);
        values.put("map_id", map_id);
        values.put("nd_id", nd_id);
        values.put("nd_cmp", nd_cmp);
        values.put("nd_x_pos", nd_x_pos);
        values.put("nd_y_pos", nd_y_pos);

        try{
            db.insert("fr_map", null, values);
            db.close();
        }
        catch(SQLiteException e)
        {
            db.close();
            Log.e("Insert Error", "Error inserting into db");
        }
    }

    public void updateNode(int id, int map_id, int nd_id, int nd_cmp, int nd_x_pos, int nd_y_pos)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("usr_id", id);
            values.put("map_id", map_id);
            values.put("nd_id", nd_id);
            values.put("nd_cmp", nd_cmp);
            values.put("nd_x_pos", nd_x_pos);
            values.put("nd_y_pos", nd_y_pos);

            db.update("fr_map", values, "usr_id = " + id + " and map_id = " + map_id + " and nd_id = " + nd_id + "", null);
            db.close();
        }
        catch(Exception e)
        {
            System.out.println("Error updating db");
        }
    }

    public ArrayList<int[]> getMapData(int id, int map_id)
    {
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            String sqlQuery = "select * from fr_map";
            sqlQuery += " where usr_id = " + id + " and map_id = " + map_id + "";

            Cursor c = db.rawQuery(sqlQuery, null);
            ArrayList<int[]> ret = new ArrayList<int[]>();
            db.close();
            if(c.moveToFirst()) {
                while(!c.isAfterLast())
                {
                    int rowData[] = new int[5];

                    rowData[0] = c.getInt(0);       //map_id
                    rowData[1] = c.getInt(1);       //node_id
                    rowData[2] = c.getInt(2);       //nd_complete
                    rowData[3] = c.getInt(3);       //node x pos
                    rowData[4] = c.getInt(4);       //node y pos

                    ret.add(rowData);

                    c.moveToNext();
                }

                return ret;
            }
            else {
                return null;
            }
        }
        catch(Exception e)
        {
            return null;
        }
    }



    public Context getContext()
    {
        return this.x;
    }
}
