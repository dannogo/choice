package com.elantix.dopeapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by oleh on 3/18/16.
 */
public class FragmentFriendsNotRegistered extends Fragment {

    View fragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_notregistered, container, false);

        return fragmentView;
    }
}
