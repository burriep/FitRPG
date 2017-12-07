package edu.uwm.cs.fitrpg.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.DrawableRes;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.uwm.cs.fitrpg.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TutorialHomeScreenFragment extends Fragment {

    int navPage = 0;
    ImageView image;
    TextView text;

    public TutorialHomeScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navPage = getArguments().getInt("navPage");
        return inflater.inflate(R.layout.fragment_tutorial_home_screen, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View fragmentView = getView();

        image = fragmentView.findViewById(R.id.iv_image);
        text = fragmentView.findViewById(R.id.tv_text);

        switch(navPage) {
            case -1:
                updateGeneralScreen();
                return;
            case -2:
                updateHomeScreen();
                return;
            case -3:
                updateFitnessOverview();
                return;
            case -4:
                updateFitnessTracking();
                return;
            case -5:
                updateFitnessAdding();
                return;
            case -6:
                updateMap();
                return;
            case -7:
                updateCombat();
                return;

            case 0:
                updateHomeScreen();
                return;

            case 1:
                updateFitnessOverview();
                return;
            case 2:
                updateFitnessTracking();
                return;
            case 3:
                updateFitnessAdding();
                return;
            case 4:
                updateMap();
                return;
            case 5:
                updateCombat();
                return;
            case 6:
                updateSettings();
                return;
            case 7:
                return;

            default:
                updateGeneralScreen();
                return;

        }


    }

    private void updateGeneralScreen() {
        image.setImageResource(0);
        text.setText(R.string.tutorialHome);
    }

    private void updateHomeScreen() {
        image.setImageResource(R.drawable.home_screen_image);
        text.setText(R.string.tutorialHomeScreen);
    }

    private void updateFitnessOverview() {
        image.setImageResource(R.drawable.fitness_overview_image);
        text.setText(R.string.tutorialFitnessOverview);
    }

    private void updateFitnessTracking() {
        image.setImageResource(R.drawable.fitness_tracking_image);
        text.setText(R.string.tutorialFitnessTracking);
    }

    private void updateFitnessAdding() {
        image.setImageResource(R.drawable.fitness_adding_image);
        text.setText(R.string.tutorialFitnessAdding);
    }

    private void updateMap() {
        image.setImageResource(R.drawable.map_image);
        text.setText(R.string.tutorialMap);
    }

    private void updateCombat() {
        image.setImageResource(R.drawable.combat_image);
        text.setText(R.string.tutorialCombat);
    }

    private void updateSettings() {
        image.setImageResource(R.drawable.settings_image);
        text.setText(R.string.tutorialSettings);
    }

}
