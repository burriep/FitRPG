package edu.uwm.cs.fitrpg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.MapActivity;
import edu.uwm.cs.fitrpg.R;


/*
Right now I am using the MainActivity class to be my test activity for buttons, etc.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper myDB = new DatabaseHelper(this);
    }

    public void launchMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
}
