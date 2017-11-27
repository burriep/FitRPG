package edu.uwm.cs.fitrpg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import edu.uwm.cs.fitrpg.MapActivity;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.fragments.FitnessActivityHistoryFragment;
import edu.uwm.cs.fitrpg.fragments.FitnessEntryFragment;
import edu.uwm.cs.fitrpg.fragments.NavigationFragment;
import edu.uwm.cs.fitrpg.fragments.SettingsFragment;
import edu.uwm.cs.fitrpg.model.FitnessActivity;

public class FitnessOverview extends AppCompatActivity implements FitnessActivityHistoryFragment.OnListFragmentInteractionListener {
    private int navigationIDTag;
    BottomNavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);

        navigationIDTag = 0;
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener);
        navigation.getMenu().getItem(0).setChecked(false);
        navigation.getMenu().getItem(1).setChecked(true);


        TextView history, recordData, trackData, goToMap;
        history = (TextView) findViewById(R.id.tv_fitness_history);
        recordData = (TextView) findViewById(R.id.tv_fitness_record_data);
        trackData = (TextView) findViewById(R.id.tv_fitness_track_data);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                Date now = calendar.getTime();
                calendar.add(Calendar.DATE, -1);
                Date yesterday = calendar.getTime();

                FitnessActivityHistoryFragment fragment = FitnessActivityHistoryFragment.newInstance(yesterday, now);
                getSupportFragmentManager().beginTransaction().replace(R.id.fitness_frag_data, fragment).commit();
            }
        });

        recordData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FitnessEntryFragment fragmentEntry = new FitnessEntryFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fitness_frag_data, fragmentEntry).commit();
            }
        });

        trackData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FitnessOverview.this, FitnessActivityTracking.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        navigation.getMenu().getItem(0).setChecked(false);
        navigation.getMenu().getItem(1).setChecked(true);
    }

    public BottomNavigationView.OnNavigationItemSelectedListener OnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(getApplicationContext(), Home.class);
                    startActivity(intent);
                    navigationIDTag = 1;
                    finish();
                    return true;
                case R.id.navigation_fitness:
                    navigationIDTag = 2;
                    return true;
                case R.id.navigation_game_map:
                    intent = new Intent(getApplicationContext(), MapActivity.class);
                    startActivity(intent);
                    navigationIDTag = 3;
                    finish();
                    return true;
                case R.id.navigation_settings:
                    intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                    navigationIDTag = 4;
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
        navigationIDTag = 1;
        finish();
    }

    @Override
    public void onListFragmentInteraction(FitnessActivity item) {

    }
}
