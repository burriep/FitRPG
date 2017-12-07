package edu.uwm.cs.fitrpg.fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import edu.uwm.cs.fitrpg.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TutorialHomeFragment extends Fragment {


    public TutorialHomeFragment() {
        // Required empty public constructor
    }


    TextView tvHomeScreen, tvFitness, tvMap, tvCombat, tvSettings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_tutorial_home, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        View fragmentView = getView();
        tvHomeScreen = fragmentView.findViewById(R.id.tv_tutorial_home_screen);
        tvHomeScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                if(fragmentManager.findFragmentById(R.id.tutorial_fragment) == null) {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    TutorialHomeFragment fragment = new TutorialHomeFragment();
                    transaction.replace(R.id.tutorial_fragment, fragment);
                    transaction.commit();
                }
            }
        });
        tvFitness = fragmentView.findViewById(R.id.tv_tutorial_fitness);
        tvMap = fragmentView.findViewById(R.id.tv_tutorial_map);
        tvCombat = fragmentView.findViewById(R.id.tv_tutorial_combat);
        tvSettings = fragmentView.findViewById(R.id.tv_tutorial_settings);



    }




}
