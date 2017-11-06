package edu.uwm.cs.fitrpg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by tcBre on 11/6/2017.
 */

public class HistoryList extends AppCompatActivity {
//    ArrayList<FitnessEntry> entries;
//    DatabaseHelper db = new DatabaseHelper(this);
//
//    private DatabaseHelper db;
//    ArrayList<FitnessEntry> fitnessList;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        RecyclerView rvEntries = (RecyclerView) findViewById(R.id.rvEntries);
//        db = new DatabaseHelper(this);
//
//        fitnessList = db.getFitnessHistory();
//        RVAdapter adapter = new RVAdapter(this, fitnessList);
//
//        rvEntries.setAdapter(adapter);
//        rvEntries.setLayoutManager(new LinearLayoutManager(this));
//    }

    private DatabaseHelper db;
    ArrayList<FitnessEntry> list;
    int id = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this);
        list = db.getFitnessHistory();
        updateView();
    }

    public void updateView() {
        RelativeLayout  layout= new RelativeLayout(this);
        ListView listView = new ListView(this);

        layout.addView(listView);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list));
        setContentView(layout);
    }

}
