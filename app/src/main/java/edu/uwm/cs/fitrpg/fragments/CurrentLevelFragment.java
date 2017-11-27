package edu.uwm.cs.fitrpg.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import edu.uwm.cs.fitrpg.R;

public class CurrentLevelFragment extends Fragment {
    public CurrentLevelFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_current_level, container, false);
    }

    public void onStart() {
        super.onStart();
        View fragmentView = getView();
        ProgressBar currentLevelProgress = fragmentView.findViewById(R.id.progress_bar_current_level);
        TextView currentLevel = fragmentView.findViewById(R.id.text_view_current_level);


        //Should call information from level and map class or database
        //ToDo
        currentLevel.setText("01");
        currentLevelProgress.setAnimation(null);
        currentLevelProgress.setMax(100);
        currentLevelProgress.setProgress(5);
    }

//    private int getLevel() {
//
//    }
//
//    private int getLevelProgress() {
//
//    }
//
//    private void setProgressBar() {
//
//    }
}
