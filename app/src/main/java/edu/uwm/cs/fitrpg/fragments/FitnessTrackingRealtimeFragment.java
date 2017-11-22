package edu.uwm.cs.fitrpg.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.model.PhysicalActivityType;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FitnessTrackingRealtimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FitnessTrackingRealtimeFragment extends Fragment {
    private static final String ARG_HAS_TIME = "hasTime";
    private static final String ARG_HAS_DISTANCE = "hasDistance";
    private static final String ARG_HAS_REPS = "hasReps";

    private boolean hasTime;
    private boolean hasDistance;
    private boolean hasReps;

    public FitnessTrackingRealtimeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param physicalActivityType The physical activity type which is being tracked
     * @return A new instance of fragment FitnessTrackingRealtimeFragment.
     */
    public static FitnessTrackingRealtimeFragment newInstance(PhysicalActivityType physicalActivityType) {
        FitnessTrackingRealtimeFragment fragment = new FitnessTrackingRealtimeFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_HAS_TIME, physicalActivityType.tracksTime());
        args.putBoolean(ARG_HAS_DISTANCE, physicalActivityType.tracksDistance());
        args.putBoolean(ARG_HAS_REPS, physicalActivityType.tracksReps());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hasTime = getArguments().getBoolean(ARG_HAS_TIME);
            hasDistance = getArguments().getBoolean(ARG_HAS_DISTANCE);
            hasReps = getArguments().getBoolean(ARG_HAS_REPS);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        if (hasTime) {
            Fragment fragment = new FitnessTrackingTimeFragment();
            ft.add(R.id.fitness_tracking_realtime_time_layout, fragment, "time");
        }
        if (hasDistance) {
            Fragment fragment = new FitnessTrackingDistanceFragment();
            ft.add(R.id.fitness_tracking_realtime_distance_layout, fragment, "distance");
        }
        if (hasReps) {
            // TODO:
            // instead of doing this here, just redirect the user to enter the rep-based activity
        }
        ft.commit();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fitness_tracking_realtime, container, false);
        return view;
    }
}
