package edu.uwm.cs.fitrpg.fragments;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.uwm.cs.fitrpg.FitnessActivity;
import edu.uwm.cs.fitrpg.R;


public class FitnessTrackDataFragment extends Fragment {
    private TextView elapsedTimeText;
    private TextView distanceText;
    private TextView averageSpeedText;
    private TextView topSpeedText;
    private Button fitnessContinuePauseRecord;

    private boolean needsLocation;
    private FitnessActivity currentActivity;

    public FitnessTrackDataFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fitness_track_data, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity a = getActivity();
        elapsedTimeText = (TextView) a.findViewById(R.id.elapsed_time_text);
        distanceText = (TextView) a.findViewById(R.id.distance_text);
        averageSpeedText = (TextView) a.findViewById(R.id.average_speed_text);
        topSpeedText = (TextView) a.findViewById(R.id.top_speed_text);
        fitnessContinuePauseRecord = (Button) a.findViewById(R.id.fitness_continue_pause_record);
    }

    public void onStart() {
        super.onStart();
    }
}
