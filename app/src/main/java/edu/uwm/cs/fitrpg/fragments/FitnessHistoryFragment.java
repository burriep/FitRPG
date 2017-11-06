package edu.uwm.cs.fitrpg.fragments;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.FitnessEntry;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.RVAdapter;

public class FitnessHistoryFragment extends Fragment {
//    public FitnessHistoryFragment() {
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_fitness_history, container, false);
//    }
//
//    public void onStart() {
//        super.onStart();
//    }
//}


    private DatabaseHelper db;
    ArrayList<FitnessEntry> fitnessList;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fitness_history, container, false);
        RecyclerView rvEntries = (RecyclerView) view.findViewById(R.id.rvEntries);
        db = new DatabaseHelper(getContext());

        fitnessList = db.getFitnessHistory();
        RVAdapter adapter = new RVAdapter(this.getContext(), fitnessList);

        rvEntries.setAdapter(adapter);
        rvEntries.setLayoutManager(new LinearLayoutManager(getContext()));

        return inflater.inflate(R.layout.fragment_fitness_history, container, false);
        //return inflater.inflate(R.layout.fragment_fitness_history, container, false);
    }

//    public void onStart() {
//        super.onStart();}
//
//    public void updateView() {
//        fitnessList = db.getFitnessHistory();
//
//        View fragmentView = getView();
//        ListView listView = fragmentView.findViewById(R.id.rvEntries);
//        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listView));
//    }
}
