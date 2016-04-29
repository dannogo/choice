package com.elantix.dopeapp;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 4/11/16.
 */
public class FragmentStartLogin extends Fragment implements View.OnClickListener{

    private View mFragmentView;
    private EditText mPasswordField;
    private EditText mUsernameField;
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
        mUsernameField = (EditText) mFragmentView.findViewById(R.id.start_login_login_field);

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
                String username = mUsernameField.getText().toString();
                String password = mPasswordField.getText().toString();

//                if (!username.isEmpty() && !password.isEmpty()) {
//                    String[] loginData = {"username", username, password};
                    String[] loginData = {"username", username, password};
                    HttpKit http = new HttpKit(getActivity());
                    http.login(loginData);
//                }
                break;
        }
    }
}
