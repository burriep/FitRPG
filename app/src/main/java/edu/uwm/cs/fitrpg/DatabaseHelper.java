package edu.uwm.cs.fitrpg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Point;
import android.util.Log;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.uwm.cs.fitrpg.model.FitnessActivity;
import edu.uwm.cs.fitrpg.model.FitnessActivityType;
import edu.uwm.cs.fitrpg.model.User;
import edu.uwm.cs.fitrpg.util.Utils;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "fitrpg.db";
    private static final int DATABASE_VERSION = 8;
    public static Context x;


    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.x = context;

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
                "act_bone INTEGER(2), " +
                "act_int INTEGER(2), " +
                "act_intUnit INTEGER(2), " +
                "act_intInc INTEGER(2))");

        FitnessActivityType.init(db);

        db.execSQL("create table fr_char (usr_id INTEGER(3) PRIMARY KEY, " +
                "usr_nam VARCHAR(13), " +
                "nd_pos INTEGER(2), " +
                "a_str INTEGER(2), " +
                "a_spd INTEGER(2), " +
                "a_sta INTEGER(2), " +
                "a_dex INTEGER(2), " +
                "a_end INTEGER(2), " +
                "loop_cnt INTEGER,"+
                "last_check_time TEXT,"+
                "current_challenge_id INTEGER(2),"+
                "challenge_flag INTEGER(2),"+
                "challenge_dest_node INTEGER(2),"+
                "map_id INTEGER)  ");

        db.execSQL("create table fr_user (usr_id INTEGER(3) PRIMARY KEY, " +
                "usr_nam VARCHAR(13), " +
                "usr_weight INTEGER(2), " +
                "usr_height INTEGER(2), " +
                "a_date TEXT)");

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

        db.execSQL("create table fr_map (usr_id INTEGER(3), " +
                "map_id INTEGER(2), " +
                "nd_id INTEGER(2), " +
                "nd_cmp INTEGER(1) DEFAULT 0, " +
                "nd_x_pos INTEGER DEFAULT 0, "+
                "nd_y_pos INTEGER DEFAULT 0, " +
                "adj_nd_x_pos INTEGER DEFAULT 0, "+
                "adj_nd_y_pos INTEGER DEFAULT 0, " +
                "act_id INTEGER(2), "+
                "boss_flg INTEGER(1), "+
                "PRIMARY KEY(usr_id,map_id,nd_id))");

        //dates will be in YYYY-MM-DD HH:MM:SS.SSS format
        db.execSQL("create table fr_path (map_id INTEGER(2), " +
                "nd_a INTEGER(2), " +
                "nd_b INTEGER(2), " +
                "trvl_sts INTEGER(1)," +
                "trvl_s_time TEXT, " +
                "trvl_e_time TEXT, " +
                "PRIMARY KEY(map_id,nd_a,nd_b))");
        //nd_a is the first node in the pair, b is the second

        db.execSQL("CREATE TABLE fr_challenge (" +
                "usr_id INTEGER NOT NULL," +
                "act_id INTEGER NOT NULL," +
                "ch_level REAL NOT NULL," +
                "ch_increment REAL NOT NULL," +
                "PRIMARY KEY(usr_id,act_id))");
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
        db.execSQL("drop table if exists fr_challenge");

        onCreate(db);
    }

    public Context getContext()
    {
        return this.x;
    }

    /*|||||||||||||||||||||||||||||||||||||||||||||||||| GETTER METHODS ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||*/
    public String getStamina(int x) {
        String ret = "";

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select a_sta from fr_char";
        sqlQuery += " where usr_id = " + x + "";
        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            ret = "" + c.getInt(0);
        }
        else {
            Log.d("ERR", "Error getting stamina from db - returning -1");
            ret = "-1";
        }

        db.close();
        return ret;
    }

    public String getName(int x) {
        String ret = "";

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select usr_nam from fr_char";
        sqlQuery += " where usr_id = " + x + "";
        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            ret = c.getString(0);
        }
        else {
            Log.d("ERR", "Error getting name from db - returning null");
            ret = null;
        }

        db.close();
        return ret;
    }

    public String getSpeed(int x) {
        String ret = "";

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select a_spd from fr_char";
        sqlQuery += " where usr_id = " + x + "";
        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            ret = "" + c.getInt(0);
        }
        else {
            Log.d("ERR", "Error getting speed from db - returning -1");
            ret = "-1";
        }

        db.close();
        return ret;
    }

    public String getStrength(int x) {
        String ret = "";

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select a_str from fr_char";
        sqlQuery += " where usr_id = " + x + "";

        Log.d("SQLString", sqlQuery);

        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            ret = "" + c.getInt(0);
        }
        else {
            Log.d("ERR", "Error getting strength from db - returning -1");
            ret = "-1";
        }

        db.close();
        return ret;
    }

    public String getEndurance(int x) {
        String ret = "";

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select a_end from fr_char";
        sqlQuery += " where usr_id = " + x + "";
        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            ret =  "" + c.getInt(0);
        }
        else {
            Log.d("ERR", "Error getting endurance from db - returning -1");
            ret = "-1";
        }

        db.close();
        return ret;
    }

    public String getDexterity(int x) {
        String ret = "";

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select a_dex from fr_char";
        sqlQuery += " where usr_id = " + x + "";
        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            ret =  "" + c.getInt(0);
        }
        else {
            Log.d("ERR", "Error getting dexterity from db - returning -1");
            ret =  "-1";
        }

        db.close();
        return ret;
    }

    public String getLoopCount(int x) {
        String ret = "";

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select loop_cnt from fr_char";
        sqlQuery += " where usr_id = " + x + "";
        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            ret = "" + c.getInt(0);
        }
        else {
            Log.d("ERR", "Error getting loop count from db - returning -1");
            ret = "-1";
        }
        db.close();
        return ret;
    }

    public String getLastCheckedTime(int x) {
        String ret = "";

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select last_check_time from fr_char";
        sqlQuery += " where usr_id = " + x + "";
        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            ret = "" + c.getInt(0);
        }
        else {
            Log.d("ERR", "Error getting checked time from db - returning -1");
            ret =  "-1";
        }

        db.close();
        return ret;
    }

    public String getCurrentChallengeID(int x)
    {
        String ret = "";

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select current_challenge_id from fr_char";
        sqlQuery += " where usr_id = " + x + "";

        Log.d("SQLString", sqlQuery);

        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            ret = "" + c.getInt(0);
        }
        else {
            Log.d("ERR", "Error getting challenge id from db - returning -1");
            ret = "-1";
        }

        db.close();
        return ret;
    }

    public String getChallengeFlag(int x)
    {
        String ret = "";

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select challenge_flag from fr_char";
        sqlQuery += " where usr_id = " + x + "";

        Log.d("SQLString", sqlQuery);

        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            ret = "" + c.getInt(0);
        }
        else {
            Log.d("ERR", "Error getting challenge flag from db - returning -1");
            ret = "-1";
        }

        db.close();
        return ret;
    }

    public String getChallengeDestNode(int x)
    {
        String ret = "";

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select challenge_dest_node from fr_char";
        sqlQuery += " where usr_id = " + x + "";

        Log.d("SQLString", sqlQuery);

        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            ret = "" + c.getInt(0);
        }
        else {
            Log.d("ERR", "Error getting challenge destination node from db - returning -1");
            ret = "-1";
        }

        db.close();
        return ret;
    }



    public String getCurrentMap(int x) {
        String ret = "";

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select map_id from fr_char";
        sqlQuery += " where usr_id = " + x + "";
        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            ret = "" + c.getInt(0);
        }
        else {
            Log.d("ERR", "Error getting char's map id from db - returning -1");
            ret = "-1";
        }
        db.close();
        return ret;

    }

    public String getNodePosition(int x) {
        String ret = "";

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select nd_pos from fr_char";
        sqlQuery += " where usr_id = " + x + "";
        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            ret = "" + c.getInt(0);
        }
        else {
            Log.d("ERR", "Error getting node pos from db - returning -1");
            ret = "-1";
        }
        db.close();
        return ret;
    }

    //get the screen x,y coordinates for the specified node from db
    public int[] getNodeCoord(int m_id, int n_id, int u_id)
    {
        int[] retCoord = new int[4];

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("fr_map", new String[]{"nd_x_pos","nd_y_pos","adj_nd_x_pos","adj_nd_y_pos"},"usr_id = " + u_id + " AND map_id = " + m_id +
                                        " AND  nd_id = " + n_id + "",null,null,null,null );
        try
        {
            cursor.moveToFirst();
            retCoord[0] = cursor.getInt(0);
            retCoord[1] = cursor.getInt(1);
            retCoord[2] = cursor.getInt(2);
            retCoord[3] = cursor.getInt(3);
            Log.d("MSG", "Node found in Database - returning coordinates");
        }
        catch(Exception e)
        {
            retCoord = null;
            Log.d("ERR", "Error getting node Coordinates - returning null");
            Log.d("SYSMSG", e.toString());
        }
        db.close();
        return retCoord;
    }

    public int[] getNodeStatus(int m_id, int n_id, int u_id)
    {
        int[] retSts = new int[3];

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("fr_map", new String[]{"nd_cmp", "act_id", "boss_flg"},"usr_id = " + u_id + " AND map_id = " + m_id +
                " AND  nd_id = " + n_id + "",null,null,null,null );
        try
        {
            cursor.moveToFirst();
            retSts[0] = cursor.getInt(0);   //node status
            retSts[1] = cursor.getInt(1);   //node challenge ID
            retSts[2] = cursor.getInt(2);   //boss flag
            Log.d("MSG", "Node found in Database - returning status");
        }
        catch(Exception e)
        {
            retSts = null;
            Log.d("ERR", "Error getting node Status - returning -1");
            Log.d("SYSMSG", e.toString());
        }
        db.close();
        return retSts;
    }

    public ArrayList<String[]> getPathData(int map_id)
    {
        ArrayList<String[]> ret = new ArrayList<String[]>();

        //each String array will represent a unique path
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            String sqlQuery = "select * from fr_path";
            sqlQuery += " where map_id = " + map_id + "";

            Cursor c = db.rawQuery(sqlQuery, null);
            if(c.moveToFirst()) {
                while(!c.isAfterLast())
                {
                    String rowData[] = new String[6];

                    rowData[0] = Integer.toString(c.getInt(0));       //map_id
                    rowData[1] = Integer.toString(c.getInt(1));       //node_a
                    rowData[2] = Integer.toString(c.getInt(2));       //node_b
                    rowData[3] = Integer.toString(c.getInt(3));       //trvl_sts
                    rowData[4] = c.getString(4);       //trvl_s_time
                    rowData[5] = c.getString(5);       //trvl_e_time

                    ret.add(rowData);

                    c.moveToNext();
                }
                db.close();
                return ret;
            }
            else {
                Log.d("ERR", "No Path Info Returned - returning null");
                db.close();
                return null;
            }
        }
        catch(Exception e)
        {
            Log.d("ERR", "Error Getting Path Info - returning null");
            return null;
        }
    }

    public ArrayList<int[]> getMapData(int id, int map_id)
    {
        //each int array in the arraylist will represent a unique MapNode
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            String sqlQuery = "select * from fr_map";
            sqlQuery += " where usr_id = " + id + " and map_id = " + map_id + "";

            Cursor c = db.rawQuery(sqlQuery, null);
            ArrayList<int[]> ret = new ArrayList<int[]>();

            if(c.moveToFirst()) {
                while(!c.isAfterLast())
                {
                    int rowData[] = new int[9];
                    rowData[0] = c.getInt(1);       //map_id
                    rowData[1] = c.getInt(2);       //node_id
                    rowData[2] = c.getInt(3);       //nd_complete
                    rowData[3] = c.getInt(4);       //node x pos
                    rowData[4] = c.getInt(5);       //node y pos
                    rowData[5] = c.getInt(6);       //adjusted x pos
                    rowData[6] = c.getInt(7);       //adjusted y pos
                    rowData[7] = c.getInt(8);       //challenge activity ID
                    rowData[8] = c.getInt(9);       //Boss flag

                    ret.add(rowData);

                    c.moveToNext();
                }
                db.close();
                return ret;
            }
            else {
                Log.d("ERR", "Error getting map info - returning null");
                db.close();
                return null;
            }
        }
        catch(Exception e)
        {
            Log.d("ERR", "Error getting map info - returning null");
            return null;
        }
    }

    //******* GETTER METHODS FOR USER TABLE ************//
    public String getUserName() {
        String ret = "";

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select * from fr_user ORDER BY usr_id DESC LIMIT 1";
        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            ret = "" + c.getString(1);
        }
        else {
            Log.d("ERR", "Error getting name from db - returning null");
            ret = null;
        }
        db.close();
        return ret;
    }

    public int getUserWeight() {
        int ret = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select * from fr_user ORDER BY usr_id DESC LIMIT 1";
        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            ret = c.getInt(2);
        }
        else {
            Log.d("ERR", "Error getting name from db - returning null");
            ret = -1;
        }

        db.close();
        return ret;
    }

    public int getUserHeight() {
        int ret = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select * from fr_user ORDER BY usr_id DESC LIMIT 1";
        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            ret = c.getInt(3);
        }
        else {
            Log.d("ERR", "Error getting name from db - returning null");
            ret = -1;
        }

        db.close();
        return ret;
    }

    public String getUserDate() {
        String ret = "";

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select * from fr_user ORDER BY usr_id DESC LIMIT 1";
        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            ret = c.getString(4);
        }
        else {
            Log.d("ERR", "Error getting name from db - returning null");
            ret = null;
        }

        db.close();
        return ret;
    }

    public User getUser() {
        User user = new User(getUserName(), 1);
        user.setHeight(getUserHeight());
        user.setWeight(getUserWeight());
        user.setLastUpdateDate(getUserDate());
        return user;
    }

    public boolean hasUser() {
        boolean ret = false;

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "select * from fr_user";
        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            ret = true;
        }
        else {
            Log.d("ERR", "No user in database");
            ret = false;
        }

        db.close();
        return ret;
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }



    /*|||||||||||||||||||||||||||||||||||||||||||||||||| SETTER METHODS ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||*/
    public int setStrength(int str, int id)
    {
        int ret = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("a_str", str);

        if(db.update("fr_char", values, "usr_id = " + id + "", null) > 0)
        {
            Log.d("SCS", "Strength was sucessfully updated");
            db.close();
            ret = 0;
        }
        else
        {
            db.close();
            Log.d("ERR", "Error Updating Strength");
        }

        return ret;

    }

    public int setLoopCount(int cnt, int id)
    {
        int ret = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("loop_cnt", cnt);

        if(db.update("fr_char", values, "usr_id = " + id + "", null) > 0)
        {
            Log.d("SCS", "Loop Count was sucessfully updated");
            db.close();
            ret = 0;
        }
        else
        {
            db.close();
            Log.d("ERR", "Error Updating Loop Count");
        }

        return ret;

    }

    public int setCurrentMap(int mapId, int id)
    {
        int ret = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("map_id", mapId);

        if(db.update("fr_char", values, "usr_id = " + id + "", null) > 0)
        {
            Log.d("SCS", "Char's current map was sucessfully updated");
            db.close();
            ret = 0;
        }
        else
        {
            db.close();
            Log.d("ERR", "Error Updating char's current map");
        }





        return ret;

    }

    public int setSpeed(int spd, int id)
    {
        int ret = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("a_spd", spd);
        Log.d("Values are:", "spd: " + spd + ", id: " + id);

        if(db.update("fr_char", values, "usr_id = " + id + "", null) > 0)
        {
            Log.d("SCS", "Speed was sucessfully updated");
            db.close();
            ret = 0;
        }
        else
        {
            db.close();
            Log.d("ERR", "Error Updating Speed");
        }


        return ret;
    }

    public int setDexterity(int dex, int id)
    {
    int ret = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("a_dex", dex);
        Log.d("Values are:", "dex: " + dex + ", id: " + id);

        if(db.update("fr_char", values, "usr_id = " + id + "", null)>0)
        {
            Log.d("SCS", "Dexterity was sucessfully updated");
            db.close();
            ret = 0;
        }
        else
        {
            db.close();
            Log.d("ERR", "Error Updating Dexterity");
        }


        return ret;
    }

   public int setEndurance(int end, int id)
    {
        int ret = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("a_end", end);
        Log.d("Values are:", "end: " + end + ", id: " + id);

        if(db.update("fr_char", values, "usr_id = " + id + "", null) >0)
        {
            Log.d("SCS", "Endurance was sucessfully updated");
            db.close();
            ret = 0;
        }
       else
        {
            db.close();
            Log.d("ERR", "Error Updating Endurance");
        }


        return ret;
    }

    public int setStamina(int sta, int id)
    {
        int ret = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("a_sta", sta);
        Log.d("Values are:", "sta: " + sta + ", id: " + id);

        if(db.update("fr_char", values, "usr_id = " + id + "", null) > 0)
        {
            Log.d("SCS", "Stamina was sucessfully updated");
            db.close();
            ret = 0;
        }
        else
        {
            db.close();
            Log.d("ERR", "Error Updating Stamina");
        }



        return ret;
    }

    //this updates the RpgChar's current node position in the db
    public int setCurrentNode(int nd, int id)
    {
        int ret = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("nd_pos", nd);
        Log.d("Values are:", "nd: " + nd + ", id: " + id);

        if(db.update("fr_char", values, "usr_id = " + id + "", null) > 0)
        {
            Log.d("SCS", "Current node was sucessfully updated");
            db.close();
            ret = 0;
        }
        else
        {
            db.close();
            Log.d("ERR", "Error Updating Current Node");
        }


        return ret;
    }

    public int setCheckTime(String time, int id)
    {
        int ret = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("last_check_time", time);
        Log.d("Values are:", "nd: " + time + ", id: " + id);

        if(db.update("fr_char", values, "usr_id = " + id + "", null) > 0)
        {
            Log.d("SCS", "Current node was sucessfully updated");
            db.close();
            ret = 0;
        }
        else
        {
            db.close();
            Log.d("ERR", "Error Updating Check Time");
        }


        return ret;
    }

    public int setCurrentChallengeID(int challengeID, int id)
    {
        int ret = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("current_challenge_id", challengeID);
        Log.d("Values are:", "current_challenge_id: " + challengeID + ", id: " + id);

        if(db.update("fr_char", values, "usr_id = " + id + "", null) > 0)
        {
            Log.d("SCS", "Challenge ID was sucessfully updated");
            db.close();
            ret = 0;
        }
        else
        {
            db.close();
            Log.d("ERR", "Error Updating Challenge ID");
        }


        return ret;
    }

    public int setChallengeFlag(int flag, int id)
    {
        int ret = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("current_challenge_id", flag);
        Log.d("Values are:", "challenge_flag: " + flag + ", id: " + id);

        if(db.update("fr_char", values, "usr_id = " + id + "", null) > 0)
        {
            Log.d("SCS", "Challenge Flag was sucessfully updated");
            db.close();
            ret = 0;
        }
        else
        {
            db.close();
            Log.d("ERR", "Error Updating Challenge Flag");
        }


        return ret;
    }

    public int setChallengeDestNode(int node_id, int id)
    {
        int ret = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("challenge_dest_node", node_id);
        Log.d("Values are:", "challenge_dest_node: " + node_id + ", id: " + id);

        if(db.update("fr_char", values, "usr_id = " + id + "", null) > 0)
        {
            Log.d("SCS", "Challenge Dest Node was sucessfully updated");
            db.close();
            ret = 0;
        }
        else
        {
            db.close();
            Log.d("ERR", "Error Updating Challenge Dest Node");
        }


        return ret;
    }


    public void setNodeCoord(Point coord, Point coord2, int map_id, int nd_id, int id, int nd_cmp, int challengeID, int isBoss)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("nd_x_pos", coord.x);
        values.put("nd_y_pos", coord.y);
        values.put("adj_nd_x_pos", coord2.x);
        values.put("adj_nd_y_pos", coord2.y);
        values.put("nd_cmp", nd_cmp);
        values.put("act_id", challengeID);
        values.put("boss_flg", isBoss);

        if(db.update("fr_map", values, "usr_id = " + id + " and map_id = " + map_id + " and nd_id = " + nd_id + "", null) > 0)
            {
                db.close();
                Log.d("SCS", "Node was Updated Successfully");
            }
            else
            {
                try
                {
                    values.put("map_id", map_id);
                    values.put("nd_id", nd_id);
                    values.put("usr_id", id);
                    db.insertOrThrow("fr_map", null, values);
                    db.close();
                    Log.d("SCS", "Node was Created Successfully");
                }
                catch(SQLiteException e2)
                {
                    Log.d("ERR", "Error Creating Node");
                    Log.d("SYSMSG", e2.getMessage());
                }
            }

    }



    /*|||||||||||||||||||||||||||||||||||||||||||||||||| CREATE METHODS ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||*/
    public void createChar(int usrId, int nd_pos, String usrName, int a_str, int a_spd, int a_dex, int a_end, int a_sta, int loop_count)
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
        values.put("loop_cnt", loop_count);
        String checkTime = new SimpleDateFormat(Utils.ISO_DATE_TIME_FORMAT).format(new Date());
        values.put("last_check_time", checkTime);

        try{
            db.insertOrThrow("fr_char", null, values);
            db.close();
            Log.d("SCS", "Successfully Created Character");
        }
        catch(SQLiteException e)
        {
            db.close();
            Log.d("ERR", "Could Not Create Character");
            Log.d("SYSMSG", e.getMessage());
        }
    }

    public int createNode(int usr_id, int map_id, int nd_id, int nd_cmp, int nd_x_pos, int nd_y_pos, int adj_nd_x_pos, int adj_nd_y_pos, int challengeID, int isBoss)
    {
        int ret = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("usr_id", usr_id);
        values.put("map_id", map_id);
        values.put("nd_id", nd_id);
        values.put("nd_cmp", nd_cmp);
        values.put("nd_x_pos", nd_x_pos);
        values.put("nd_y_pos", nd_y_pos);
        values.put("adj_nd_x_pos",adj_nd_x_pos);
        values.put("adj_nd_y_pos",adj_nd_y_pos);
        values.put("act_id", challengeID);
        values.put("boss_flg", isBoss);

        try{
            db.insertOrThrow("fr_map", null, values);
            db.close();
            ret = 0;
            Log.d("SCS", "Node Created");
        }
        catch(SQLiteException e)
        {
            db.close();
            Log.d("ERR", "Error inserting into db");
            Log.d("SYSMSG", e.getMessage());
        }

        return ret;
    }


    //********** CREATE METHOD FOR USER TABLE ************//
    public void createUser(String userName, int userWeight, int userHeight, String userDate)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        //values.put("usr_id", usrId);
        values.put("usr_nam", userName);
        values.put("usr_weight", userWeight);
        values.put("usr_height", userHeight);
        values.put("a_date", userDate);

        try{
            db.insertOrThrow("fr_user", null, values);
            db.close();
            Log.d("SCS", "Successfully Created Character");
        }
        catch(SQLiteException e)
        {
            db.close();
            Log.d("ERR", "Could Not Create Character");
            Log.d("SYSMSG", e.getMessage());
        }
    }

    public void updateNode(int id, int map_id, int nd_id, int nd_cmp, int nd_x_pos, int nd_y_pos, int adj_nd_x_pos, int adj_nd_y_pos, int challengeID, int isBoss)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("usr_id", id);
        values.put("map_id", map_id);
        values.put("nd_id", nd_id);
        values.put("nd_cmp", nd_cmp);
        values.put("nd_x_pos", nd_x_pos);
        values.put("nd_y_pos", nd_y_pos);
        values.put("adj_nd_x_pos", adj_nd_x_pos);
        values.put("adj_nd_y_pos", adj_nd_y_pos);
        values.put("act_id", challengeID);
        values.put("boss_flg", isBoss);


     if(db.update("fr_map", values, "usr_id = " + id + " and map_id = " + map_id + " and nd_id = " + nd_id + "", null) > 0)
        {
            db.close();
            Log.d("SCS", "Successfully Updated Node");
        }
        else
        {
            Log.d("ERR", "Error Updating Node - now trying to insert record");
            //if we cannon update the rows, they might not exist
            if(createNode(id,map_id,nd_id,nd_cmp,nd_x_pos,nd_y_pos,adj_nd_x_pos,adj_nd_y_pos,challengeID, isBoss) != -1)
            {
                Log.d("SCS", "Successfully Created Node");
            }
            else
            {
                Log.d("ERR", "Error Creating Node");
            }

        }

    }

    public void updatePath(int map_id, int nodeA, int nodeB, int status, String startTime, String endTime)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("map_id", map_id);
        values.put("nd_a", nodeA);
        values.put("nd_b", nodeB);
        values.put("trvl_sts", status);
        values.put("trvl_s_time", startTime);
        values.put("trvl_e_time", endTime);



        if(db.update("fr_path", values, "map_id = " + map_id + " and (nd_a = " +nodeA + " or nd_b = " + nodeA +") and (nd_a = " +nodeB + " or nd_b = " + nodeB +")", null) > 0)
        {
            Log.d("SCS", "Successfully Updated Path between (" + nodeA + "," + nodeB + ")");
            db.close();
        }
        else
        {
            db.close();
            Log.d("ERR", "Error Updating Path - now tring to insert new record");
            if(createPath(map_id,nodeA,nodeB,status,startTime,endTime) != -1)
            {
                Log.d("SCS", "Successfully Created Path");
            }
            else
            {
                Log.d("ERR", "Error Creating Path");
            }

        }

    }

    public int createPath(int map_id, int nodeA, int nodeB, int status, String startTime, String endTime)
    {
        int ret = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("map_id", map_id);
        values.put("nd_a", nodeA);
        values.put("nd_b", nodeB);
        values.put("trvl_sts", status);
        values.put("trvl_s_time", startTime);
        values.put("trvl_e_time", endTime);

        try{
            db.insertOrThrow("fr_path", null, values);
            db.close();
            Log.d("SCS", "Path Created between ("+ nodeB + "," + nodeB + ")");
            ret = 0;
        }
        catch(SQLiteException e)
        {
            db.close();
            Log.d("ERR", "Error inserting path into db");
            Log.d("SYSMSG", e.getMessage());
        }

        return ret;
    }

    public int deletePath(int map_id, int nodeA, int nodeB)
    {
        int ret = -1;
        SQLiteDatabase db = this.getReadableDatabase();

        if((db.delete("fr_path", "map_id = " + map_id + " and nd_a = " + nodeA + " and nd_b = "+ nodeB + "", null) > 0)  || (db.delete("fr_path", "map_id = " + map_id + " and nd_a = " + nodeB + " and nd_b = "+ nodeA + "", null) > 0))
        {
            db.close();
            Log.d("SCS", "Path deleted between ("+ nodeA + "," + nodeB + ")");
            ret = 0;
        }
        else
        {
            db.close();
            Log.d("ERR", "Path deletion between ("+ nodeB + "," + nodeA + ") failed");
        }


        return ret;
    }
}