package com.elantix.dopeapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 3/18/16.
 */
public class FragmentFriendsNotRegistered extends Fragment implements View.OnClickListener{

    View fragmentView;
    FancyButton mCreateUsernameBtn;
    FancyButton mAlreadyUserBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_notregistered, container, false);
        mCreateUsernameBtn = (FancyButton) fragmentView.findViewById(R.id.not_registered_create_username);
        mAlreadyUserBtn = (FancyButton) fragmentView.findViewById(R.id.not_registered_already_a_user);
        mCreateUsernameBtn.setOnClickListener(this);
        mAlreadyUserBtn.setOnClickListener(this);


        return fragmentView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent(getActivity(), AuthActivity.class);
        if (id == mCreateUsernameBtn.getId()){
            intent.putExtra("fragment", "SetUsername");
        }else if(id == mAlreadyUserBtn.getId()){
            intent.putExtra("fragment", "SignInDope");
        }
        getActivity().startActivityForResult(intent, Utilities.SIGN_IN_UP);
    }

}
