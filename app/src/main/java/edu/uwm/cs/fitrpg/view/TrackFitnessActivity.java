package edu.uwm.cs.fitrpg.view;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import edu.uwm.cs.fitrpg.BuildConfig;
import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.Utils;
import edu.uwm.cs.fitrpg.fragments.FitnessTrackingDistanceFragment;
import edu.uwm.cs.fitrpg.fragments.FitnessTrackingRealtimeFragment;
import edu.uwm.cs.fitrpg.fragments.PhysicalActivityTypeFragment;
import edu.uwm.cs.fitrpg.model.*;
import edu.uwm.cs.fitrpg.model.FitnessActivity;
import edu.uwm.cs.fitrpg.services.LocationUpdatesService;

public class TrackFitnessActivity extends AppCompatActivity implements PhysicalActivityTypeFragment.OnListFragmentInteractionListener {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6923;
    PhysicalActivityType activityType;
    edu.uwm.cs.fitrpg.model.FitnessActivity currentActivity;
    Button continuePauseButton, cancelButton, stopButton;

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
    private Handler updateClockHandler = new Handler();
    private Runnable updateClockTask = new Runnable() {
        public void run() {
            PreferenceManager.getDefaultSharedPreferences(TrackFitnessActivity.this).edit().putLong(Utils.SP_KEY_DURATION_SECONDS_UPDATES, currentActivity.getDuration()).apply();
            updateClockHandler.postDelayed(updateClockTask, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_fitness);

        continuePauseButton = findViewById(R.id.fitness_continue_pause_record);
        cancelButton = findViewById(R.id.fitness_cancel);
        stopButton = findViewById(R.id.fitness_stop);

        myReceiver = new MyReceiver();

        if (savedInstanceState != null) {
            // If we're being restored from a previous state, then we don't need to do anything and should return or else we could end up with overlapping fragments.
            // TODO: if the user is currently tracking an activity, go to that fragment instead of the selection fragment.
        } else {
            // Create a new Fragment to be placed in the activity layout
            PhysicalActivityTypeFragment topFragment = PhysicalActivityTypeFragment.newInstance();
            // In case this activity was started with special instructions from an Intent, pass the Intent's extras to the fragment as arguments
//            topFragment.setArguments(getIntent().getExtras());
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.track_fitness_top_frame, topFragment).commit();
        }

        continuePauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTrackingActivity(view);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection, Context.BIND_AUTO_CREATE);

        boolean hasLocationPermissions = hasLocationPermission();
        updateButtonState();
        if (!hasLocationPermissions) {
            requestLocationPermission();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: fix this
        if (hasLocationPermission())
            LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void startTrackingActivity(View view) {
        if (currentActivity == null) {
            Fragment fragment = FitnessTrackingRealtimeFragment.newInstance(activityType);
            // Replace whatever is in the fragment_container view with this fragment.
            getSupportFragmentManager().beginTransaction().replace(R.id.track_fitness_top_frame, fragment).commit();

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelFitnessActivity(view);
                }
            });
            stopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopFitnessActivity(view);
                }
            });
            currentActivity = new FitnessActivity();
            currentActivity.setType(activityType);
            currentActivity.start();
        } else {
            if (currentActivity.isTracking()) {
                stopTrackingFitness();
            } else {
                startTrackingFitness();
            }
            stopButton.setEnabled(true);
        }
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    private void requestLocationPermission() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            Utils.showSnackbar(findViewById(R.id.activity_track_fitness), getString(R.string.location_permission_rationale), getString(android.R.string.ok),
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

    private void updateButtonState() {
        boolean hasLocationPermissions = hasLocationPermission();
        continuePauseButton.setEnabled(hasLocationPermissions && activityType != null);
    }

    @Override
    public void onListFragmentInteraction(PhysicalActivityType item) {
        activityType = item;
        updateButtonState();
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
                    updateButtonState();
                } else {
                    // Permission denied.
                    updateButtonState();
                    // inform user that the permission is needed to use the app
                    Utils.showSnackbar(findViewById(R.id.activity_track_fitness), getString(R.string.location_permission_denied_explanation), getString(R.string.settings),
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

    public void cancelFitnessActivity(View view) {
        if (currentActivity.isTracking()) {
            stopTrackingFitness();
        }
        continuePauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        cancelButton.setEnabled(false);
        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
        startActivity(intent);
    }

    public void stopFitnessActivity(View view) {
        if (currentActivity.isTracking()) {
            stopTrackingFitness();
        }
        continuePauseButton.setEnabled(false);
        stopButton.setEnabled(false);
        cancelButton.setEnabled(false);
        if (currentActivity != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    currentActivity.create(new DatabaseHelper(TrackFitnessActivity.this).getWritableDatabase());
                    TrackFitnessActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                            startActivity(intent);
                        }
                    });
                }
            }).run();
        } else {
            Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
            startActivity(intent);
        }
    }

    private void startTrackingFitness() {
        // start
        currentActivity.start();
        updateClockHandler.removeCallbacks(updateClockTask);
        updateClockHandler.postDelayed(updateClockTask, 1000);
        continuePauseButton.setText(R.string.activity_tracking_pause_record);
        if (mService != null) {
            mService.requestLocationUpdates();
        }
    }

    private void stopTrackingFitness() {
        // stop
        currentActivity.stop();
        updateClockHandler.removeCallbacks(updateClockTask);
        continuePauseButton.setText(R.string.activity_tracking_start_record);
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
                if (currentActivity != null) {
                    currentActivity.addLocation(location);
                    PreferenceManager.getDefaultSharedPreferences(context)
                            .edit()
                            .putLong(Utils.SP_KEY_TOP_SPEED_UPDATES, Double.doubleToRawLongBits(currentActivity.getTopSpeed()))
                            .putLong(Utils.SP_KEY_AVG_SPEED_UPDATES, Double.doubleToRawLongBits(currentActivity.getAverageSpeed()))
                            .putLong(Utils.SP_KEY_DISTANCE_UPDATES, Double.doubleToRawLongBits(currentActivity.getDistance()))
                            .apply();
                }
            }
        }
    }

}
