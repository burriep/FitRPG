package edu.uwm.cs.fitrpg.fragments;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uwm.cs.fitrpg.R;

public class AvatarControlFragment extends Fragment{
    public AvatarControlFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_avatar, container, false);
    }
}
