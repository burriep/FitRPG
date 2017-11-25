package edu.uwm.cs.fitrpg.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.MapActivity;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.fragments.CurrentLevelFragment;

public class HomeScreen extends AppCompatActivity{

    ImageButton ibGame, ibFitness, ibGraphics;
    public static Context appCon;

    private int userID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        appCon = this.getApplicationContext();

        TextView stamina, speed, strength, endurance, dexterity;
        stamina = (TextView) findViewById(R.id.tv_stamina);
        speed = (TextView) findViewById(R.id.tv_speed);
        strength = (TextView) findViewById(R.id.tv_strength);
        endurance = (TextView) findViewById(R.id.tv_endurance);
        dexterity = (TextView) findViewById(R.id.tv_dexterity);
        DatabaseHelper myDB = new DatabaseHelper(this);

        ibGame = (ImageButton) findViewById(R.id.btn_go_to_game);
        ibFitness = (ImageButton) findViewById(R.id.btn_go_to_fitness);
        ibGraphics = (ImageButton) findViewById(R.id.btn_go_to_graphics);

        ibGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoGame();
            }
        });

        ibFitness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoFitness();
            }
        });

        ibGraphics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoGraphics();
            }
        });

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

    public void gotoGame() {
        //Intent intent = new Intent(this, GameActivity.class);
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void gotoFitness() {
        Intent intent = new Intent(this, FitnessActivity.class);
        startActivity(intent);
    }

    public void gotoGraphics() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

}
