package edu.uwm.cs.fitrpg.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.GameBoard;
import edu.uwm.cs.fitrpg.MapActivity;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.RpgChar;
import edu.uwm.cs.fitrpg.model.FitnessChallengeLevel;
import edu.uwm.cs.fitrpg.model.User;
import edu.uwm.cs.fitrpg.util.NotificationHandler;
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
    private TextView stamina, speed, strength, endurance, dexterity, currentLevel;
    private ProgressBar currentLevelProgress;
    private DatabaseHelper myDB;
    private int userID = 1;
    private RpgChar character;

    private Handler updateProgressHandler = new Handler();
    private Runnable updateProgressTask = new Runnable() {
        public void run() {
            updateCurrentLevelProgress();
            updateProgressHandler.postDelayed(updateProgressTask, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        appCon = this.getApplicationContext();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener);

        currentLevelProgress = findViewById(R.id.progress_bar_current_level);
        currentLevelProgress.setAnimation(null);
        currentLevelProgress.setMax(100);
        currentLevelProgress.setProgress(0);

        currentLevel = findViewById(R.id.text_view_current_level);
        currentLevel.setText("0");

        stamina = findViewById(R.id.home_stamina);
        speed = findViewById(R.id.home_speed);
        strength = findViewById(R.id.home_strength);
        endurance = findViewById(R.id.home_endurance);
        dexterity = findViewById(R.id.home_dexterity);

        // check if this is the first time the user launched the app
        new Thread(new Runnable() {
            @Override
            public void run() {
                myDB = new DatabaseHelper(Home.this);
                SQLiteDatabase db = myDB.getReadableDatabase();
                character = RpgChar.get(db, 1);
                db.close();
                if (character == null) {
                    firstRunInitDatabase();
                    Home.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            firstRunInitUserInterface();
                        }
                    });
                } else {
                    Home.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initUserInterface();
                        }
                    });
                }
            }
        }).run();
    }

    private void firstRunInitDatabase() {
        // setup
        SQLiteDatabase db = myDB.getWritableDatabase();
        // character
        character = new RpgChar();
        character.setId(1);
        character.setName("Defaultio");
        character.setStamina(10);
        character.setSpeed(10);
        character.setStrength(10);
        character.setDexterity(10);
        character.setEndurance(10);
        character.setLoopCount(1);
        character.create(db);
        // fitness challenge levels
        FitnessChallengeLevel.initUser(db, userID);
        // user
        User user = new User();
        user.setName("");
        Date d = Calendar.getInstance().getTime();
        user.setLastUpdateDate(d);
        user.setFitnessReminderDate(d);
        user.create(db);
        // cleanup
        db.close();
    }

    private void firstRunInitUserInterface() {
        initUserInterface();
        showNewUserDialog();
        NotificationHandler.createNotificationChannels(this);
    }

    @Override
    protected void onPause() {
        updateProgressHandler.removeCallbacks(updateProgressTask);
        super.onPause();
    }

    private void initUserInterface() {
        stamina.setText(Integer.toString(character.getStamina()));
        speed.setText(Integer.toString(character.getSpeed()));
        strength.setText(Integer.toString(character.getStrength()));
        endurance.setText(Integer.toString(character.getEndurance()));
        dexterity.setText(Integer.toString(character.getDexterity()));
        currentLevel.setText(Integer.toString(character.getLoopCount()));

        updateCurrentLevelProgress();
        updateProgressHandler.removeCallbacks(updateProgressTask);
        updateProgressHandler.postDelayed(updateProgressTask, 1000);
    }

    private void updateCurrentLevelProgress() {
        GameBoard board = new GameBoard();
        int numberOfNodes = board.getNumNodes();
        int id = board.getmapId();
        int loop = board.getLoopCount();

        currentLevel.setText("" + id + 1);
        currentLevelProgress.setMax(numberOfNodes);
        currentLevelProgress.setProgress(loop);
    }

    private void showNewUserDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
        final EditText etName = alertLayout.findViewById(R.id.diaglog_name);
        final EditText etWeight = alertLayout.findViewById(R.id.diaglog_weight);
        final EditText etHeight = alertLayout.findViewById(R.id.diaglog_height);

        AlertDialog.Builder alert = new AlertDialog.Builder(Home.this);
        alert.setTitle("Create User");
        alert.setView(alertLayout);
        alert.setCancelable(false);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SQLiteDatabase db = new DatabaseHelper(Home.this).getWritableDatabase();
                        User user = User.get(db, 1);
                        String name = etName.getText().toString();
                        if(name.length() == 0)
                            name = "User";
                        user.setName(name);
                        user.setWeight(Utils.getIntegerField(etWeight, 0));
                        user.setHeight(Utils.getIntegerField(etHeight, 0));
                        user.setLastUpdateDate(Calendar.getInstance().getTime());
                        user.update(db);
                        db.close();
                        Home.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "User created", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).run();
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateProgressHandler.removeCallbacks(updateProgressTask);
        updateProgressHandler.postDelayed(updateProgressTask, 1000);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
