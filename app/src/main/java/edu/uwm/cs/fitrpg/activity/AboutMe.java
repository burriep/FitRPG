package edu.uwm.cs.fitrpg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.uwm.cs.fitrpg.R;

/**
 * Created by Admin on 11/30/2017.
 */

public class AboutMe extends AppCompatActivity{

    TextView tvAboutMe, tvTitle,tvVersion;
    Button btnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(R.string.app_name);

        tvVersion = findViewById(R.id.tv_version);
        tvVersion.setText(R.string.appVersion);

        tvAboutMe = findViewById(R.id.tv_aboutMe);
        tvAboutMe.setText(R.string.aboutMe);

        btnOK = findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

}
