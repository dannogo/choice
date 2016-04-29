package com.elantix.dopeapp;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
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
public class FragmentAuthSetUsername extends Fragment {
    private View mFragmentView;
    private EditText mUsernameField;
    private FancyButton mContinueBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_auth_set_username, container, false);
        mUsernameField = (EditText) mFragmentView.findViewById(R.id.fragment_auth_username_field);
        mUsernameField.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
//                if (s.length() != 0)
//                    Field2.setText("");
            }
        });
        mContinueBtn = (FancyButton) mFragmentView.findViewById(R.id.fragment_auth_continue_btn);
        mContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUsernameField.length() != 0){
                    ((AuthActivity)getActivity()).mUsername = mUsernameField.getText().toString();
                    ((AuthActivity)getActivity()).switchPageHandler(AuthActivity.AuthPage.EnterPassword);
                }else {
                    Toast.makeText(getActivity(), "Enter username", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return mFragmentView;
    }
}
