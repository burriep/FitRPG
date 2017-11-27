package edu.uwm.cs.fitrpg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
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
import edu.uwm.cs.fitrpg.fragments.FitnessEntryFragment;
import edu.uwm.cs.fitrpg.fragments.NavigationFragment;
import edu.uwm.cs.fitrpg.fragments.SettingsFragment;
import edu.uwm.cs.fitrpg.model.User;

public class SettingsActivity extends AppCompatActivity{
    private int navigationIDTag;


    public EditText etName, etWeight, etHeight;
    public TextView tvUpdateDate;
    public Button btnSave, btnClear;
    private User user;

    private String name;
    private double weight, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings);

        navigationIDTag = 0;

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener);

        user = new User("Tyler", 1);

        name = user.getName();
        weight = user.getWeight();
        height = user.getHeight();

        etName = findViewById(R.id.et_settings_name);
        etWeight = findViewById(R.id.et_settings_weight);
        etHeight = findViewById(R.id.et_settings_height);
        tvUpdateDate = findViewById(R.id.tv_settings_lastUpdateDate);
        btnClear = findViewById(R.id.btn_settings_clear);
        btnSave = findViewById(R.id.btn_settings_save);

        updateFakeUser();
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
                    return true;
                case R.id.navigation_fitness:
                    intent = new Intent(getApplicationContext(), FitnessOverview.class);
                    startActivity(intent);
                    navigationIDTag = 2;
                    return true;
                case R.id.navigation_game_map:
                    intent = new Intent(getApplicationContext(), MapActivity.class);
                    startActivity(intent);
                    navigationIDTag = 3;
                    return true;
                case R.id.navigation_settings:
                    intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                    navigationIDTag = 4;
                    return true;
            }
            return false;
        }
    };

    private void getUser() {
        DatabaseHelper db = new DatabaseHelper(this);
        //this.user = new User(db.getUserName, db.getUserId);
    }

    private void updateFakeUser() {
        user.setHeight(75.0);
        user.setWeight(200);
        user.setLastUpdateDate(new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime()));
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
                name = etName.getText().toString();
                weight = Double.parseDouble(etWeight.getText().toString());
                height = Double.parseDouble(etHeight.getText().toString());
                tvUpdateDate.setText(new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime()));
                createHints();
            }
        });
    }

    private void updateSettings() {
        user.setLastUpdateDate(tvUpdateDate.getText().toString());
        user.setName(name);
        user.setWeight(weight);
        user.setHeight(height);
        //Add calls to update user in database
        // ...
    }


}
