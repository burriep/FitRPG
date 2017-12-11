package edu.uwm.cs.fitrpg.activity;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.MapActivity;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.fragments.CurrentLevelFragment;
import edu.uwm.cs.fitrpg.model.FitnessChallengeLevel;
import edu.uwm.cs.fitrpg.model.User;
import edu.uwm.cs.fitrpg.util.Utils;

public class Home extends AppCompatActivity {
    public static Context appCon;
    private BottomNavigationView.OnNavigationItemSelectedListener OnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
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
                    return true;
                case R.id.navigation_game_map:
                    intent = new Intent(getApplicationContext(), MapActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_settings:
                    intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
            }
            return false;
        }
    };
    private TextView stamina, speed, strength, endurance, dexterity;
    private DatabaseHelper myDB;
    private int userID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        appCon = this.getApplicationContext();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener);

        //TextView stamina, speed, strength, endurance, dexterity;
        stamina = (TextView) findViewById(R.id.home_stamina);
        speed = (TextView) findViewById(R.id.home_speed);
        strength = (TextView) findViewById(R.id.home_strength);
        endurance = (TextView) findViewById(R.id.home_endurance);
        dexterity = (TextView) findViewById(R.id.home_dexterity);
        myDB = new DatabaseHelper(this);

        //PS Check database for a user with ID 1, otherwise create one
        if (myDB.getStamina(userID) == "-1") {
            myDB.createChar(userID, 0, "Defaultio", 10, 10, 10, 10, 10, 1);
            FitnessChallengeLevel.initUser(myDB.getReadableDatabase(), userID);
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
        if (fragmentManager.findFragmentById(R.id.home_top_frame) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            CurrentLevelFragment fragment = new CurrentLevelFragment();
            transaction.add(R.id.home_top_frame, fragment);
            transaction.commit();
        }
        doTheUser();
    }

    private void doTheUser() {
        if (myDB.hasUser()) {
            return;
        } else {
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
            final EditText etName = alertLayout.findViewById(R.id.diaglog_name);
            final EditText etWeight = alertLayout.findViewById(R.id.diaglog_weight);
            final EditText etHeight = alertLayout.findViewById(R.id.diaglog_height);

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Create User");
            alert.setView(alertLayout);
            alert.setCancelable(false);

            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                String name = "";
                int weight = 0;
                int height = 0;

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(etName.getText().length() > 0)
                        name = etName.getText().toString();
                    else
                        name = "User";
                    if(etWeight.getText().length() > 0)
                        weight = Integer.parseInt(etWeight.getText().toString());
                    if(etHeight.getText().length() > 0)
                        height = Integer.parseInt(etHeight.getText().toString());


                    User user = new User(name, 1);
                    user.setWeight(weight);
                    user.setHeight(height);
                    user.setLastUpdateDate(new SimpleDateFormat(Utils.ISO_DATE_TIME_FORMAT).format(Calendar.getInstance().getTime()));
                    user.updateUser(myDB);
                    Toast.makeText(getApplicationContext(), "User created", Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog dialog = alert.create();
            dialog.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (myDB.getStamina(userID) == "-1") {
            myDB.createChar(userID, 0, "Defaultio", 10, 10, 10, 10, 10, 1);
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

    @Override
    public void onBackPressed() {
        finish();
    }

}
