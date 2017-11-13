package edu.uwm.cs.fitrpg.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
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

    public PhysicalActivityType get(int id) {
/*
create table fr_act (
act_id INTEGER(2) PRIMARY KEY,
act_nam VARCHAR(13),
act_dsc VARCHAR(50),
act_mode INTEGER(1),
act_aero INTEGER(2),
act_flex INTEGER(2),
act_musc INTEGER(2),
act_bone INTEGER(2)
)
*/
        return null;
    }

    public PhysicalActivityType getAll() {
        return null;
    }
}
