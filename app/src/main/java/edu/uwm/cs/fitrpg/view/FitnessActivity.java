package edu.uwm.cs.fitrpg.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.activity.FitnessActivityHistory;
import edu.uwm.cs.fitrpg.activity.FitnessActivityTracking;
import edu.uwm.cs.fitrpg.fragments.FitnessEntryFragment;

public class FitnessActivity extends AppCompatActivity {
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
                Intent intent = new Intent(FitnessActivity.this, FitnessActivityHistory.class);
                startActivity(intent);
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
                Intent intent = new Intent(FitnessActivity.this, FitnessActivityTracking.class);
                startActivity(intent);
            }
        });
    }
}
