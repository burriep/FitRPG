package edu.uwm.cs.fitrpg.fragments;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

import edu.uwm.cs.fitrpg.model.FitnessActivity;
import edu.uwm.cs.fitrpg.services.LocationUpdatesBroadcastReceiver;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.Utils;


public class FitnessTrackDataFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    public final static String KEY_LOCATION_UPDATES_REQUESTED = "location-updates-requested";
    public final static String KEY_LOCATION_UPDATES_RESULT_DISTANCE = "location-update-result-distance";
    public final static String KEY_LOCATION_UPDATES_RESULT_AVG_SPEED = "location-update-result-avg-speed";
    public final static String KEY_LOCATION_UPDATES_RESULT_TOP_SPEED = "location-update-result-top-speed";
    private TextView elapsedTimeText;
    private TextView distanceText;
    private TextView averageSpeedText;
    private TextView topSpeedText;
    private Button fitnessContinuePauseRecord;
    private FusedLocationProviderClient mFusedLocationClient;
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL = 5000; // Every 5 seconds.

    /**
     * The fastest rate for active location updates. Updates will never be more frequent than this value, but they may be less frequent.
     */
    private static final long FASTEST_UPDATE_INTERVAL = 1000; // Every 1 second

    /**
     * The max time before batched results are delivered by location services. Results may be delivered sooner than this interval.
     */
    private static final long MAX_WAIT_TIME = 60000; // Every 1 minute.

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    private static final String ARG_CURRENT_FITNESS_ACTIVITY = "param1";

    private boolean needsLocation;
    private static FitnessActivity currentActivity;
    private boolean isRecording;

    public FitnessTrackDataFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param fitnessActivityId
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FitnessTrackDataFragment newInstance(int fitnessActivityId, FitnessActivity fa) {
        FitnessTrackDataFragment fragment = new FitnessTrackDataFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CURRENT_FITNESS_ACTIVITY, fitnessActivityId);
        // TODO: temporary testing thing, remove the following line:
        FitnessTrackDataFragment.currentActivity = fa;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int currentFitnessActivityId = getArguments().getInt(ARG_CURRENT_FITNESS_ACTIVITY);
            // TODO: read fitness activity from DB and load it.
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        isRecording = currentActivity != null && currentActivity.isStarted();
        fitnessContinuePauseRecord.setEnabled(isRecording);

        updateClockHandler.removeCallbacks(updateClockTask);
        updateClockHandler.postDelayed(updateClockTask, 100);
    }

    @Override
    public void onPause() {
        super.onPause();
        updateClockHandler.removeCallbacks(updateClockTask);
    }

    public void onStop() {
        super.onStop();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        elapsedTimeText = view.findViewById(R.id.elapsed_time_text);
        distanceText = view.findViewById(R.id.distance_text);
        averageSpeedText = view.findViewById(R.id.average_speed_text);
        topSpeedText = view.findViewById(R.id.top_speed_text);
        fitnessContinuePauseRecord = view.findViewById(R.id.fitness_continue_pause_record);
        fitnessContinuePauseRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordFitnessActivity(view);
            }
        });

        // location services
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        // create location request
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setMaxWaitTime(MAX_WAIT_TIME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fitness_track_data, container, false);
    }

    private Handler updateClockHandler = new Handler();
    private Runnable updateClockTask = new Runnable() {
        public void run() {
            // update clock / time
            String display = Utils.formatDuration(currentActivity.getDuration() / 1000000);
            elapsedTimeText.setText(display);
            // update location summary
//            updateLocationSummary();
            // schedule next iteration
            updateClockHandler.postDelayed(updateClockTask, 1000);
        }
    };

    private void updateLocationSummary() {
        distanceText.setText(String.format(Locale.ENGLISH, "%.2f", currentActivity.getDistance()));
        averageSpeedText.setText(String.format(Locale.ENGLISH, "%.2f", currentActivity.getAverageSpeed()));
        topSpeedText.setText(String.format(Locale.ENGLISH, "%.2f", currentActivity.getTopSpeed()));
    }

    public void recordFitnessActivity(View view) {
        if (isRecording) {
            // stop
            currentActivity.stop();
            isRecording = false;
            updateClockHandler.removeCallbacks(updateClockTask);
            fitnessContinuePauseRecord.setText(R.string.activity_tracking_start_record);
            // stop tracking location
            setRequestingLocationUpdates(getContext(), false);
            mFusedLocationClient.removeLocationUpdates(getPendingIntent());
        } else {
            // start
            currentActivity.start();
            isRecording = true;
            updateClockHandler.removeCallbacks(updateClockTask);
            updateClockHandler.postDelayed(updateClockTask, 100);
            fitnessContinuePauseRecord.setText(R.string.activity_tracking_pause_record);
            // TODO: re-check for location permission before starting again
            try {
                setRequestingLocationUpdates(getContext(), true);
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, getPendingIntent());
            } catch (SecurityException e) {
                setRequestingLocationUpdates(getContext(), false);
            }
        }
    }

    /**
     * Provides a simple way of getting a device's location and is well suited for
     * applications that do not require a fine-grained location and that do not need location
     * updates. Gets the best and most recent location currently available, which may be null
     * in rare cases when a location is not available.
     * <p>
     * Note: this method should be called after location permission has been granted.
     */
    @SuppressWarnings("MissingPermission")
