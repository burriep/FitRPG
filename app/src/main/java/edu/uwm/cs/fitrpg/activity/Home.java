package edu.uwm.cs.fitrpg.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.MapActivity;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.fragments.CurrentLevelFragment;
import edu.uwm.cs.fitrpg.view.GameActivity;

public class Home extends AppCompatActivity{

    private int navigationIDTag;
    public static Context appCon;

    private int userID = 1;
    TextView stamina, speed, strength, endurance, dexterity;
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        appCon = this.getApplicationContext();
        navigationIDTag = 0;

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener);

        //TextView stamina, speed, strength, endurance, dexterity;
        stamina = (TextView) findViewById(R.id.tv_stamina);
        speed = (TextView) findViewById(R.id.tv_speed);
        strength = (TextView) findViewById(R.id.tv_strength);
        endurance = (TextView) findViewById(R.id.tv_endurance);
        dexterity = (TextView) findViewById(R.id.tv_dexterity);
        myDB = new DatabaseHelper(this);

        //PS Check database for a user with ID 0, otherwise create one
        if(myDB.getStamina(userID) == "-1") {
            myDB.createChar(userID, 0, "Defaultio", 10, 10, 10, 10, 10);
        }
        //These setText calls would eventually collect the stat info
        // from database or character class
        //ToDo
        //stamina.setText("" + 0);
        String staminaText = myDB.getStamina(userID);
        stamina.setText(staminaText);
        //speed.setText("" + 0);
        String speedText = myDB.getSpeed(userID);
        speed.setText(speedText);
        //strength.setText("" + 0);
        String strengthText = myDB.getStrength(userID);
        strength.setText(strengthText);
        //endurance.setText("" + 0);
        String enduranceText = myDB.getEndurance(userID);
        endurance.setText(enduranceText);
        //dexterity.setText("" + 0);
        String dexterityText = myDB.getDexterity(userID);
        dexterity.setText(dexterityText);

        FragmentManager fragmentManager = getFragmentManager();
        if(fragmentManager.findFragmentById(R.id.ll_top_left) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            CurrentLevelFragment fragment = new CurrentLevelFragment();
            transaction.add(R.id.ll_top_left, fragment);
            transaction.commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(myDB.getStamina(userID) == "-1") {
            myDB.createChar(userID, 0, "Defaultio", 10, 10, 10, 10, 10);
        }
        //These setText calls would eventually collect the stat info
        // from database or character class
        //ToDo
        //stamina.setText("" + 0);
        String staminaText = myDB.getStamina(userID);
        stamina.setText(staminaText);
        //speed.setText("" + 0);
        String speedText = myDB.getSpeed(userID);
        speed.setText(speedText);
        //strength.setText("" + 0);
        String strengthText = myDB.getStrength(userID);
        strength.setText(strengthText);
        //endurance.setText("" + 0);
        String enduranceText = myDB.getEndurance(userID);
        endurance.setText(enduranceText);
        //dexterity.setText("" + 0);
        String dexterityText = myDB.getDexterity(userID);
        dexterity.setText(dexterityText);

    }

    public void getStats() {

    }

    @Override
    public void onBackPressed() {
        finish();
    }


    public BottomNavigationView.OnNavigationItemSelectedListener OnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_fitness:
                    intent = new Intent(getApplicationContext(), FitnessOverview.class);
                    startActivity(intent);
                    finish();
                    navigationIDTag = 2;
                    return true;
                case R.id.navigation_game_map:
                    intent = new Intent(getApplicationContext(), MapActivity.class);
                    startActivity(intent);
                    finish();
                    navigationIDTag = 3;
                    return true;
                case R.id.navigation_settings:
                    intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                    finish();
                    navigationIDTag = 4;
                    return true;
            }
            return false;
        }
    };

}
