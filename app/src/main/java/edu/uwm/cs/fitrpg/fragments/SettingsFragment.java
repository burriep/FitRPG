package edu.uwm.cs.fitrpg.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.model.User;


public class SettingsFragment extends Fragment {


    public EditText etName, etWeight, etHeight;
    public TextView tvUpdateDate;
    public Button btnSave, btnClear;
    private User user;

    private String name;
    private int weight, height;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View fragmentView = getView();

        /* Need to create DB methods and table for user data*/
        //getUser();
        user = new User("Tyler", 1);

        name = user.getName();
        weight = user.getWeight();
        height = user.getHeight();

        etName = fragmentView.findViewById(R.id.et_settings_name);
        etWeight = fragmentView.findViewById(R.id.et_settings_weight);
        etHeight = fragmentView.findViewById(R.id.et_settings_height);
        tvUpdateDate = fragmentView.findViewById(R.id.tv_settings_lastUpdateDate);
        btnClear = fragmentView.findViewById(R.id.btn_settings_clear);
        btnSave = fragmentView.findViewById(R.id.btn_settings_save);

        updateFakeUser();
        createHints();

        createListeners();
    }


    private void getUser() {
        DatabaseHelper db = new DatabaseHelper(getContext());
        //this.user = new User(db.getUserName, db.getUserId);
    }

    private void updateFakeUser() {
        user.setHeight(75);
        user.setWeight(200);
        user.setLastUpdateDate(new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime()));
    }

    private void createHints() {
        etName.setHint(user.getName());
        etWeight.setHint("" + user.getWeight());
        etHeight.setHint("" + user.getHeight());
        tvUpdateDate.setHint("" + user.getLastUpdateDate());
    }

    public void createListeners() {
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etName.setText("");
                etHeight.setText("");
                etWeight.setText("");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = etName.getText().toString();
                weight = Integer.parseInt(etWeight.getText().toString());
                height = Integer.parseInt(etHeight.getText().toString());
                tvUpdateDate.setText(new SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().getTime()));
                createHints();
            }
        });
    }

    private void updateSettings() {
        user.setLastUpdateDate(tvUpdateDate.getText().toString());
        user.setName(name);
        user.setWeight(weight);
        user.setHeight(height);
        //Add calls to update user in database
        // ...
    }

}
