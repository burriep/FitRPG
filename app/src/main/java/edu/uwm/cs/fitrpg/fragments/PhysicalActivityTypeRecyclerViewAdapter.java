package edu.uwm.cs.fitrpg.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.fragments.PhysicalActivityTypeFragment.OnListFragmentInteractionListener;
import edu.uwm.cs.fitrpg.model.PhysicalActivityType;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PhysicalActivityType} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class PhysicalActivityTypeRecyclerViewAdapter extends RecyclerView.Adapter<PhysicalActivityTypeRecyclerViewAdapter.ViewHolder> {

    private final List<PhysicalActivityType> mValues;
    private final OnListFragmentInteractionListener mListener;

    public PhysicalActivityTypeRecyclerViewAdapter(List<PhysicalActivityType> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_physical_activity_item, parent, false);
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
        public PhysicalActivityType mItem;

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
