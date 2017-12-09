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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.MapActivity;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.model.User;

public class SettingsActivity extends AppCompatActivity{
    private int navigationIDTag;


    public EditText etName, etWeight, etHeight;
    public TextView tvUpdateDate, tutorial, aboutMe;
    public Button btnSave, btnClear;
    private User user;
    private DatabaseHelper db;
    public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

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

        etName = findViewById(R.id.et_settings_name);
        etWeight = findViewById(R.id.et_settings_weight);
        etHeight = findViewById(R.id.et_settings_height);
        tvUpdateDate = findViewById(R.id.tv_settings_lastUpdateDate);
        btnClear = findViewById(R.id.btn_settings_clear);
        btnSave = findViewById(R.id.btn_settings_save);

        tutorial = findViewById(R.id.tutorial);
        aboutMe = findViewById(R.id.about_me);

        //updateFakeUser();
        createHints();

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
        //this.user = new User(db.getUserName, db.getUserId);
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

    private void createHints() {
        etName.setHint(user.getName());
        etWeight.setHint("" + user.getWeight());
        etHeight.setHint("" + user.getHeight());
        tvUpdateDate.setHint("" + user.getLastUpdateDate());
    }

    public void createListeners() {
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etName.setText("");
                etHeight.setText("");
                etWeight.setText("");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //updateSettings();
                name = etName.getText().toString();
                weight = Integer.parseInt(etWeight.getText().toString());
                height = Integer.parseInt(etHeight.getText().toString());
                tvUpdateDate.setText(new SimpleDateFormat(ISO_DATE_TIME_FORMAT).format(Calendar.getInstance().getTime()));
                updateSettings();
                createHints();
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
        user.setLastUpdateDate(new SimpleDateFormat(ISO_DATE_TIME_FORMAT).format(Calendar.getInstance().getTime()));
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
