package edu.uwm.cs.fitrpg.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.fragments.TutorialHomeFragment;
import edu.uwm.cs.fitrpg.fragments.TutorialHomeScreenFragment;

public class TutorialActivity extends AppCompatActivity{

    LinearLayout info;
    Button previous, next, home;
    int navPage; // 0-TutorialHome, 1-General, 2-HomeScreen, 3-Map, 4-Combat, 5-Settings

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        navPage = 0;

        info = findViewById(R.id.tutorial_fragment);
        previous = findViewById(R.id.btn_tutorial_previous);
        home = findViewById(R.id.btn_tutorial_home);
        next = findViewById(R.id.btn_tutorial_next);



        final FragmentManager fragmentManager = getFragmentManager();

        if(fragmentManager.findFragmentById(R.id.tutorial_fragment) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            TutorialHomeScreenFragment fragment = new TutorialHomeScreenFragment();
            transaction.add(R.id.tutorial_fragment, fragment);
            Bundle b = new Bundle();
            b.putInt("navPage", 10);
            transaction.commit();
            fragment.setArguments(b);
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(navPage == 7) {
                    finish();
                }
                else {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    TutorialHomeScreenFragment fragment = new TutorialHomeScreenFragment();
                    transaction.replace(R.id.tutorial_fragment, fragment);

                    Bundle b = new Bundle();
                    b.putInt("navPage", navPage++);
                    transaction.detach(fragment);
                    transaction.attach(fragment);
                    transaction.commit();
                    fragment.setArguments(b);
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(navPage == 0) {
                    finish();
                }
                else {
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    TutorialHomeScreenFragment fragment = new TutorialHomeScreenFragment();
                    transaction.replace(R.id.tutorial_fragment, fragment);

                    Bundle b = new Bundle();
                    b.putInt("navPage", -1*navPage--);
                    transaction.detach(fragment);
                    transaction.attach(fragment);
                    transaction.commit();
                    fragment.setArguments(b);
                    //navPage--;
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                TutorialHomeScreenFragment fragment = new TutorialHomeScreenFragment();
                transaction.replace(R.id.tutorial_fragment, fragment);

                Bundle b = new Bundle();
                b.putInt("navPage", 10);
                navPage = 0;
                transaction.detach(fragment);
                transaction.attach(fragment);
                transaction.commit();
                fragment.setArguments(b);
            }
        });
    }
}