//    private void getLastLocation() {
//        mFusedLocationClient.getLastLocation()
//                .addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Location> task) {
//                        if (task.isSuccessful() && task.getResult() != null) {
//                            currentActivity.addLocation(task.getResult());
//                        } else {
//                            Utils.showSnackbar(getView(), getString(R.string.no_location_detected));
//                        }
//                    }
//                });
//    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (s.equals(KEY_LOCATION_UPDATES_RESULT_TOP_SPEED)) {
            topSpeedText.setText(sp.getString(KEY_LOCATION_UPDATES_RESULT_TOP_SPEED, ""));
        } else if (s.equals(KEY_LOCATION_UPDATES_RESULT_AVG_SPEED)) {
            averageSpeedText.setText(sp.getString(KEY_LOCATION_UPDATES_RESULT_AVG_SPEED, ""));
        } else if (s.equals(KEY_LOCATION_UPDATES_RESULT_DISTANCE)) {
            distanceText.setText(sp.getString(KEY_LOCATION_UPDATES_RESULT_DISTANCE, ""));
        } else if (s.equals(KEY_LOCATION_UPDATES_REQUESTED)) {
            boolean requestingUpdates = sp.getBoolean(KEY_LOCATION_UPDATES_REQUESTED, false);
        }
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(getContext(), LocationUpdatesBroadcastReceiver.class);
        intent.setAction(LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES);
        return PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void setRequestingLocationUpdates(Context context, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(KEY_LOCATION_UPDATES_REQUESTED, value).apply();
    }

    /**
     * Called when new Locations are received.
     *
     * @param context
     * @param locations
     */
    public static void setLocationUpdatesResult(Context context, List<Location> locations) {
        // TODO: store data in db
        FitnessActivity activity = currentActivity;
        if (activity != null) {
            activity.addLocations(locations);
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putString(KEY_LOCATION_UPDATES_RESULT_TOP_SPEED, String.format(Locale.ENGLISH, "%s: %.2f%s", R.string.activity_top_speed_label, currentActivity.getTopSpeed(), R.string.activity_speed_unit_metric))
                    .putString(KEY_LOCATION_UPDATES_RESULT_AVG_SPEED, String.format(Locale.ENGLISH, "%s: %.2f%s", R.string.activity_top_speed_label, currentActivity.getAverageSpeed(), R.string.activity_speed_unit_metric))
                    .putString(KEY_LOCATION_UPDATES_RESULT_DISTANCE, String.format(Locale.ENGLISH, "%s: %.2f%s", R.string.activity_top_speed_label, currentActivity.getDistance(), R.string.activity_distance_unit_short_metric))
                    .apply();
        }
    }
}
