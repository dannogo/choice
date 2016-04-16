package com.elantix.dopeapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 4/11/16.
 */
public class FragmentStartPassRecovery extends Fragment {

    private View mFragmentView;
    private FancyButton mSubmitButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_start_pass_recovery, container, false);
        mSubmitButton = (FancyButton) mFragmentView.findViewById(R.id.start_pass_recovery_submit_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SignInActivity)getActivity()).switchPageHandler(SignInActivity.StartLogin.Login);
            }
        });


        return mFragmentView;
    }
}
