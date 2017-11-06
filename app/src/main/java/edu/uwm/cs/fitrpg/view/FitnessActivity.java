package edu.uwm.cs.fitrpg.view;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

//<<<<<<< Updated upstream
//=======
import edu.uwm.cs.fitrpg.HistoryList;
import edu.uwm.cs.fitrpg.MainActivity;
//>>>>>>> Stashed changes
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.fragments.FitnessEntryFragment;
import edu.uwm.cs.fitrpg.fragments.FitnessHistoryFragment;
import edu.uwm.cs.fitrpg.fragments.FitnessTrackDataFragment;

public class FitnessActivity extends AppCompatActivity {
    private static int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);


        TextView history, recordData, trackData, goToMap;
        history = (TextView) findViewById(R.id.tv_fitness_history);
        recordData = (TextView) findViewById(R.id.tv_fitness_record_data);
        trackData = (TextView) findViewById(R.id.tv_fitness_track_data);
        goToMap = (TextView) findViewById(R.id.tv_fitness_gotoMap);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoList();

//                position = 1;
//                replaceFragment(position);
            }
        });

        recordData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = 2;
                replaceFragment(position);
            }
        });

        trackData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = 3;
                replaceFragment(position);
            }
        });

        goToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void replaceFragment(int pos) {
        Fragment fragment = null;
        FitnessEntryFragment fragmentEntry = null;
        TrackFitnessActivity fragEntry = null;
        Bundle bundle = new Bundle();

        switch (pos) {
            case (1):
                fragment = new FitnessHistoryFragment();
                break;
            case (2):
                bundle.putInt("Position", position);
                fragmentEntry = new FitnessEntryFragment();
                fragmentEntry.setArguments(bundle);
                break;
            case (3):
                gotoTracker();
                break;
            default:
                fragment = new FitnessHistoryFragment();
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fitness_frag_data, fragment);
            transaction.commit();
        } else if (fragmentEntry != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fitness_frag_data, fragmentEntry);
            transaction.commit();
        }
        else if (fragEntry != null) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.replace(R.id.fitness_frag_data, fragEntry);
//            transaction.commit();
        }
    }

    public void gotoList() {
        Intent intent = new Intent(this, HistoryList.class);
        startActivity(intent);
    }

    public void gotoTracker() {
        Intent intent = new Intent(this, TrackFitnessActivity.class);
        startActivity(intent);
    }

}
