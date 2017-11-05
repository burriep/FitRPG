package edu.uwm.cs.fitrpg.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.R;


/*
Right now I am using the MainActivity class to be my test activity for buttons, etc.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper frDB = new DatabaseHelper(this);
    }
}
