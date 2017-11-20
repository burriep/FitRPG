package edu.uwm.cs.fitrpg.fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.view.FitnessActivity;


public class FitnessEntryFragment extends Fragment {
    public FitnessEntryFragment() {
    }

    RadioGroup rpgActivities;
    Calendar myCalendar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fitness_entry, container, false);
    }

    public void onStart() {
        super.onStart();
        View fragmentView = getView();
        int position = getArguments().getInt("Position");


        rpgActivities = (RadioGroup) fragmentView.findViewById(R.id.radioGroup_fitness_activities);
        final TextView tvActivity = fragmentView.findViewById(R.id.tv_top_title);
        final EditText etActivity = fragmentView.findViewById(R.id.et_activity_type);
        final TextView tvMiddle = fragmentView.findViewById(R.id.tv_middle_title);
        final EditText etMiddle = fragmentView.findViewById(R.id.et_middle_entry);
        final TextView tvBottom = fragmentView.findViewById(R.id.tv_bottom_title);
        final EditText etBottom = fragmentView.findViewById(R.id.et_bottom_entry);

        Button btnClear = fragmentView.findViewById(R.id.btn_fitness_entry_clear);
        Button btnSave = fragmentView.findViewById(R.id.btn_fitness_entry_save);

        myCalendar = Calendar.getInstance();

        if (position == 2) { // Record Data
            btnClear.setText("Clear");
            btnSave.setText("Save");
            //tvData.setText("Manually recorded data fragment here");

            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager.findFragmentById(R.id.ll_entry_middle) == null) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                FitnessRecordDataFragment fragment = new FitnessRecordDataFragment();
                transaction.add(R.id.ll_entry_middle, fragment);
                transaction.commit();
            }
        }

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //updateLabel(etMiddle);
            }
        };

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
            }
        };

//        if(etMiddle != null) {
//            etMiddle.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR),
//                    //    myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//                    new TimePickerDialog(getContext(), time, myCalendar.get(Calendar.HOUR),
//                            myCalendar.get(Calendar.MINUTE), true).show();
//                    updateLabel(etMiddle);
//
//                }
//            });
//        }
//
//        if(etBottom != null) {
//            etMiddle.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR),
//                    //    myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//                    new TimePickerDialog(getContext(), time, myCalendar.get(Calendar.HOUR),
//                            myCalendar.get(Calendar.MINUTE), true).show();
//                    updateLabel(etBottom);
//
//                }
//            });
//        }


        rpgActivities.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                etActivity.setText("");
                etMiddle.setText("");
                etBottom.setText("");

                if(i == R.id.rb_time_based) {
                    tvActivity.setText("Activity");
                    tvMiddle.setText("Start Time");
                    tvBottom.setText("End Time");

                    etActivity.setHint("i.e. Running");
                    etActivity.setVisibility(View.VISIBLE);
                    etMiddle.setHint("");
                    etMiddle.setVisibility(View.VISIBLE);

                    etBottom.setHint("");
                    etBottom.setVisibility(View.VISIBLE);

                    if(etMiddle != null) {
                        etMiddle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR),
                                //    myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                                new TimePickerDialog(getContext(), time, myCalendar.get(Calendar.HOUR),
                                        myCalendar.get(Calendar.MINUTE), true).show();
                                updateLabel(etMiddle);

                            }
                        });
                    }

                    if(etBottom != null) {
                        etBottom.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR),
                                //    myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                                new TimePickerDialog(getContext(), time, myCalendar.get(Calendar.HOUR),
                                        myCalendar.get(Calendar.MINUTE), true).show();
                                updateLabel(etBottom);

                            }
                        });
                    }
                }
                if(i == R.id.rb_rep_based) {
                    tvActivity.setText("Activity");
                    tvMiddle.setText("Number of Sets");
                    tvBottom.setText("Number of Reps");

                    etActivity.setHint("i.e. push-ups");
                    etActivity.setVisibility(View.VISIBLE);
                    etMiddle.setHint("");
                    etMiddle.setVisibility(View.VISIBLE);
                    etBottom.setHint("");
                    etBottom.setVisibility(View.VISIBLE);

                    if(etMiddle != null) {
                        etMiddle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                etMiddle.setText(etMiddle.getText());
                            }
                        });
                    }
                    if(etBottom != null) {
                        etBottom.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                etBottom.setText(etBottom.getText());
                            }
                        });
                    }
                }
                if(i == R.id.rb_distance_based) {
                    tvActivity.setText("Activity");
                    tvMiddle.setText("Duration");
                    tvBottom.setText("Distnace");

                    etActivity.setHint("i.e. walking");
                    etActivity.setVisibility(View.VISIBLE);
                    etMiddle.setHint("In minutes");
                    etMiddle.setVisibility(View.VISIBLE);
                    etBottom.setHint("In miles");
                    etBottom.setVisibility(View.VISIBLE);

                    if(etMiddle != null) {
                        etMiddle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                etMiddle.setText(etMiddle.getText());
                            }
                        });
                    }
                    if(etBottom != null) {
                        etBottom.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                etBottom.setText(etBottom.getText());
                            }
                        });
                    }
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etActivity.setText("");
                etMiddle.setText("");
                etBottom.setText("");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            DatabaseHelper db = new DatabaseHelper(getContext());
            @Override
            public void onClick(View view) {
                db.addTimeBasedData(etActivity.getText().toString(), etMiddle.getText().toString(), etBottom.getText().toString());
                db.close();
                etActivity.setText("");
                etMiddle.setText("");
                etBottom.setText("");
               // Toast.makeText(Fitne, "Data Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLabel(EditText et_date) {
        String myFormat = "MM/dd/yy HH:mm";
        SimpleDateFormat formatter = new SimpleDateFormat(myFormat, Locale.US);
        et_date.setText(formatter.format(myCalendar.getTime()));
    }

}
