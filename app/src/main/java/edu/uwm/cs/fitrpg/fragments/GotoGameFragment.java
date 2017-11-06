package edu.uwm.cs.fitrpg.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import edu.uwm.cs.fitrpg.view.TrackFitnessActivity;
import edu.uwm.cs.fitrpg.MainActivity;
import edu.uwm.cs.fitrpg.R;

public class GotoGameFragment extends Fragment{
    public GotoGameFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_goto_game, container, false);
    }

    public void onStart() {
        super.onStart();
        final View fragmentView = getView();
        ImageButton btnGame, btnFitness;
        btnGame = (ImageButton) fragmentView.findViewById(R.id.image_button_game);
        btnFitness = (ImageButton) fragmentView.findViewById(R.id.image_button_fitness);

        btnGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fragmentView.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnFitness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fragmentView.getContext(), TrackFitnessActivity.class);
                startActivity(intent);
            }
        });
    }
}
