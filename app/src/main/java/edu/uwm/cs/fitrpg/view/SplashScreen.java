package edu.uwm.cs.fitrpg.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.view.HomeScreen;

public class SplashScreen extends AppCompatActivity {
    private ProgressBar mProgress;
    LinearLayout linearLayout1, linearLayout2;
    Animation uptodown, downtoup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mProgress = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);

        linearLayout1 = (LinearLayout) findViewById(R.id.linearlayout1);
        linearLayout2 = (LinearLayout) findViewById(R.id.linearlayout2);
        uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this, R.anim.downtoup);
        linearLayout1.setAnimation(uptodown);
        linearLayout2.setAnimation(downtoup);

        new Thread(new Runnable() {
            public void run() {
                doProgress();
                startApp();
                finish();
            }
        }).start();
    }

    private void doProgress() {
        for (int progress = 0; progress < 100; progress += 10) {
            try {
                Thread.sleep(10);
                mProgress.setProgress(progress);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void startApp() {
        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
        startActivity(intent);
    }
}

