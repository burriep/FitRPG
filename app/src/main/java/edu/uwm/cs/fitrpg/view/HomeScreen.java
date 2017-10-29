package edu.uwm.cs.fitrpg.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.fragments.CurrentLevelFragment;
import edu.uwm.cs.fitrpg.fragments.GotoGameFragment;

public class HomeScreen extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // AvatarControlFragment Information
        TextView stamina, speed, strength, endurance, dexterity;
        stamina = (TextView) findViewById(R.id.stamina);
        speed = (TextView) findViewById(R.id.speed);
        strength = (TextView) findViewById(R.id.strength);
        endurance = (TextView) findViewById(R.id.endurance);
        dexterity = (TextView) findViewById(R.id.dexterity);

        //These setText calls would eventually collect the stat info
        // from database or character class
        //ToDo
        stamina.setText("" + 0);
        speed.setText("" + 0);
        strength.setText("" + 0);
        endurance.setText("" + 0);
        dexterity.setText("" + 0);

        FragmentManager fragmentManager = getFragmentManager();
        if(fragmentManager.findFragmentById(R.id.current_level) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            CurrentLevelFragment fragment = new CurrentLevelFragment();
            transaction.add(R.id.current_level, fragment);
            transaction.commit();
        }

        FragmentManager fragmentManager2 = getFragmentManager();
        if(fragmentManager2.findFragmentById(R.id.advance) == null) {
            FragmentTransaction transaction = fragmentManager2.beginTransaction();
            GotoGameFragment fragment = new GotoGameFragment();
            transaction.add(R.id.advance, fragment);
            transaction.commit();
        }

    }
}
