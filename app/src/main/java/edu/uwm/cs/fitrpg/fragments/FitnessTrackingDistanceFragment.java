package edu.uwm.cs.fitrpg.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import edu.uwm.cs.fitrpg.R;

import static edu.uwm.cs.fitrpg.util.Utils.SP_KEY_AVG_SPEED_UPDATES;
import static edu.uwm.cs.fitrpg.util.Utils.SP_KEY_DISTANCE_UPDATES;
import static edu.uwm.cs.fitrpg.util.Utils.SP_KEY_TOP_SPEED_UPDATES;

public class FitnessTrackingDistanceFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private TextView distanceText, averageSpeedText, topSpeedText;

    public FitnessTrackingDistanceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fitness_tracking_distance, container, false);
        distanceText = view.findViewById(R.id.distance_text);
        averageSpeedText = view.findViewById(R.id.average_speed_text);
        topSpeedText = view.findViewById(R.id.top_speed_text);
        return view;
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
    public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
        double value = 0;
        switch (key) {
            case SP_KEY_TOP_SPEED_UPDATES:
                value = Double.longBitsToDouble(sp.getLong(key, Double.doubleToLongBits(0)));
                topSpeedText.setText(String.format(Locale.ENGLISH, "%s: %.2f%s", getString(R.string.activity_top_speed_label), value, getString(R.string.activity_speed_unit_metric)));
                break;
            case SP_KEY_AVG_SPEED_UPDATES:
                value = Double.longBitsToDouble(sp.getLong(key, Double.doubleToLongBits(0)));
                averageSpeedText.setText(String.format(Locale.ENGLISH, "%s: %.2f%s", getString(R.string.activity_average_speed_label), value, getString(R.string.activity_speed_unit_metric)));
                break;
            case SP_KEY_DISTANCE_UPDATES:
                value = Double.longBitsToDouble(sp.getLong(key, Double.doubleToLongBits(0)));
                distanceText.setText(String.format(Locale.ENGLISH, "%s: %.2f%s", getString(R.string.activity_distance_label), value, getString(R.string.activity_distance_unit_short_metric)));
                break;
        }
    }
}
