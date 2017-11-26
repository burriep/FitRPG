package edu.uwm.cs.fitrpg.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.model.FitnessActivity;
import edu.uwm.cs.fitrpg.util.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FitnessEntryDistanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FitnessEntryDistanceFragment extends Fragment {
    private EditText distanceField;

    public FitnessEntryDistanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FitnessEntryDistanceFragment.
     */
    public static FitnessEntryDistanceFragment newInstance(String param1, String param2) {
        FitnessEntryDistanceFragment fragment = new FitnessEntryDistanceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fitness_entry_distance, container, false);
        distanceField = view.findViewById(R.id.activity_entry_distance);
        return view;
    }

    public void getData(FitnessActivity activity) {
        activity.setDistance(Utils.getDoubleField(distanceField, 0));
    }
}
