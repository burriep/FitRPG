package edu.uwm.cs.fitrpg.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.R;


public class HomeFragment extends Fragment {

    public static Context appCon;
    private int userID = 1;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        return inflater.inflate(R.layout.activity_home_screen, container, false);
    }

    private Context context;
    @Override
    public void onStart() {
        super.onStart();
        View fragmentView = getView();


        TextView stamina, speed, strength, endurance, dexterity;
        stamina = (TextView) fragmentView.findViewById(R.id.tv_stamina);
        speed = (TextView) fragmentView.findViewById(R.id.tv_speed);
        strength = (TextView) fragmentView.findViewById(R.id.tv_strength);
        endurance = (TextView) fragmentView.findViewById(R.id.tv_endurance);
        dexterity = (TextView) fragmentView.findViewById(R.id.tv_dexterity);
        DatabaseHelper myDB = new DatabaseHelper(context);

        //PS Check database for a user with ID 0, otherwise create one
        if(myDB.getStamina(userID) == "-1") {
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

//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        CurrentLevelFragment fragment = new CurrentLevelFragment();
//        transaction.add(R.id.ll_top_left, fragment, "FragmentName");
//        transaction.commit();

    }

}
