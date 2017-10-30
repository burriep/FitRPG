package edu.uwm.cs.fitrpg;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jason on 10/24/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "fitrpg.db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table fr_act (act_id INTEGER(2) PRIMARY KEY, act_dsc VARCHAR(50))");
        db.execSQL("create table fr_char (usr_id INTEGER(3) PRIMARY KEY, usr_nam VARCHAR(13), nd_pos INTEGER(2), a_str INTEGER(2), a_spd INTEGER(2), a_sta INTEGER(2), a_dex INTEGER(2), a_end INTEGER(2))");
        db.execSQL("create table fr_hst (act_num INTEGER(4) PRIMARY KEY, act_id INTEGER(2) NOT NULL, usr_id INTEGER(3) NOT NULL, s_tme TEXT, e_tme TEXT, dist FLOAT, reps INTEGER(3))");
        db.execSQL("create table fr_map (usr_id INTEGER(3) PRIMARY KEY, map_id INTEGER(2) PRIMARY KEY, nd_id INTEGER(2) PRIMARY KEY, nd_cmp INTEGER(1) DEFAULT 0)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists fr_act");
        db.execSQL("drop table if exists fr_char");
        db.execSQL("drop table if exists fr_hst");
        db.execSQL("drop table if exists fr_map");

        onCreate(db);

    }
}
