package edu.uwm.cs.fitrpg.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.fragments.FitnessActivityTypeFragment.OnListFragmentInteractionListener;
import edu.uwm.cs.fitrpg.model.FitnessActivityType;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link FitnessActivityType} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class FitnessActivityTypeRecyclerViewAdapter extends RecyclerView.Adapter<FitnessActivityTypeRecyclerViewAdapter.ViewHolder> {

    private final List<FitnessActivityType> mValues;
    private final OnListFragmentInteractionListener mListener;

    public FitnessActivityTypeRecyclerViewAdapter(List<FitnessActivityType> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_fitness_activity_type, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(holder.mItem.getName());

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
        public FitnessActivityType mItem;

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
