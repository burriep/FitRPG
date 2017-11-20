package edu.uwm.cs.fitrpg.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uwm.cs.fitrpg.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FitnessTrackingTimeFragment extends Fragment {


    public FitnessTrackingTimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fitness_tracking_time, container, false);
    }

}
