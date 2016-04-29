package com.elantix.dopeapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 4/12/16.
 */
public class FragmentAuthLinkEmail extends Fragment{
    private View mFragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_auth_link_with_email, container, false);
        final EditText emailField = (EditText) mFragmentView.findViewById(R.id.auth_link_with_email_field);
        FancyButton continueBtn = (FancyButton) mFragmentView.findViewById(R.id.auth_link_with_email_continue_btn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utilities.isEmailValid(emailField.getText().toString())){
                    Toast.makeText(getActivity(), "Email is not valid", Toast.LENGTH_SHORT).show();
                }else {
                    AuthActivity authActivity = ((AuthActivity) getActivity());
                    authActivity.mEmail = emailField.getText().toString();
                    HttpKit http = new HttpKit(getActivity());

                    String[] params = {"connected_email", authActivity.mEmail, authActivity.mUsername, authActivity.mPassword};
                    http.regAndLogin(params);
                }
            }
        });
        return mFragmentView;
    }
}
