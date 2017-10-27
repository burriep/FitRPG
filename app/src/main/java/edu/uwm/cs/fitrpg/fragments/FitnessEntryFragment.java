package edu.uwm.cs.fitrpg.fragments;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import edu.uwm.cs.fitrpg.R;


public class FitnessEntryFragment extends Fragment{
    public FitnessEntryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fitness_entry, container, false);
    }

    public void onStart() {
        super.onStart();
        View fragmentView = getView();
        int position = getArguments().getInt("Position");

        RadioGroup rgActivities = (RadioGroup) fragmentView.findViewById(R.id.radioGroup_fitness_activities);
        TextView tvData = fragmentView.findViewById(R.id.tv_middle_note);
        Button btnClear = fragmentView.findViewById(R.id.btn_fitness_entry_clear);
        Button btnSave = fragmentView.findViewById(R.id.btn_fitness_entry_save);

        if (position == 2) { // Record Data
            btnClear.setText("Clear");
            btnSave.setText("Save");
            tvData.setText("Manually recorded data fragment here");

            FragmentManager fragmentManager = getFragmentManager();
            if(fragmentManager.findFragmentById(R.id.ll_entry_middle) == null) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                FitnessRecordDataFragment fragment = new FitnessRecordDataFragment();
                transaction.add(R.id.ll_entry_middle, fragment);
                transaction.commit();
            }
        }
        else if (position == 3) { // Track Data
            btnClear.setText("Cancel");
            btnSave.setText("Start");
            tvData.setText("Tracking fragment here");

            FragmentManager fragmentManager = getFragmentManager();
            if(fragmentManager.findFragmentById(R.id.ll_entry_middle) == null) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                FitnessTrackDataFragment fragment = new FitnessTrackDataFragment();
                transaction.replace(R.id.ll_entry_middle, fragment);
                transaction.commit();
            }
        }
        else {
            FragmentManager fragmentManager = getFragmentManager();
            if(fragmentManager.findFragmentById(R.id.ll_entry_middle) == null) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                FitnessTrackDataFragment fragment = new FitnessTrackDataFragment();
                transaction.replace(R.id.ll_entry_middle, fragment);
                transaction.commit();
            }
        }
    }
}
