package edu.uwm.cs.fitrpg.activity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.MapActivity;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.model.User;
import edu.uwm.cs.fitrpg.util.NotificationHandler;
import edu.uwm.cs.fitrpg.util.Utils;

import static edu.uwm.cs.fitrpg.util.Utils.ISO_DATE_TIME_FORMAT;

public class SettingsActivity extends AppCompatActivity {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.ENGLISH);
    private EditText etName, etWeight, etHeight, etReminderTime;
    private CheckBox cbReminder;
    private TextView tvUpdateDate, tutorial, aboutMe;
    private Button btnSave, btnReset;
    private Calendar reminderCalendar;
    public BottomNavigationView.OnNavigationItemSelectedListener OnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(getApplicationContext(), Home.class);
                    startActivity(intent);
                    finish();
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
                    return true;
            }
            return false;
        }
    };
    private User user;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener);

        navigation.getMenu().getItem(3).setChecked(true);

        etName = findViewById(R.id.settings_name);
        etWeight = findViewById(R.id.settings_weight);
        etHeight = findViewById(R.id.settings_height);
        tvUpdateDate = findViewById(R.id.settings_last_updated);
        btnReset = findViewById(R.id.btn_settings_reset);
        btnSave = findViewById(R.id.btn_settings_save);
        tutorial = findViewById(R.id.tutorial);
        aboutMe = findViewById(R.id.about_me);
        cbReminder = findViewById(R.id.settings_reminders);
        etReminderTime = findViewById(R.id.settings_reminders_time);

        reminderCalendar = Calendar.getInstance();
        new Thread(new Runnable() {
            @Override
            public void run() {
                dbHelper = new DatabaseHelper(SettingsActivity.this);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                user = User.get(db, 1);
                SettingsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initUserInterface();
                    }
                });
            }
        }).run();

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

    private void initUserInterface() {
        resetSettings();
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetSettings();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSettings();
            }
        });

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                reminderCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                reminderCalendar.set(Calendar.MINUTE, minute);
                updateReminderTimeField();
            }
        };

        etReminderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(SettingsActivity.this, time, reminderCalendar.get(Calendar.HOUR_OF_DAY), reminderCalendar.get(Calendar.MINUTE), false).show();
            }
        });
    }

    public void saveSettings() {
        user.setLastUpdateDate(Calendar.getInstance().getTime());
        user.setName(etName.getText().toString());
        user.setWeight(Utils.getIntegerField(etWeight, 0));
        user.setHeight(Utils.getIntegerField(etHeight, 0));
        user.setFitnessReminderDate(reminderCalendar.getTime());
        user.setFitnessReminders(cbReminder.isChecked());

        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                user.update(db);
                db.close();
                tvUpdateDate.post(new Runnable() {
                    @Override
                    public void run() {
                        tvUpdateDate.setText(DATE_FORMAT.format(user.getLastUpdateDate()));
                        Context context = SettingsActivity.this;
                        if (user.isFitnessReminders()) {
                            NotificationHandler.setEnabledState(context,true);
                            NotificationHandler.setReminder(context, reminderCalendar.getTime());
                        } else {
                            NotificationHandler.setEnabledState(context,false);
                            NotificationHandler.cancelReminder(context);
                        }
                        Toast.makeText(getApplicationContext(), "Updated user", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).run();
    }

    private void updateReminderTimeField() {
        etReminderTime.setText(new SimpleDateFormat("h:mm a", Locale.US).format(reminderCalendar.getTime()));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
        finish();
    }

    private void resetSettings() {
        etName.setText(user.getName());
        etWeight.setText(Integer.toString(user.getWeight()));
        etHeight.setText(Integer.toString(user.getHeight()));
        tvUpdateDate.setText("Last updated: " + new SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.ENGLISH).format(user.getLastUpdateDate()));

        cbReminder.setChecked(user.isFitnessReminders());
        reminderCalendar.setTime(user.getFitnessReminderDate());
        updateReminderTimeField();
    }
}
