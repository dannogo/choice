package com.elantix.dopeapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by oleh on 4/12/16.
 */
public class FragmentAuthSetUsername extends Fragment {
    private View mFragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_auth_set_username, container, false);
        mFragmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AuthActivity)getActivity()).switchPageHandler(AuthActivity.AuthPage.EnterPassword);
            }
        });
        return mFragmentView;
    }
}
