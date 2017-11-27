package edu.uwm.cs.fitrpg.fragments;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.model.FitnessActivity;


public class FitnessHistoryDataFragment extends Fragment{

    TextView tvStartTime,tvEndTime, tvDuration, tvDistance, tvTopSpeed, tvSets, tvReps;
    Button btnOK;
    FitnessActivity fitnessActivity;
    SQLiteDatabase db;
    public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public FitnessHistoryDataFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = new DatabaseHelper(getActivity()).getReadableDatabase();
        Bundle myBundle = this.getArguments();
        fitnessActivity = FitnessActivity.get(db ,myBundle.getInt("FitnessActivityValue"));
        return inflater.inflate(R.layout.fragment_fitness_history_data, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View fragmentView = getView();

        tvStartTime = fragmentView.findViewById(R.id.frag_history_start_time);
        tvEndTime = fragmentView.findViewById(R.id.frag_history_end_time);
        tvDuration = fragmentView.findViewById(R.id.frag_history_duration);
        tvDistance = fragmentView.findViewById(R.id.frag_history_distance);
        tvTopSpeed = fragmentView.findViewById(R.id.frag_history_top_speed);
        tvSets = fragmentView.findViewById(R.id.frag_history_sets);
        tvReps = fragmentView.findViewById(R.id.frag_history_reps);

        btnOK = fragmentView.findViewById(R.id.btn_frag_history_ok);

        tvStartTime.setText((new SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.US)).format(fitnessActivity.getStartDate()));
        tvEndTime.setText((new SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.US)).format(fitnessActivity.getStopDate()));
        tvDuration.setText(Long.toString(fitnessActivity.getDuration()));
        tvDistance.setText(Double.toString(fitnessActivity.getDistance()));
        tvTopSpeed.setText(Double.toString(fitnessActivity.getTopSpeed()));
        tvSets.setText(Integer.toString(fitnessActivity.getSets()));
        tvReps.setText(Integer.toString(fitnessActivity.getRepetitions()));

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                Date now = calendar.getTime();
                calendar.add(Calendar.DATE, -1);
                Date yesterday = calendar.getTime();

                FitnessActivityHistoryFragment fragment = FitnessActivityHistoryFragment.newInstance(yesterday, now);
                FragmentActivity fragmentActivity = getActivity();
                fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fitness_frag_data, fragment).commit();
                onStop();
            }
        });

    }


}
