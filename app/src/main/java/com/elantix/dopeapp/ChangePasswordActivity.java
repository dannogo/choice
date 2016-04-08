package com.elantix.dopeapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by oleh on 4/8/16.
 */
public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mCurrentPasswordField;
    private EditText mCreatePasswordField;
    private EditText mConfirmPasswordField;
    private TextView mToolbarCancelBtn;
    private TextView mToolbarDoneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mToolbarCancelBtn = (TextView) findViewById(R.id.change_password_left_button);
        mToolbarDoneBtn = (TextView) findViewById(R.id.change_password_right_button);
        mToolbarCancelBtn.setOnClickListener(this);
        mToolbarDoneBtn.setOnClickListener(this);

        mCurrentPasswordField = (EditText) findViewById(R.id.change_password_current_password_field);
        mCurrentPasswordField.setTypeface(Typeface.DEFAULT);
        mCurrentPasswordField.setTransformationMethod(new PasswordTransformationMethod());

        mCreatePasswordField = (EditText) findViewById(R.id.change_password_create_password_field);
        mCreatePasswordField.setTypeface(Typeface.DEFAULT);
        mCreatePasswordField.setTransformationMethod(new PasswordTransformationMethod());

        mConfirmPasswordField = (EditText) findViewById(R.id.change_password_confirm_password_field);
        mConfirmPasswordField.setTypeface(Typeface.DEFAULT);
        mConfirmPasswordField.setTransformationMethod(new PasswordTransformationMethod());

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mToolbarCancelBtn.getId()){
            finish();
        }else if (id == mToolbarDoneBtn.getId()){
            // send new password to the server
            finish();
        }
    }
}
