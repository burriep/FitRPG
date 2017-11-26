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

import java.util.LinkedList;
import java.util.List;

import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.model.FitnessActivityType;
import edu.uwm.cs.fitrpg.util.FitnessActivityTypeRecyclerViewAdapter;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FitnessActivityTypeFragment extends Fragment {
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FitnessActivityTypeFragment() {
    }

    @SuppressWarnings("unused")
    public static FitnessActivityTypeFragment newInstance() {
        FitnessActivityTypeFragment fragment = new FitnessActivityTypeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fitness_activity_type_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            final Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            final List<FitnessActivityType> items = new LinkedList<>();
            final FitnessActivityTypeRecyclerViewAdapter adapter = new FitnessActivityTypeRecyclerViewAdapter(items, mListener);
            recyclerView.setAdapter(adapter);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    SQLiteDatabase db = new DatabaseHelper(recyclerView.getContext()).getReadableDatabase();
                    final List<FitnessActivityType> newItems = FitnessActivityType.getAll(db);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(FitnessActivityType item);
    }
}
