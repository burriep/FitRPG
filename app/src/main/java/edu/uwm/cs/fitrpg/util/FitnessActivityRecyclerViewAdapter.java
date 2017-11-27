package edu.uwm.cs.fitrpg.util;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.fragments.FitnessActivityHistoryFragment.OnListFragmentInteractionListener;
import edu.uwm.cs.fitrpg.fragments.FitnessHistoryDataFragment;
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
        Context context = parent.getContext();
        return new ViewHolder(context, view);
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



    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        public final View mView;
        public final TextView mContentView;
        public FitnessActivity mItem;
        private Context context;

        public ViewHolder(Context context, View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            if(pos != RecyclerView.NO_POSITION) {
                FitnessActivity fitnessActivity = mValues.get(pos);
                Toast.makeText(context, fitnessActivity.getAccountId(), Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
