package edu.uwm.cs.fitrpg.util;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.uwm.cs.fitrpg.model.FitnessActivityType;

public class FitnessActivityTypeAdapter extends BaseAdapter {
    List<FitnessActivityType> activityTypes;
    private Fragment fragment;

    public FitnessActivityTypeAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public FitnessActivityTypeAdapter(Fragment fragment, List<FitnessActivityType> types) {
        this.fragment = fragment;
        activityTypes = types;
    }

    @Override
    public int getCount() {
        return activityTypes.size();
    }

    @Override
    public String getItem(int position) {
        return activityTypes.get(position).getName();
    }

    @Override
    public long getItemId(int position) {
        return activityTypes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = fragment.getLayoutInflater().inflate(android.R.layout.simple_spinner_dropdown_item, container, false);
        }
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(getItem(position));
        return convertView;
    }
}
