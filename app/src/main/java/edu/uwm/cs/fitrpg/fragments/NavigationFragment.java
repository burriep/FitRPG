package edu.uwm.cs.fitrpg.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Set;

import edu.uwm.cs.fitrpg.MapActivity;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.activity.FitnessActivityTracking;
import edu.uwm.cs.fitrpg.activity.FitnessOverview;
import edu.uwm.cs.fitrpg.activity.Home;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationFragment extends Fragment{
    private int navigationIDTag = 0;
    private Context context;

    public NavigationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        BottomNavigationView.OnNavigationItemSelectedListener OnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        intent = new Intent(getActivity(), Home.class);
                        startActivity(intent);
                        navigationIDTag = 1;
                        return true;
                    case R.id.navigation_fitness:
                        intent = new Intent(getActivity(), FitnessOverview.class);
                        startActivity(intent);
                        navigationIDTag = 2;
                        return true;
                    case R.id.navigation_game_map:
                        intent = new Intent(getActivity(), MapActivity.class);
                        startActivity(intent);
                        navigationIDTag = 3;
                        return true;
                    case R.id.navigation_settings:
                        intent = new Intent(getActivity(), SettingsFragment.class);
                        startActivity(intent);
                        navigationIDTag = 4;
                        return true;
                }
                return false;
            }
        };
    }
//
//    public BottomNavigationView.OnNavigationItemSelectedListener OnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            Intent intent;
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    intent = new Intent(getActivity(), Home.class);
//                    startActivity(intent);
//                    navigationIDTag = 1;
//                    return true;
//                case R.id.navigation_fitness:
//                    intent = new Intent(getActivity(), FitnessOverview.class);
//                    startActivity(intent);
//                    navigationIDTag = 2;
//                    return true;
//                case R.id.navigation_game_map:
//                    intent = new Intent(getActivity(), MapActivity.class);
//                    startActivity(intent);
//                    navigationIDTag = 3;
//                    return true;
//                case R.id.navigation_settings:
//                    intent = new Intent(getActivity(), SettingsFragment.class);
//                    startActivity(intent);
//                    navigationIDTag = 4;
//                    return true;
//            }
//            return false;
//        }
//    };
}
