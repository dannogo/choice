package com.elantix.dopeapp;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by oleh on 4/12/16.
 */
public class FragmentAuthEnterPassword extends Fragment {
    private View mFragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_auth_enter_password, container, false);
        mFragmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AuthActivity)getActivity()).switchPageHandler(AuthActivity.AuthPage.ConnectAccount);
            }
        });

        EditText field1 = (EditText) mFragmentView.findViewById(R.id.auth_enter_pass_field_1);
        field1.setTypeface(Typeface.DEFAULT);
        field1.setTransformationMethod(new PasswordTransformationMethod());
        EditText field2 = (EditText) mFragmentView.findViewById(R.id.auth_enter_pass_field_2);
        field2.setTypeface(Typeface.DEFAULT);
        field2.setTransformationMethod(new PasswordTransformationMethod());

        return mFragmentView;
    }
}
