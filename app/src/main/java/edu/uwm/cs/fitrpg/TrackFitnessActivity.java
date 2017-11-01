package edu.uwm.cs.fitrpg;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class TrackFitnessActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private boolean isRecording;
    private TextView elapsedTime;
    private FusedLocationProviderClient mFusedLocationClient;
    private List<Location> locations;
    private long sumOfPreviousTimes;
    private Button startStopButton;
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
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_fitness);
        // UI widgets
        elapsedTime = (TextView) findViewById(R.id.elapsedTime);
        startStopButton = (Button) findViewById(R.id.fitness_continue_pause_record);
        // data
        locations = new LinkedList<>();
        times = new LinkedList<>();
        // location services
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // create location request
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setMaxWaitTime(MAX_WAIT_TIME);
    }

    @Override
    public void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        // manage permissions
        if (!hasLocationPermission()) {
            startStopButton.setEnabled(false);
            requestLocationPermission();
        } else {
            startStopButton.setEnabled(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: update anything on screen that could have changed state
    }

    public void onStop() {
        super.onStop();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    private String formatDuration(long ms) {
        long seconds = ms / 1000L;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        String result = "";
        if (days > 0) {
            result += days + days == 1 ? "day " : "days ";
        }
        // cleanup clock values
        hours = hours % 24;
        minutes = minutes % 60;
        seconds = seconds % 60;

        if (hours > 0) {
            result += String.format(Locale.ENGLISH, "%02d:", hours);
        }
        result += String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds);
        return result;
    }

    public void recordFitnessActivity(View view) {
        if (isRecording) {
            startStopTimer();
            stopTrackingLocation();
        } else {
            if (hasLocationPermission()) {
                startStopTimer();
                startTrackingLocation();
            } else {
                requestLocationPermission();
            }
        }
    }

    private void startStopTimer() {
        if (times.size() % 2 == 0) {
            // start
            long startTime = android.os.SystemClock.elapsedRealtime();
            times.add(startTime);
            elapsedTime.setText(formatDuration(sumOfPreviousTimes));
            startStopButton.setText(R.string.activity_tracking_pause_record);
        } else {
            // stop
            long startTime = times.get(times.size() - 1);
            long stopTime = android.os.SystemClock.elapsedRealtime();
            sumOfPreviousTimes += (stopTime - startTime);
            times.add(stopTime);
            elapsedTime.setText(formatDuration(sumOfPreviousTimes));
            startStopButton.setText(R.string.activity_tracking_start_record);
        }
    }

    private void startTrackingLocation() {
        getLastLocation();
    }

    private void stopTrackingLocation() {
        getLastLocation();
    }


    /**
     * Shows a {@link Snackbar} using {@code text}.
     *
     * @param text The Snackbar text.
     */
    private void showSnackbar(final String text) {
        View container = findViewById(R.id.track_fitness_container);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId, View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content), getString(mainTextStringId), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(TrackFitnessActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    private void requestLocationPermission() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            showSnackbar(R.string.location_permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startLocationPermissionRequest();
                        }
                    });
        } else {
            startLocationPermissionRequest();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                    startStopButton.setEnabled(true);
                } else {
                    // Permission denied.
                    startStopButton.setEnabled(false);
                    // inform user that the permission is needed to use the app
                    showSnackbar(R.string.location_permission_denied_explanation, R.string.settings,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Build intent that displays the App settings screen.
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                                    intent.setData(uri);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });
                }
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
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            locations.add(task.getResult());
                        } else {
                            showSnackbar(getString(R.string.no_location_detected));
                        }
                    }
                });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(Utils.KEY_LOCATION_UPDATES_RESULT)) {
            // new Locations are available
            Utils.getLocationUpdatesResult(this);
        } else if (s.equals(Utils.KEY_LOCATION_UPDATES_REQUESTED)) {
            // TODO: update UI state to show if location updates are being requested
            // Utils.getRequestingLocationUpdates(this);
        }
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, LocationUpdatesBroadcastReceiver.class);
        intent.setAction(LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Requests start of location updates.
     */
    private void requestLocationUpdates(View view) {
        try {
            Utils.setRequestingLocationUpdates(this, true);
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, getPendingIntent());
        } catch (SecurityException e) {
            Utils.setRequestingLocationUpdates(this, false);
        }
    }

    /**
     * Requests removal of location updates.
     */
    private void removeLocationUpdates(View view) {
        Utils.setRequestingLocationUpdates(this, false);
        mFusedLocationClient.removeLocationUpdates(getPendingIntent());
    }



}
