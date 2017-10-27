package edu.uwm.cs.fitrpg.fragments;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uwm.cs.fitrpg.R;

public class FitnessHistoryFragment extends Fragment{
    public FitnessHistoryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fitness_history, container, false);
    }

    public void onStart() {
        super.onStart();}
}
