package edu.uwm.cs.fitrpg.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.model.FitnessActivityType;
import edu.uwm.cs.fitrpg.util.FitnessActivityTypeAdapter;
import edu.uwm.cs.fitrpg.util.FitnessActivityTypeRecyclerViewAdapter;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FitnessActivityTypeFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private OnListFragmentInteractionListener mListener;
    private Spinner typeSpinner;
    private TextView impactStrengthText, impactEnduranceText, impactDexterityText, impactSpeedText;

    private List<FitnessActivityType> fitnessActivityTypes;
    private FitnessActivityType activityType;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FitnessActivityTypeFragment() {
    }

    @SuppressWarnings("unused")
    public static FitnessActivityTypeFragment newInstance() {
        FitnessActivityTypeFragment fragment = new FitnessActivityTypeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fitness_activity_type_list, container, false);

        typeSpinner = view.findViewById(R.id.fitness_tracking_type_spinner);
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
                        typeSpinner.setAdapter(new FitnessActivityTypeAdapter(FitnessActivityTypeFragment.this, list));
                        if (!fitnessActivityTypes.isEmpty()) {
                            activityType = fitnessActivityTypes.get(0);
                        }
                        updateImpactLabels();
                    }
                });
            }
        }).run();
        typeSpinner.setOnItemSelectedListener(this);

        impactStrengthText = view.findViewById(R.id.fitness_tracker_type_impact_strength);
        impactEnduranceText = view.findViewById(R.id.fitness_tracker_type_impact_endurance);
        impactDexterityText= view.findViewById(R.id.fitness_tracker_type_impact_dexterity);
        impactSpeedText = view.findViewById(R.id.fitness_tracker_type_impact_speed);

        return view;
    }

    private void updateImpactLabels() {
        if (activityType != null) {
            impactStrengthText.setText(String.format(Locale.US, "%+d Strength", activityType.getMuscleStrengthImpact()));
            impactEnduranceText.setText(String.format(Locale.US, "%+d Endurance", activityType.getAerobicImpact()));
            impactDexterityText.setText(String.format(Locale.US, "%+d Dexterity", activityType.getFlexibilityImpact()));
            impactSpeedText.setText(String.format(Locale.US, "%+d Speed", activityType.getBoneStrengthImpact()));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position >= 0 && position < fitnessActivityTypes.size()) {
            activityType = fitnessActivityTypes.get(position);
            updateImpactLabels();
            mListener.onListFragmentInteraction(activityType);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // nothing to do
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(FitnessActivityType item);
    }
}
