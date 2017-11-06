package edu.uwm.cs.fitrpg.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.fragments.FitnessActivitySelectionFragment;
import edu.uwm.cs.fitrpg.fragments.FitnessTrackDataFragment;

public class TrackFitnessActivity extends AppCompatActivity implements FitnessActivitySelectionFragment.OnFragmentInteractionListener, FitnessTrackDataFragment.OnFragmentInteractionListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_fitness);
        // TODO: if the user is currently tracking an activity, go to that fragment instead of the selection fragment.
        // Check that the activity is using the layout version with the fragment_container FrameLayout
        if (findViewById(R.id.track_fitness_container) != null) {
            // However, if we're being restored from a previous state, then we don't need to do anything and should return or else we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            // Create a new Fragment to be placed in the activity layout
            FitnessActivitySelectionFragment firstFragment = FitnessActivitySelectionFragment.newInstance();
            // In case this activity was started with special instructions from an Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.track_fitness_container, firstFragment).commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
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

    @Override
    public void onStartTrackingFitnessActivity(String activity) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.track_fitness_container) != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            FitnessTrackDataFragment fragment = FitnessTrackDataFragment.newInstance(activity, null);
            Bundle args = new Bundle();
//            args.putInt(ArticleFragment.ARG_POSITION, position);
            fragment.setArguments(args);
            // Replace whatever is in the fragment_container view with this fragment, and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.track_fitness_container, fragment);
//            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        // respond to the "start" tracking activity button
        FitnessActivitySelectionFragment fragment = (FitnessActivitySelectionFragment) getSupportFragmentManager().findFragmentById(R.id.track_fitness_container);
        if (fragment != null) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
