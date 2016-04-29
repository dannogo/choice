package com.elantix.dopeapp;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 4/12/16.
 */
public class FragmentAuthEnterPassword extends Fragment {
    private View mFragmentView;
    private String mCurrentPassword = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_auth_enter_password, container, false);

        final EditText field1 = (EditText) mFragmentView.findViewById(R.id.auth_enter_pass_field_1);

        field1.setTypeface(Typeface.DEFAULT);
        field1.setTransformationMethod(new PasswordTransformationMethod());

        field1.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (
                        actionId == EditorInfo.IME_ACTION_DONE ||
                                actionId == EditorInfo.IME_ACTION_NEXT
                        ) {
                    //Request to server in order to validate password


                    return false;
                }
                return false;
            }
        });

        final EditText field2 = (EditText) mFragmentView.findViewById(R.id.auth_enter_pass_field_2);
        field2.setTypeface(Typeface.DEFAULT);
        field2.setTransformationMethod(new PasswordTransformationMethod());
        field2.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (
                        actionId == EditorInfo.IME_ACTION_DONE ||
                                actionId == EditorInfo.IME_ACTION_NEXT
                        ) {
                    //Request to server in order to validate password
                    if (!field1.getText().toString().equals(field2.getText().toString())) {
                        Toast.makeText(getActivity(), "Passwords should match to each other", Toast.LENGTH_LONG).show();
                        return true;
                    }
                }
                return false;
            }
        });

        FancyButton continueBtn = (FancyButton) mFragmentView.findViewById(R.id.auth_enter_password_continue_btn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if password is valid
                mCurrentPassword = field1.getText().toString();
                if (!mCurrentPassword.equals(field2.getText().toString()) /* && if password is valid*/) {
                    Toast.makeText(getActivity(), "Passwords should match to each other", Toast.LENGTH_LONG).show();
                } else {
                    ((AuthActivity) getActivity()).mPassword = mCurrentPassword;
                    ((AuthActivity) getActivity()).switchPageHandler(AuthActivity.AuthPage.ConnectAccount);
                }
            }
        });

        return mFragmentView;
    }
}
