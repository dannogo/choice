package com.elantix.dopeapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 3/14/16.
 */
public class SignInActivity extends AppCompatActivity{

    private android.app.FragmentManager mManager = getFragmentManager();
    private FragmentTransaction mTransaction;

    private Fragment mCurrentFragment;
    private StartLogin mPage = StartLogin.Login;
    private ImageView mToolbarLeftButton;
    private TextView mToolbarTitle;
    private TextView mToolbarRightButton;
    private String mPredecessorActivity;
    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mPredecessorActivity = getIntent().getStringExtra("activity");

        mToolbarLeftButton = (ImageView) findViewById(R.id.start_login_toolbar_left_button);
        mToolbarTitle = (TextView) findViewById(R.id.start_login_toolbar_title);
        mToolbarRightButton = (TextView) findViewById(R.id.start_login_toolbar_right_button);

        launchFragment(mPage);
        toolbarTitleAndButtonChangesHandler(mPage);

    }

    public enum StartLogin{
        Login, Recovery
    }

    /**
     * Removes previous fragment, adds new fragment to activity;
     * changes title in toolbar;
     * changes appearance of toolbar buttons and their behaviour
     * @param newPage
     */
    protected void switchPageHandler(StartLogin newPage){
        removeFragment();
        mPage = newPage;
        toolbarTitleAndButtonChangesHandler(mPage);
        launchFragment(mPage);
    }

    private void toolbarTitleAndButtonChangesHandler(StartLogin page){
        switch(page){
            case Login:
                mToolbarTitle.setText(R.string.start_log_in_toolbar_login);
                mToolbarRightButton.setVisibility(View.VISIBLE);
                mToolbarRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // SKIP
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                mToolbarLeftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mPredecessorActivity != null){
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_CANCELED, returnIntent);
                        }
                        finish();
                    }
                });
                break;
            case Recovery:
                mToolbarTitle.setText(R.string.start_log_in_toolbar_password_recovery);
                mToolbarRightButton.setVisibility(View.GONE);
                mToolbarLeftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchPageHandler(StartLogin.Login);
                    }
                });
                break;
        }
    }

    private void launchFragment(StartLogin page){
        String fragmentTAG;
        Bundle bundle;
        switch (page){
            case Login:
                mCurrentFragment = new FragmentStartLogin();
                fragmentTAG = "Login";
                break;
            case Recovery:
                mCurrentFragment = new FragmentStartPassRecovery();
                fragmentTAG = "PassRecovery";
                break;
            default:
                mCurrentFragment = new FragmentStartLogin();
                fragmentTAG = "Login";
        }
        mTransaction = mManager.beginTransaction();
        mTransaction.add(R.id.start_login_fragment_container, mCurrentFragment, fragmentTAG);
        mTransaction.commit();
    }

    private void removeFragment(){

        if(mCurrentFragment != null) {
            mTransaction = mManager.beginTransaction();
            mTransaction.remove(mCurrentFragment);
            mTransaction.commit();

        }
    }

    public void afterLoginAction(){
        if (mPredecessorActivity != null){
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
        }else {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
