package edu.uwm.cs.fitrpg.fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.activity.FitnessOverview;
import edu.uwm.cs.fitrpg.model.FitnessActivity;
import edu.uwm.cs.fitrpg.util.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FitnessEntryTimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FitnessEntryTimeFragment extends Fragment {

    private EditText dateField, timeField, durationHourField, durationMinuteField, durationSecondField;
    private Calendar calendar;
    private Date date;

    public FitnessEntryTimeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FitnessEntryTimeFragment.
     */
    public static FitnessEntryTimeFragment newInstance(String param1, String param2) {
        FitnessEntryTimeFragment fragment = new FitnessEntryTimeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fitness_entry_time, container, false);
        dateField = view.findViewById(R.id.activity_entry_date);
        timeField = view.findViewById(R.id.activity_entry_time);
        durationHourField = view.findViewById(R.id.activity_entry_duration_hours);
        durationMinuteField = view.findViewById(R.id.activity_entry_duration_minutes);
        durationSecondField = view.findViewById(R.id.activity_entry_duration_seconds);

        calendar = Calendar.getInstance();

        date = ((FitnessOverview) getActivity()).getSelectedDate();

        calendar.setTime(date);

        updateDateField();
        updateTimeField();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateField();
            }
        };

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                updateTimeField();
            }
        };

        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        timeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(), time, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        });

        return view;
    }

    private void updateDateField() {
        dateField.setText(new SimpleDateFormat("M/d/yyyy", Locale.US).format(calendar.getTime()));
    }

    private void updateTimeField() {
        timeField.setText(new SimpleDateFormat("h:mm a", Locale.US).format(calendar.getTime()));
    }

    public void getData(FitnessActivity activity) {
        int durationHour = Utils.getIntegerField(durationHourField, 0);
        int durationMinute = Utils.getIntegerField(durationMinuteField, 0);
        int durationSecond = Utils.getIntegerField(durationSecondField, 0);

        Calendar stopCalendar = Calendar.getInstance();
        stopCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        stopCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        stopCalendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        stopCalendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + durationHour);
        stopCalendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + durationMinute);
        stopCalendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + durationSecond);

        activity.setStartDate(calendar.getTime());
        activity.setStopDate(stopCalendar.getTime());
        activity.setDuration((durationHour * 60 * 60 + durationMinute * 60 + durationSecond) * 1000);
    }
}
