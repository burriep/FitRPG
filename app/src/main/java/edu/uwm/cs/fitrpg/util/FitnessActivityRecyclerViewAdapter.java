package edu.uwm.cs.fitrpg.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.fragments.FitnessActivityHistoryFragment.OnListFragmentInteractionListener;
import edu.uwm.cs.fitrpg.model.FitnessActivity;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link FitnessActivity} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class FitnessActivityRecyclerViewAdapter extends RecyclerView.Adapter<FitnessActivityRecyclerViewAdapter.ViewHolder> {

    private final List<FitnessActivity> mValues;
    private final OnListFragmentInteractionListener mListener;

    public FitnessActivityRecyclerViewAdapter(List<FitnessActivity> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_fitness_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(holder.mItem.getType().getName() + "\n" + holder.mItem.getStartDate().toString());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public FitnessActivity mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
