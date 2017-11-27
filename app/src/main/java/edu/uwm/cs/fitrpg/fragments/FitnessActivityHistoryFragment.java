package edu.uwm.cs.fitrpg.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.model.FitnessActivity;
import edu.uwm.cs.fitrpg.util.FitnessActivityRecyclerViewAdapter;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fitness_activity_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            final Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            final List<FitnessActivity> items = new LinkedList<>();
            final FitnessActivityRecyclerViewAdapter adapter = new FitnessActivityRecyclerViewAdapter(items, mListener);
            recyclerView.setAdapter(adapter);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
                    final List<FitnessActivity> newItems = FitnessActivity.getAllByDate(db, startDate, endDate);
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            items.addAll(newItems);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }).run();
        }


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


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(FitnessActivity item);
    }
}
