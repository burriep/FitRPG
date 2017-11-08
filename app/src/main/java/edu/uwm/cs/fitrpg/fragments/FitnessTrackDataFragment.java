package edu.uwm.cs.fitrpg.fragments;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;

import java.util.Locale;

import edu.uwm.cs.fitrpg.model.FitnessActivity;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.Utils;
import edu.uwm.cs.fitrpg.services.LocationUpdatesService;


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
    private Button fitnessCancel;
    private Button fitnessStop;
    private OnFragmentInteractionListener mListener;

    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;

    // A reference to the service used to get location updates.
    private LocationUpdatesService mService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;

    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };


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

    private static final String ARG_CURRENT_FITNESS_ACTIVITY_TYPE = "param1";

    private boolean needsLocation;
    private static FitnessActivity currentActivity;
    private boolean isRecording;

    public FitnessTrackDataFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FitnessTrackDataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FitnessTrackDataFragment newInstance(String fitnessActivityType, FitnessActivity fa) {
        FitnessTrackDataFragment fragment = new FitnessTrackDataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CURRENT_FITNESS_ACTIVITY_TYPE, fitnessActivityType);
        currentActivity = fa;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myReceiver = new MyReceiver();
        if (getArguments() != null) {
            String fitnessActivityType = getArguments().getString(ARG_CURRENT_FITNESS_ACTIVITY_TYPE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);

        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        getContext().bindService(new Intent(getContext(), LocationUpdatesService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onResume() {
        super.onResume();

        isRecording = currentActivity != null && currentActivity.isTracking();
        fitnessContinuePauseRecord.setEnabled(true);

        updateClockHandler.removeCallbacks(updateClockTask);
        updateClockHandler.postDelayed(updateClockTask, 100);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(myReceiver, new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));

        if (currentActivity == null) {
            currentActivity = new FitnessActivity();
        }
    }

    @Override
    public void onPause() {
        updateClockHandler.removeCallbacks(updateClockTask);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(myReceiver);
        super.onPause();
    }

    public void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            getContext().unbindService(mServiceConnection);
            mBound = false;
        }
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        elapsedTimeText = view.findViewById(R.id.elapsed_time_text);
        distanceText = view.findViewById(R.id.distance_text);
        averageSpeedText = view.findViewById(R.id.average_speed_text);
        topSpeedText = view.findViewById(R.id.top_speed_text);
        fitnessContinuePauseRecord = view.findViewById(R.id.fitness_continue_pause_record);
        fitnessCancel = view.findViewById(R.id.fitness_cancel);
        fitnessStop = view.findViewById(R.id.fitness_stop);
        fitnessContinuePauseRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordFitnessActivity(view);
            }
        });
        fitnessCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelFitnessActivity(view);
            }
        });
        fitnessStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopFitnessActivity(view);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fitness_track_data, container, false);
    }

    private Handler updateClockHandler = new Handler();
    private Runnable updateClockTask = new Runnable() {
        public void run() {
            // update clock / time
            String display = Utils.formatDuration(currentActivity.getDuration());
            elapsedTimeText.setText(display);
            // update location summary
//            updateLocationSummary();
            // schedule next iteration
            updateClockHandler.postDelayed(updateClockTask, 1000);
        }
    };
//
//    private void updateLocationSummary() {
//        distanceText.setText(String.format(Locale.ENGLISH, "%.2f", currentActivity.getDistance()));
//        averageSpeedText.setText(String.format(Locale.ENGLISH, "%.2f", currentActivity.getAverageSpeed()));
//        topSpeedText.setText(String.format(Locale.ENGLISH, "%.2f", currentActivity.getTopSpeed()));
//    }

    public void recordFitnessActivity(View view) {
        if (isRecording) {
            stopTrackingFitness();
        } else {
            startTrackingFitness();
        }
    }

    public void cancelFitnessActivity(View view) {
        if (isRecording) {
            stopTrackingFitness();
        }
        mListener.onCancelTrackingFitnessActivity();
    }

    public void stopFitnessActivity(View view) {
        if (isRecording) {
            stopTrackingFitness();
        }
        if (currentActivity != null) {
            // TODO: save in a background thread / async
            currentActivity.create(getContext());
        }
        mListener.onStopTrackingFitnessActivity();
    }

    private void startTrackingFitness() {
        // start
        currentActivity.start();
        isRecording = true;
        updateClockHandler.removeCallbacks(updateClockTask);
        updateClockHandler.postDelayed(updateClockTask, 100);
        fitnessContinuePauseRecord.setText(R.string.activity_tracking_pause_record);
        // TODO: re-check for location permission before starting again
        if (mService != null) {
            mService.requestLocationUpdates();
        }
    }

    private void stopTrackingFitness() {
        // stop
        currentActivity.stop();
        isRecording = false;
        updateClockHandler.removeCallbacks(updateClockTask);
        fitnessContinuePauseRecord.setText(R.string.activity_tracking_start_record);
        // stop tracking location
        if (mService != null) {
            mService.removeLocationUpdates();
        }
    }

    /**
     * Receiver for broadcasts sent by {@link LocationUpdatesService}.
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            if (location != null) {
                Toast.makeText(getActivity(), Utils.getLocationText(location), Toast.LENGTH_SHORT).show();
                FitnessActivity activity = currentActivity;
                if (activity != null) {
                    activity.addLocation(location);
                    PreferenceManager.getDefaultSharedPreferences(context)
                            .edit()
                            .putString(KEY_LOCATION_UPDATES_RESULT_TOP_SPEED, String.format(Locale.ENGLISH, "%s: %.2f%s", R.string.activity_top_speed_label, currentActivity.getTopSpeed(), R.string.activity_speed_unit_metric))
                            .putString(KEY_LOCATION_UPDATES_RESULT_AVG_SPEED, String.format(Locale.ENGLISH, "%s: %.2f%s", R.string.activity_top_speed_label, currentActivity.getAverageSpeed(), R.string.activity_speed_unit_metric))
                            .putString(KEY_LOCATION_UPDATES_RESULT_DISTANCE, String.format(Locale.ENGLISH, "%s: %.2f%s", R.string.activity_top_speed_label, currentActivity.getDistance(), R.string.activity_distance_unit_short_metric))
                            .apply();
                }
            }
        }
    }

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onStopTrackingFitnessActivity();
        void onCancelTrackingFitnessActivity();
    }
}
