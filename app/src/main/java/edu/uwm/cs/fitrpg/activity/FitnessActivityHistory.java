package edu.uwm.cs.fitrpg.activity;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.fragments.FitnessActivityHistoryFragment;
import edu.uwm.cs.fitrpg.model.FitnessActivity;

public class FitnessActivityHistory extends Activity implements FitnessActivityHistoryFragment.OnListFragmentInteractionListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_history);

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();

        Fragment fragment = FitnessActivityHistoryFragment.newInstance(yesterday, now);
        getFragmentManager().beginTransaction().replace(R.id.fitness_history_layout, fragment).commit();
    }

    @Override
    public void onListFragmentInteraction(FitnessActivity item) {
    }
}
