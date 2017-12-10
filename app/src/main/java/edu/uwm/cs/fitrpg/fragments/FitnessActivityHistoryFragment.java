package edu.uwm.cs.fitrpg.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.model.FitnessActivity;
import edu.uwm.cs.fitrpg.util.FitnessActivityRecyclerViewAdapter;
import edu.uwm.cs.fitrpg.util.Utils;

import static edu.uwm.cs.fitrpg.util.Utils.ISO_DATE_FORMAT;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FitnessActivityHistoryFragment extends Fragment {
    private static final String ARG_START_DATE = "start-date";
    private static final String ARG_END_DATE = "end-date";
    private Date startDate, endDate;

    private TextView dateText, durationText, distanceText, repsText;

    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FitnessActivityHistoryFragment() {
    }

    @SuppressWarnings("unused")
    public static FitnessActivityHistoryFragment newInstance(Date startDate, Date endDate) {
        FitnessActivityHistoryFragment fragment = new FitnessActivityHistoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_START_DATE, startDate);
        args.putSerializable(ARG_END_DATE, endDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            startDate = (Date) getArguments().getSerializable(ARG_START_DATE);
            endDate = (Date) getArguments().getSerializable(ARG_END_DATE);
        }
    }

    public void setFitnessStats(List<FitnessActivity> activities) {
        int duration = 0, reps = 0;
        double distance = 0;
        if (activities != null && !activities.isEmpty()) {
            for (FitnessActivity activity : activities) {
                duration += activity.getDuration();
                distance += activity.getDistance();
                reps += activity.getSets() * activity.getRepetitions();
            }
        }
        String formattedDistance;
        if (distance > 1000) {
            formattedDistance = String.format(Locale.ENGLISH, "%.2f km", (distance / 1000));
        } else {
            formattedDistance = String.format(Locale.ENGLISH, "%.2f m", distance);
        }
        dateText.setText(new SimpleDateFormat(ISO_DATE_FORMAT, Locale.ENGLISH).format(startDate));
        durationText.setText(String.format(Locale.ENGLISH, "Active Time: %s", Utils.formatDuration(duration)));
        distanceText.setText(String.format(Locale.ENGLISH, "Distance: %s", formattedDistance));
        repsText.setText(String.format(Locale.ENGLISH, "Reps: %d", reps));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fitness_activity_list, container, false);

        dateText = view.findViewById(R.id.activity_stats_date);
        durationText = view.findViewById(R.id.activity_stats_duration);
        distanceText = view.findViewById(R.id.activity_stats_distance);
        repsText = view.findViewById(R.id.activity_stats_reps);

        // Set the adapter
        final Context context = view.getContext();
        final RecyclerView recyclerView = view.findViewById(R.id.fitness_activity_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        final List<FitnessActivity> items = new LinkedList<>();
        final FitnessActivityRecyclerViewAdapter adapter = new FitnessActivityRecyclerViewAdapter(items, mListener);
        recyclerView.setAdapter(adapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
                final List<FitnessActivity> newItems = FitnessActivity.getAllByDate(db, 1, startDate, endDate);
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        items.addAll(newItems);
                        setFitnessStats(newItems);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).run();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public String toString() {
        return "";
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(FitnessActivity item);
    }
}
