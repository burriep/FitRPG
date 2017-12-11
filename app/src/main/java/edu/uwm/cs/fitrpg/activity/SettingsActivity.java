package edu.uwm.cs.fitrpg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.MapActivity;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.model.User;

import static edu.uwm.cs.fitrpg.util.Utils.ISO_DATE_TIME_FORMAT;

public class SettingsActivity extends AppCompatActivity{
    private int navigationIDTag;


    public EditText etName, etWeight, etHeight;
    public TextView tvUpdateDate, tutorial, aboutMe;
    public Button btnSave, btnReset;
    private User user;
    private DatabaseHelper db;
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(ISO_DATE_TIME_FORMAT);

    private String name;
    private int weight, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        navigationIDTag = 0;

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener);

        navigation.getMenu().getItem(0).setChecked(false);
        navigation.getMenu().getItem(3).setChecked(true);

        db = new DatabaseHelper(this);

        if (db.hasUser())
            getUser();
        else
         initUser();

        name = user.getName();
        weight = user.getWeight();
        height = user.getHeight();

        etName = findViewById(R.id.settings_name);
        etWeight = findViewById(R.id.settings_weight);
        etHeight = findViewById(R.id.settings_height);
        tvUpdateDate = findViewById(R.id.settings_last_updated);
        btnReset = findViewById(R.id.btn_settings_reset);
        btnSave = findViewById(R.id.btn_settings_save);

        tutorial = findViewById(R.id.tutorial);
        aboutMe = findViewById(R.id.about_me);

        //updateFakeUser();
        resetSettings();

        createListeners();

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
                    intent = new Intent(getApplicationContext(), FitnessOverview.class);
                    startActivity(intent);
                    navigationIDTag = 2;
                    finish();
                    return true;
                case R.id.navigation_game_map:
                    intent = new Intent(getApplicationContext(), MapActivity.class);
                    startActivity(intent);
                    navigationIDTag = 3;
                    finish();
                    return true;
                case R.id.navigation_settings:
                    navigationIDTag = 4;
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

    private boolean hasUser() {
        if (db.getUser() == null)
            return false;
        else
            return true;
    }

    private void getUser() {
        user = db.getUser();
        closeDatabase();
    }

    private void initUser() {
        user = new User("User", 1);
        updateFakeUser();
    }

    private void updateFakeUser() {
        user.setHeight(75);
        user.setWeight(200);
        user.setLastUpdateDate(new SimpleDateFormat(ISO_DATE_TIME_FORMAT).format(Calendar.getInstance().getTime()));
    }

    private void resetSettings() {
        etName.setText("");
        etName.setHint(user.getName());
        etWeight.setText("");
        etWeight.setHint(Integer.toString(user.getWeight()));
        etHeight.setText("");
        etHeight.setHint(Integer.toString(user.getHeight()));
        tvUpdateDate.setText("Last updated: " + user.getLastUpdateDate());
    }

    public void createListeners() {
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetSettings();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean changed = false;
                if(etName.getText().length() > 0) {
                    name = etName.getText().toString();
                    changed = true;
                }
                if(etWeight.getText().length() > 0) {
                    weight = Integer.parseInt(etWeight.getText().toString());
                    changed = true;
                }
                if(etHeight.getText().length() > 0) {
                    height = Integer.parseInt(etHeight.getText().toString());
                    changed = true;
                }
                if(changed) {
                    tvUpdateDate.setText(DATE_FORMAT.format(Calendar.getInstance().getTime()));
                    updateSettings();
                    resetSettings();
                    Toast.makeText(getApplicationContext(), "Updated user", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Nothing to update", Toast.LENGTH_SHORT).show();
                }

            }
        });

        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
                startActivity(intent);
            }
        });

        aboutMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AboutMe.class);
                startActivity(intent);
            }
        });
    }

    private void updateSettings() {
        user.setLastUpdateDate(DATE_FORMAT.format(Calendar.getInstance().getTime()));
        user.setName(name);
        user.setWeight(weight);
        user.setHeight(height);
        user.updateUser(db);
        closeDatabase();
        //Add calls to update user in database
        // ...
    }

    private void closeDatabase() {
        db.close();
    }


}
