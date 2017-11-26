package edu.uwm.cs.fitrpg.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.model.FitnessActivity;
import edu.uwm.cs.fitrpg.util.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FitnessEntryRepsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FitnessEntryRepsFragment extends Fragment {
    private EditText setsField, repsField;

    public FitnessEntryRepsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FitnessEntryRepsFragment.
     */
    public static FitnessEntryRepsFragment newInstance(String param1, String param2) {
        FitnessEntryRepsFragment fragment = new FitnessEntryRepsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fitness_entry_reps, container, false);
        setsField = view.findViewById(R.id.activity_entry_sets);
        repsField = view.findViewById(R.id.activity_entry_reps);
        return view;
    }

    public void getData(FitnessActivity activity) {
        activity.setSets(Utils.getIntegerField(setsField, 0));
        activity.setRepetitions(Utils.getIntegerField(repsField, 0));
    }
}
