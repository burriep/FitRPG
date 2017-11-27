package edu.uwm.cs.fitrpg;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import edu.uwm.cs.fitrpg.fragments.FitnessFragment;
import edu.uwm.cs.fitrpg.fragments.HomeFragment;
import edu.uwm.cs.fitrpg.fragments.MapFragment;
import edu.uwm.cs.fitrpg.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity{
    private int navigationIDTag = 0;
    private Context context;

    private BottomNavigationView.OnNavigationItemSelectedListener OnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    HomeFragment fragment = new HomeFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment,"FragmentName");
                    fragmentTransaction.commit();
                    navigationIDTag = 1;
                    return true;
                case R.id.navigation_fitness:
                    FitnessFragment fragment2 = new FitnessFragment();
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.fragment_container, fragment2,"FragmentName");
                    fragmentTransaction2.commit();
                    navigationIDTag = 2;
                    return true;
                case R.id.navigation_game_map:
                    gotoGame();
//                    MapFragment fragment3 = new MapFragment();
//                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction3.replace(R.id.fragment_container, fragment3,"FragmentName");
//                    fragmentTransaction3.commit();
                    navigationIDTag = 3;
                    return true;
                case R.id.navigation_settings:
                    SettingsFragment fragment4 = new SettingsFragment();
                    FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction4.replace(R.id.fragment_container, fragment4,"FragmentName");
                    fragmentTransaction4.commit();
                    navigationIDTag = 4;
                    return true;
            }
            return false;
        }
    };

    public void gotoGame() {
        Intent intent = new Intent(context, MapActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        HomeFragment fragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment,"FragmentName");
        fragmentTransaction.commit();

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener);
    }

    public int getNavigationIDTag() {
        return navigationIDTag;
    }

}
