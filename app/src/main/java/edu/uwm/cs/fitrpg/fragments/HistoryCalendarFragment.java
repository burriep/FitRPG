package edu.uwm.cs.fitrpg.fragments;


import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executors;

import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.util.EventDecorator;
import edu.uwm.cs.fitrpg.util.OneDayDecorator;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.activity.FitnessOverview;
import edu.uwm.cs.fitrpg.model.FitnessActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryCalendarFragment extends Fragment implements OnDateSelectedListener {

    private final int color = Color.parseColor("#228BC34A");
    private CalendarDay date;
    private HashSet<CalendarDay> dates = new HashSet<CalendarDay>();
    public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public HistoryCalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar_history, container, false);
    }

    MaterialCalendarView widget;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    @Override
    public void onStart() {
        super.onStart();
        ((FitnessOverview) getActivity()).eraseFitnessDate();
        View fragmentView = getView();
        initDates();
        widget = fragmentView.findViewById(R.id.calendarView);

        widget.setOnDateChangedListener(this);
        widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);

        Calendar instance = Calendar.getInstance();
        widget.setSelectedDate(instance.getTime());
        ((FitnessOverview) getActivity()).setSelectedDate(instance.getTime());

        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR -1), Calendar.JANUARY, 1);

        Calendar instance2 = Calendar.getInstance();
        instance2.set(instance2.get(Calendar.YEAR), Calendar.DECEMBER, 31);

        widget.state().edit()
                .setMinimumDate(instance1.getTime())
                .setMaximumDate(instance2.getTime())
                .commit();

        widget.addDecorator(new EventDecorator(Color.RED, dates));


        new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        oneDayDecorator.setDate(date.getDate());
        widget.invalidateDecorators();


        Date now = date.getCalendar().getTime();
        Calendar calendar = date.getCalendar();
        calendar.add(Calendar.DATE, 1);
        Date date2 = calendar.getTime();

        ((FitnessOverview) getActivity()).setSelectedDate(now);

        FitnessActivityHistoryFragment fragment = FitnessActivityHistoryFragment.newInstance(now, date2);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fitness_frag_data, fragment);
        transaction.commit();
    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -2);
            ArrayList<CalendarDay> dates = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);
                calendar.add(Calendar.DATE, 5);
            }

            return dates;
        }
    }


    private void initDates() {
        SQLiteDatabase db =  new DatabaseHelper(getContext()).getReadableDatabase();
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();

        calendar.add(Calendar.MONTH, -6);
        Date yesterday = calendar.getTime();
        List<FitnessActivity> faList = FitnessActivity.getAllByDate(db, 1, yesterday, now);
        for(FitnessActivity fa: faList) {
            dates.add(CalendarDay.from(fa.getStartDate()));
        }
    }



}
