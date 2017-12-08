package edu.uwm.cs.fitrpg.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FitnessActivityType {
    private int _id;
    private String name;
    private String description;
    private boolean hasTime;
    private boolean hasDistance;
    private boolean hasReps;
    private int aerobicImpact;
    private int flexibilityImpact;
    private int muscleStrengthImpact;
    private int boneStrengthImpact;
    private int impactInterval;
    private int impactIntervalUnit;
    private int impactIntervalIncrement;

    public FitnessActivityType() {
    }

    public FitnessActivityType(int id, String name, String description, boolean hasTime, boolean hasDistance, boolean hasReps, int aerobicImpact, int flexibilityImpact, int muscleStrengthImpact, int boneStrengthImpact, int impactInterval, int impactIntervalUnit, int impactIntervalIncrement) {
        this._id = id;
        this.name = name;
        this.hasTime = hasTime;
        this.hasDistance = hasDistance;
        this.hasReps = hasReps;
        this.description = description;
        this.aerobicImpact = aerobicImpact;
        this.flexibilityImpact = flexibilityImpact;
        this.muscleStrengthImpact = muscleStrengthImpact;
        this.boneStrengthImpact = boneStrengthImpact;
        this.impactInterval = (FitnessActivityUnit.TIME == impactIntervalUnit) ? minToMS(impactInterval) : impactInterval;
        this.impactIntervalUnit = impactIntervalUnit;
        this.impactIntervalIncrement = impactIntervalIncrement;
    }

    public FitnessActivityType(Cursor cursor) {
        _id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        name = cursor.getString(cursor.getColumnIndexOrThrow("act_nam"));
        description = cursor.getString(cursor.getColumnIndexOrThrow("act_dsc"));
        setModeBooleansFromInt(this, cursor.getInt(cursor.getColumnIndexOrThrow("act_mode")));
        aerobicImpact = cursor.getInt(cursor.getColumnIndexOrThrow("act_aero"));
        flexibilityImpact = cursor.getInt(cursor.getColumnIndexOrThrow("act_flex"));
        muscleStrengthImpact = cursor.getInt(cursor.getColumnIndexOrThrow("act_musc"));
        boneStrengthImpact = cursor.getInt(cursor.getColumnIndexOrThrow("act_bone"));
        impactInterval = cursor.getInt(cursor.getColumnIndexOrThrow("act_int"));
        impactIntervalUnit = cursor.getInt(cursor.getColumnIndexOrThrow("act_intUnit"));
        impactIntervalIncrement = cursor.getInt(cursor.getColumnIndexOrThrow("act_intInc"));
    }

    public static void init(SQLiteDatabase db) {
        List<FitnessActivityType> acts = new ArrayList<>(10);
        acts.add(new FitnessActivityType(0, "Running", "", true, true, false, 2, 0, 0, 1, 1000, FitnessActivityUnit.DISTANCE, 100));
        acts.add(new FitnessActivityType(0, "Walking", "", true, true, false, 2, 0, 0, 1, 1000, FitnessActivityUnit.DISTANCE, 100));
        acts.add(new FitnessActivityType(0, "Swimming", "", true, true, false, 1, 0, 0, 0, 10, FitnessActivityUnit.TIME, 5));
        acts.add(new FitnessActivityType(0, "Bicycling", "", true, true, false, 1, 0, 0, 0, 1000, FitnessActivityUnit.DISTANCE, 100));
        acts.add(new FitnessActivityType(0, "Dancing", "", true, false, false, 2, 0, 0, 1, 20, FitnessActivityUnit.TIME, 5));
        acts.add(new FitnessActivityType(0, "Tennis", "", true, false, false, 1, 0, 0, 0, 10, FitnessActivityUnit.TIME, 5));
        acts.add(new FitnessActivityType(0, "Racquetball", "", true, false, false, 1, 0, 0, 0, 15, FitnessActivityUnit.TIME, 5));
        acts.add(new FitnessActivityType(0, "Basketball", "", true, false, false, 2, 0, 0, 1, 5, FitnessActivityUnit.TIME, 5));
        acts.add(new FitnessActivityType(0, "Soccer", "", true, false, false, 2, 0, 0, 1, 30, FitnessActivityUnit.TIME, 5));
        acts.add(new FitnessActivityType(0, "Jumping jacks", "", true, false, true, 2, 0, 0, 1, 20, FitnessActivityUnit.REPS, 5));
        acts.add(new FitnessActivityType(0, "Stretches", "", true, false, false, 0, 1, 0, 0, 5, FitnessActivityUnit.TIME, 1));
        acts.add(new FitnessActivityType(0, "Yoga", "", true, false, false, 0, 1, 0, 0, 20, FitnessActivityUnit.TIME, 5));
        acts.add(new FitnessActivityType(0, "Pilates", "", true, false, false, 0, 1, 0, 0, 20, FitnessActivityUnit.TIME, 5));
        acts.add(new FitnessActivityType(0, "Jumping rope", "", true, false, false, 1, 0, 0, 1, 10, FitnessActivityUnit.TIME, 1));
        acts.add(new FitnessActivityType(0, "Pushups", "", true, false, true, 0, 0, 1, 0, 5, FitnessActivityUnit.REPS, 1));
        acts.add(new FitnessActivityType(0, "Situps", "", true, false, true, 0, 0, 1, 0, 5, FitnessActivityUnit.REPS, 2));
        acts.add(new FitnessActivityType(0, "Lifting weights", "", true, false, true, 0, 0, 1, 1, 10, FitnessActivityUnit.TIME, 2));
        acts.add(new FitnessActivityType(0, "Climbing stairs", "", true, false, true, 1, 0, 1, 0, 5, FitnessActivityUnit.TIME, 1));
        for (FitnessActivityType type : acts) {
            if (!type.create(db)) {
                break;
            }
        }
    }

    public static FitnessActivityType get(SQLiteDatabase db, int id) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                "_id",
                "act_nam",
                "act_dsc",
                "act_mode",
                "act_aero",
                "act_flex",
                "act_musc",
                "act_bone",
                "act_int",
                "act_intUnit",
                "act_intInc"
        };
        String selection = "_id = ?";
        String[] selectionArgs = {Integer.toString(id)};

        // How you want the results sorted in the resulting Cursor
        String sortOrder = "_id DESC";
        Cursor cursor = db.query("fr_act", projection, selection, selectionArgs, null, null, sortOrder);
        FitnessActivityType pat = null;
        while (cursor.moveToNext()) {
            pat = new FitnessActivityType(cursor);
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
                "_id",
                "act_nam",
                "act_dsc",
                "act_mode",
                "act_aero",
                "act_flex",
                "act_musc",
                "act_bone",
                "act_int",
                "act_intUnit",
                "act_intInc"
        };
        // How you want the results sorted in the resulting Cursor
        String sortOrder = "_id ASC";
        Cursor cursor = db.query("fr_act", projection, "", null, null, null, sortOrder);
        List<FitnessActivityType> types = new LinkedList<>();
        while (cursor.moveToNext()) {
            types.add(new FitnessActivityType(cursor));
        }
        cursor.close();
        return types;
    }

    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FitnessActivityType) {
            return ((FitnessActivityType) obj)._id == _id;
        }
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public int getImpactInterval() {
        return impactInterval;
    }

    public void setImpactInterval(int impactInterval) {
        this.impactInterval = impactInterval;
    }

    public int getImpactIntervalUnit() {
        return impactIntervalUnit;
    }

    public void setImpactIntervalUnit(int impactIntervalUnit) {
        this.impactIntervalUnit = impactIntervalUnit;
    }

    public int getImpactIntervalIncrement() {
        return impactIntervalIncrement;
    }

    public void setImpactIntervalIncrement(int impactIntervalIncrement) {
        this.impactIntervalIncrement = impactIntervalIncrement;
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

    private int minToMS(int minutes) {
        return minutes * 60 * 1000;
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
        values.put("act_int", impactInterval);
        values.put("act_intUnit", impactIntervalUnit);
        values.put("act_intInc", impactIntervalIncrement);
        _id = (int) db.insert("fr_act", null, values);
        return _id > 0;
    }
}
