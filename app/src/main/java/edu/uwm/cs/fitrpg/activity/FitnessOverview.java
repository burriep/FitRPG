package edu.uwm.cs.fitrpg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.fragments.FitnessActivityHistoryFragment;
import edu.uwm.cs.fitrpg.fragments.FitnessEntryFragment;
import edu.uwm.cs.fitrpg.model.FitnessActivity;

public class FitnessOverview extends AppCompatActivity implements FitnessActivityHistoryFragment.OnListFragmentInteractionListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);

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
    public void onListFragmentInteraction(FitnessActivity item) {

    }
}
