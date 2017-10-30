package edu.uwm.cs.fitrpg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


/*
Right now I am using the MainActivity class to be my test activity for buttons, etc.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frDB = new DatabaseHelper(this);
    }
}
