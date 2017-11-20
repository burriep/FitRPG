package edu.uwm.cs.fitrpg.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.Utils;

public class FitnessTrackingTimeFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private TextView elapsedTimeText;

    public FitnessTrackingTimeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fitness_tracking_time, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Utils.SP_KEY_DURATION_SECONDS_UPDATES)) {
            String display = Utils.formatDuration(sharedPreferences.getLong(Utils.SP_KEY_DURATION_SECONDS_UPDATES, 0));
            elapsedTimeText.setText(display);
        }
    }
}
