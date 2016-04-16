package com.elantix.dopeapp;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 4/11/16.
 */
public class FragmentStartLogin extends Fragment implements View.OnClickListener{

    private View mFragmentView;
    private EditText mPasswordField;
    private FancyButton mSignInButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_start_login, container, false);
        TextView forgotPass = (TextView) mFragmentView.findViewById(R.id.start_login_forgot_password);
        forgotPass.setOnClickListener(this);

        mPasswordField = (EditText) mFragmentView.findViewById(R.id.start_login_password_field);
        mPasswordField.setTypeface(Typeface.DEFAULT);
        mPasswordField.setTransformationMethod(new PasswordTransformationMethod());

        mSignInButton = (FancyButton) mFragmentView.findViewById(R.id.start_login_signin_button);
        mSignInButton.setOnClickListener(this);

        return mFragmentView;
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.start_login_forgot_password:
                ((SignInActivity)getActivity()).switchPageHandler(SignInActivity.StartLogin.Recovery);
                break;
            case R.id.start_login_signin_button:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
                break;
        }
    }
}
