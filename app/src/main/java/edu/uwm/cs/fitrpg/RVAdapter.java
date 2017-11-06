package edu.uwm.cs.fitrpg;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tcBre on 11/6/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView activityType;
        public TextView activityInfo;

        public ViewHolder(View itemView) {
            super(itemView);

            activityType = (TextView) itemView.findViewById(R.id.tv_activity_type);
            activityInfo = (TextView) itemView.findViewById(R.id.tv_info);


        }
    }

    private List<FitnessEntry> myEntries;
    private Context myContext;

    public RVAdapter(Context context, List<FitnessEntry> entries) {
        myEntries = entries;
        myContext = context;
    }

    private Context getContxt() {
        return myContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View fitnessView = inflater.inflate(R.layout.list_layout,parent, false);

        ViewHolder viewHolder = new ViewHolder(fitnessView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FitnessEntry entry = myEntries.get(position);

        TextView textView1 = holder.activityType;
        textView1.setText(entry.getFitnessType());
        TextView  textView2 = holder.activityInfo;
        textView2.setText(entry.getStartTime());
    }

    @Override
    public int getItemCount() {
        return myEntries.size();
    }

}
