package edu.uwm.cs.fitrpg.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.model.FitnessActivity;
import edu.uwm.cs.fitrpg.model.FitnessActivityType;
import edu.uwm.cs.fitrpg.util.FitnessActivityTypeAdapter;

public class FitnessEntryFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner typeSpinner;
    private FitnessActivityType activityType;
    private FitnessEntryTimeFragment timeFragment;
    private FitnessEntryDistanceFragment distanceFragment;
    private FitnessEntryRepsFragment repsFragment;
    private List<FitnessActivityType> fitnessActivityTypes;
    private Button saveButton, clearButton;

    public FitnessEntryFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void updateFragments(boolean forceReplace) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        if (activityType != null && activityType.tracksTime()) {
            if (timeFragment == null || forceReplace) {
                timeFragment = new FitnessEntryTimeFragment();
                ft.replace(R.id.fitness_entry_time_layout, timeFragment);
            }
        } else if (timeFragment != null) {
            ft.remove(timeFragment);
            timeFragment = null;
        }
        if (activityType != null && activityType.tracksDistance()) {
            if (distanceFragment == null || forceReplace) {
                distanceFragment = new FitnessEntryDistanceFragment();
                ft.replace(R.id.fitness_entry_distance_layout, distanceFragment);
            }
        } else if (distanceFragment != null) {
            ft.remove(distanceFragment);
            distanceFragment = null;
        }
        if (activityType != null && activityType.tracksReps()) {
            if (repsFragment == null || forceReplace) {
                repsFragment = new FitnessEntryRepsFragment();
                ft.replace(R.id.fitness_entry_reps_layout, repsFragment);
            }
        } else if (repsFragment != null) {
            ft.remove(repsFragment);
            repsFragment = null;
        }
        ft.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fitness_entry, container, false);
        typeSpinner = view.findViewById(R.id.fitness_entry_type_spinner);
        final Context context = getContext();

        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
                final List<FitnessActivityType> list = FitnessActivityType.getAll(db);
                typeSpinner.post(new Runnable() {
                    @Override
                    public void run() {
                        fitnessActivityTypes = list;
                        typeSpinner.setAdapter(new FitnessActivityTypeAdapter(FitnessEntryFragment.this, list));
                        if (!fitnessActivityTypes.isEmpty()) {
                            activityType = fitnessActivityTypes.get(0);
                        }
                        updateFragments(false);
                    }
                });
            }
        }).run();
        typeSpinner.setOnItemSelectedListener(this);

        saveButton = view.findViewById(R.id.btn_fitness_entry_save);
        clearButton = view.findViewById(R.id.btn_fitness_entry_clear);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveActivity(v);
            }
        });
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearForm(v);
            }
        });

        return view;
    }

    private void saveActivity(final View view) {
        // get the data
        final FitnessActivity activity = new FitnessActivity(1);
        activity.setType(activityType);
        if (timeFragment != null)
            timeFragment.getData(activity);
        if (distanceFragment != null)
            distanceFragment.getData(activity);
        if (repsFragment != null)
            repsFragment.getData(activity);
        // save the data
        final Context context = getContext();
        new Thread(new Runnable() {
            @Override
            public void run() {
                activity.create(new DatabaseHelper(context).getWritableDatabase());
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Activity Saved", Toast.LENGTH_SHORT).show();
                        clearForm(view);
                    }
                });
            }
        }).run();
    }

    private void clearForm(View view) {
        updateFragments(true);
    }

    public void onStart() {
        super.onStart();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position >= 0 && position < fitnessActivityTypes.size()) {
            activityType = fitnessActivityTypes.get(position);
            updateFragments(false);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // nothing to do
    }
}
