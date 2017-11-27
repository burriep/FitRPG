package edu.uwm.cs.fitrpg.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.activity.FitnessActivityTracking;
import edu.uwm.cs.fitrpg.activity.FitnessOverview;
import edu.uwm.cs.fitrpg.model.FitnessActivity;

public class FitnessFragment extends Fragment implements FitnessActivityHistoryFragment.OnListFragmentInteractionListener {
    private static int position;

    public FitnessFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_fitness, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
        View fragmentView = getView();
        //Fragment fragment = null;

//        TextView history, recordData, trackData, goToMap;
//        history = (TextView) fragmentView.findViewById(R.id.tv_fitness_history);
//        recordData = (TextView) fragmentView.findViewById(R.id.tv_fitness_record_data);
//        trackData = (TextView) fragmentView.findViewById(R.id.tv_fitness_track_data);
//
//        history.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Calendar calendar = Calendar.getInstance();
//                Date now = calendar.getTime();
//                calendar.add(Calendar.DATE, -1);
//                Date yesterday = calendar.getTime();
//                Fragment fragment = FitnessActivityHistoryFragment.newInstance(yesterday, now);
//                FragmentActivity fragmentActivity = getActivity();
//                fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fitness_frag_data, fragment).commit();
//            }
//        });
//
//        recordData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FitnessEntryFragment fragmentEntry = new FitnessEntryFragment();
//                FragmentActivity fragmentActivity = getActivity();
//                fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fitness_frag_data, fragmentEntry).commit();
//            }
//        });
//
//        trackData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            Intent intent = new Intent(getActivity(), FitnessActivityTracking.class);
//            startActivity(intent);
//            }
//        });
    }


    @Override
    public void onListFragmentInteraction(FitnessActivity item) {

    }

}
