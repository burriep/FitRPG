package edu.uwm.cs.fitrpg.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FitnessActivityType {
    private int id;
    private String name;
    private String description;
    private boolean hasTime;
    private boolean hasDistance;
    private boolean hasReps;
    private int aerobicImpact;
    private int flexibilityImpact;
    private int muscleStrengthImpact;
    private int boneStrengthImpact;

    public FitnessActivityType() {
    }

    public FitnessActivityType(int id, String name, String description, boolean hasTime, boolean hasDistance, boolean hasReps, int aerobicImpact, int flexibilityImpact, int muscleStrengthImpact, int boneStrengthImpact) {
        this.id = id;
        this.name = name;
        this.hasTime = hasTime;
        this.hasDistance = hasDistance;
        this.hasReps = hasReps;
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

    public boolean tracksTime() {
        return hasTime;
    }

    public boolean tracksDistance() {
        return hasDistance;
    }

    public boolean tracksReps() {
        return hasReps;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        List<FitnessActivityType> acts = new ArrayList<>(10);
        acts.add(new FitnessActivityType(0, "running", "", true, true, false, 2, 0, 0, 1));
        acts.add(new FitnessActivityType(0, "walking", "", true, true, false, 2, 0, 0, 1));
        acts.add(new FitnessActivityType(0, "swimming", "", true, true, false, 1, 0, 0, 0));
        acts.add(new FitnessActivityType(0, "bicycling", "", true, true, false, 1, 0, 0, 0));
        acts.add(new FitnessActivityType(0, "dancing", "", true, false, false, 2, 0, 0, 1));
        acts.add(new FitnessActivityType(0, "tennis", "", true, false, false, 1, 0, 0, 0));
        acts.add(new FitnessActivityType(0, "racquetball", "", true, false, false, 1, 0, 0, 0));
        acts.add(new FitnessActivityType(0, "basketball", "", true, false, false, 2, 0, 0, 1));
        acts.add(new FitnessActivityType(0, "soccer", "", true, false, false, 2, 0, 0, 1));
        acts.add(new FitnessActivityType(0, "jumping jacks", "", true, false, true, 2, 0, 0, 1));
        acts.add(new FitnessActivityType(0, "stretches", "", true, false, false, 0, 1, 0, 0));
        acts.add(new FitnessActivityType(0, "yoga", "", true, false, false, 0, 1, 0, 0));
        acts.add(new FitnessActivityType(0, "pilates", "", true, false, false, 0, 1, 0, 0));
        acts.add(new FitnessActivityType(0, "jumping rope", "", true, false, false, 1, 0, 0, 1));
        acts.add(new FitnessActivityType(0, "pushups", "", true, false, false, 0, 0, 1, 0));
        acts.add(new FitnessActivityType(0, "situps", "", true, false, false, 0, 0, 1, 0));
        acts.add(new FitnessActivityType(0, "lifting weights", "", true, false, false, 0, 0, 1, 1));
        acts.add(new FitnessActivityType(0, "climbing stairs", "", true, false, false, 1, 0, 1, 0));
        for (FitnessActivityType type : acts) {
            if (!type.create(db)) {
                break;
            }
        }
    }

    private boolean create(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("act_nam", name);
        values.put("act_dsc", description);
        values.put("act_mode", modeBooleanToInt(hasTime, hasDistance, hasReps));
        values.put("act_aero", aerobicImpact);
        values.put("act_flex", flexibilityImpact);
        values.put("act_musc", muscleStrengthImpact);
        values.put("act_bone", boneStrengthImpact);
        id = (int) db.insert("fr_act", null, values);
        return id > 0;
    }

    public static FitnessActivityType get(SQLiteDatabase db, int id) {
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
        FitnessActivityType pat = null;
        while (cursor.moveToNext()) {
            pat = new FitnessActivityType();
            pat.id = cursor.getInt(cursor.getColumnIndexOrThrow("act_id"));
            pat.name = cursor.getString(cursor.getColumnIndexOrThrow("act_nam"));
            pat.description = cursor.getString(cursor.getColumnIndexOrThrow("act_dsc"));
            setModeBooleansFromInt(pat, cursor.getInt(cursor.getColumnIndexOrThrow("act_mode")));
            pat.aerobicImpact = cursor.getInt(cursor.getColumnIndexOrThrow("act_aero"));
            pat.flexibilityImpact = cursor.getInt(cursor.getColumnIndexOrThrow("act_flex"));
            pat.muscleStrengthImpact = cursor.getInt(cursor.getColumnIndexOrThrow("act_musc"));
            pat.boneStrengthImpact = cursor.getInt(cursor.getColumnIndexOrThrow("act_bone"));
        }
        cursor.close();
        return pat;
    }

    private static int modeBooleanToInt(boolean hasTime, boolean hasDistance, boolean hasReps) {
        int mode = 0;
        mode += hasTime ? 1 : 0;
        mode += hasDistance ? 2 : 0;
        mode += hasReps ? 4 : 0;
        return mode;
    }

    private static void setModeBooleansFromInt(FitnessActivityType type, int mode) {
        type.hasTime = (mode & 1) > 0;
        type.hasDistance = (mode & 2) > 0;
        type.hasReps = (mode & 4) > 0;
    }

    public static List<FitnessActivityType> getAll(SQLiteDatabase db) {
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
        List<FitnessActivityType> types = new LinkedList<>();
        while (cursor.moveToNext()) {
            FitnessActivityType pat = new FitnessActivityType();
            pat.id = cursor.getInt(cursor.getColumnIndexOrThrow("act_id"));
            pat.name = cursor.getString(cursor.getColumnIndexOrThrow("act_nam"));
            pat.description = cursor.getString(cursor.getColumnIndexOrThrow("act_dsc"));
            setModeBooleansFromInt(pat, cursor.getInt(cursor.getColumnIndexOrThrow("act_mode")));
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
