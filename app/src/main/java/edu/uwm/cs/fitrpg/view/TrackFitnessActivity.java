package edu.uwm.cs.fitrpg.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import edu.uwm.cs.fitrpg.BuildConfig;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.Utils;
import edu.uwm.cs.fitrpg.fragments.FitnessTrackDataFragment;
import edu.uwm.cs.fitrpg.fragments.PhysicalActivityTypeFragment;
import edu.uwm.cs.fitrpg.model.PhysicalActivityType;

public class TrackFitnessActivity extends AppCompatActivity implements FitnessTrackDataFragment.OnFragmentInteractionListener, PhysicalActivityTypeFragment.OnListFragmentInteractionListener {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6923;
    PhysicalActivityType activityType;
    Button continuePauseButton, cancelButton, stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_fitness);

        continuePauseButton = findViewById(R.id.fitness_continue_pause_record);
        cancelButton = findViewById(R.id.fitness_cancel);
        stopButton = findViewById(R.id.fitness_stop);

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

        boolean hasLocationPermissions = hasLocationPermission();
        updateButtonState();
        if (!hasLocationPermissions) {
            requestLocationPermission();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void startTrackingActivity(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.track_fitness_top_frame) != null) {
            FitnessTrackDataFragment fragment = FitnessTrackDataFragment.newInstance(activityType.getId());
            // Replace whatever is in the fragment_container view with this fragment.
            fragmentManager.beginTransaction().replace(R.id.track_fitness_top_frame, fragment).commit();
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
        Log.i("Test Activity", item.toString());
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

    @Override
    public void onStopTrackingFitnessActivity() {
        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
        startActivity(intent);
    }

    @Override
    public void onCancelTrackingFitnessActivity() {
        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
        startActivity(intent);
    }
}
