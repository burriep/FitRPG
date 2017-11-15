package edu.uwm.cs.fitrpg.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PhysicalActivityType {
    private int id;
    private String name;
    private String description;
    private PhysicalActivityTrackingMode mode;
    private int aerobicImpact;
    private int flexibilityImpact;
    private int muscleStrengthImpact;
    private int boneStrengthImpact;

    public PhysicalActivityType() {
    }

    public PhysicalActivityType(int id, String name, String description, PhysicalActivityTrackingMode mode, int aerobicImpact, int flexibilityImpact, int muscleStrengthImpact, int boneStrengthImpact) {
        this.id = id;
        this.name = name;
        this.mode = mode;
        this.description = description;
        this.aerobicImpact = aerobicImpact;
        this.flexibilityImpact = flexibilityImpact;
        this.muscleStrengthImpact = muscleStrengthImpact;
        this.boneStrengthImpact = boneStrengthImpact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PhysicalActivityTrackingMode getMode() {
        return mode;
    }

    public void setMode(PhysicalActivityTrackingMode mode) {
        this.mode = mode;
    }

    public int getAerobicImpact() {
        return aerobicImpact;
    }

    public void setAerobicImpact(int aerobicImpact) {
        this.aerobicImpact = aerobicImpact;
    }

    public int getFlexibilityImpact() {
        return flexibilityImpact;
    }

    public void setFlexibilityImpact(int flexibilityImpact) {
        this.flexibilityImpact = flexibilityImpact;
    }

    public int getMuscleStrengthImpact() {
        return muscleStrengthImpact;
    }

    public void setMuscleStrengthImpact(int muscleStrengthImpact) {
        this.muscleStrengthImpact = muscleStrengthImpact;
    }

    public int getBoneStrengthImpact() {
        return boneStrengthImpact;
    }

    public void setBoneStrengthImpact(int boneStrengthImpact) {
        this.boneStrengthImpact = boneStrengthImpact;
    }

    public static void init(SQLiteDatabase db) {
        List<PhysicalActivityType> acts = new ArrayList<>(10);
        acts.add(new PhysicalActivityType(0, "running", "", PhysicalActivityTrackingMode.TIME_DISTANCE, 2, 0, 0, 1));
        acts.add(new PhysicalActivityType(0, "walking", "", PhysicalActivityTrackingMode.TIME_DISTANCE, 2, 0, 0, 1));
        acts.add(new PhysicalActivityType(0, "swimming", "", PhysicalActivityTrackingMode.TIME_DISTANCE, 1, 0, 0, 0));
        acts.add(new PhysicalActivityType(0, "bicycling", "", PhysicalActivityTrackingMode.TIME_DISTANCE, 1, 0, 0, 0));
        acts.add(new PhysicalActivityType(0, "dancing", "", PhysicalActivityTrackingMode.TIME, 2, 0, 0, 1));
        acts.add(new PhysicalActivityType(0, "tennis", "", PhysicalActivityTrackingMode.TIME, 1, 0, 0, 0));
        acts.add(new PhysicalActivityType(0, "racquetball", "", PhysicalActivityTrackingMode.TIME, 1, 0, 0, 0));
        acts.add(new PhysicalActivityType(0, "basketball", "", PhysicalActivityTrackingMode.TIME, 2, 0, 0, 1));
        acts.add(new PhysicalActivityType(0, "soccer", "", PhysicalActivityTrackingMode.TIME, 2, 0, 0, 1));
        acts.add(new PhysicalActivityType(0, "jumping jacks", "", PhysicalActivityTrackingMode.TIME_REPS, 2, 0, 0, 1));
        acts.add(new PhysicalActivityType(0, "stretches", "", PhysicalActivityTrackingMode.TIME, 0, 1, 0, 0));
        acts.add(new PhysicalActivityType(0, "yoga", "", PhysicalActivityTrackingMode.TIME, 0, 1, 0, 0));
        acts.add(new PhysicalActivityType(0, "pilates", "", PhysicalActivityTrackingMode.TIME, 0, 1, 0, 0));
        acts.add(new PhysicalActivityType(0, "jumping rope", "", PhysicalActivityTrackingMode.TIME, 1, 0, 0, 1));
        acts.add(new PhysicalActivityType(0, "pushups", "", PhysicalActivityTrackingMode.TIME, 0, 0, 1, 0));
        acts.add(new PhysicalActivityType(0, "situps", "", PhysicalActivityTrackingMode.TIME, 0, 0, 1, 0));
        acts.add(new PhysicalActivityType(0, "lifting weights", "", PhysicalActivityTrackingMode.TIME, 0, 0, 1, 1));
        acts.add(new PhysicalActivityType(0, "climbing stairs", "", PhysicalActivityTrackingMode.TIME, 1, 0, 1, 0));
        for (PhysicalActivityType type : acts) {
            if (!type.create(db)) {
                break;
            }
        }
    }

    private boolean create(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("act_nam", name);
        values.put("act_dsc", description);
        values.put("act_mode", mode.getValue());
        values.put("act_aero", aerobicImpact);
        values.put("act_flex", flexibilityImpact);
        values.put("act_musc", muscleStrengthImpact);
        values.put("act_bone", boneStrengthImpact);
        id = (int) db.insert("fr_act", null, values);
        return id > 0;
    }

    public static PhysicalActivityType get(SQLiteDatabase db, int id) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                "act_id",
                "act_nam",
                "act_dsc",
                "act_mode",
                "act_aero",
                "act_flex",
                "act_musc",
                "act_bone"
        };
        String selection = "act_id = ?";
        String[] selectionArgs = {Integer.toString(id)};

        // How you want the results sorted in the resulting Cursor
        String sortOrder = "act_id DESC";
        Cursor cursor = db.query("fr_act", projection, selection, selectionArgs, null, null, sortOrder);
        PhysicalActivityType pat = null;
        while (cursor.moveToNext()) {
            pat = new PhysicalActivityType();
            pat.id = cursor.getInt(cursor.getColumnIndexOrThrow("act_id"));
            pat.name = cursor.getString(cursor.getColumnIndexOrThrow("act_nam"));
            pat.description = cursor.getString(cursor.getColumnIndexOrThrow("act_dsc"));
            pat.mode = PhysicalActivityTrackingMode.values()[cursor.getInt(cursor.getColumnIndexOrThrow("act_mode"))];
            pat.aerobicImpact = cursor.getInt(cursor.getColumnIndexOrThrow("act_aero"));
            pat.flexibilityImpact = cursor.getInt(cursor.getColumnIndexOrThrow("act_flex"));
            pat.muscleStrengthImpact = cursor.getInt(cursor.getColumnIndexOrThrow("act_musc"));
            pat.boneStrengthImpact = cursor.getInt(cursor.getColumnIndexOrThrow("act_bone"));
        }
        cursor.close();
        return pat;
    }

    public static List<PhysicalActivityType> getAll(SQLiteDatabase db) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                "act_id",
                "act_nam",
                "act_dsc",
                "act_mode",
                "act_aero",
                "act_flex",
                "act_musc",
                "act_bone"
        };
        // How you want the results sorted in the resulting Cursor
        String sortOrder = "act_id ASC";
        Cursor cursor = db.query("fr_act", projection, "", null, null, null, sortOrder);
        List<PhysicalActivityType> types = new LinkedList<>();
        while (cursor.moveToNext()) {
            PhysicalActivityType pat = new PhysicalActivityType();
            pat.id = cursor.getInt(cursor.getColumnIndexOrThrow("act_id"));
            pat.name = cursor.getString(cursor.getColumnIndexOrThrow("act_nam"));
            pat.description = cursor.getString(cursor.getColumnIndexOrThrow("act_dsc"));
            pat.mode = PhysicalActivityTrackingMode.values()[cursor.getInt(cursor.getColumnIndexOrThrow("act_mode"))];
            pat.aerobicImpact = cursor.getInt(cursor.getColumnIndexOrThrow("act_aero"));
            pat.flexibilityImpact = cursor.getInt(cursor.getColumnIndexOrThrow("act_flex"));
            pat.muscleStrengthImpact = cursor.getInt(cursor.getColumnIndexOrThrow("act_musc"));
            pat.boneStrengthImpact = cursor.getInt(cursor.getColumnIndexOrThrow("act_bone"));
            types.add(pat);
        }
        cursor.close();
        return types;
    }
}
