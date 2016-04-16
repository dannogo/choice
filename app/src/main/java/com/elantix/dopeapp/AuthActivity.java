package com.elantix.dopeapp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by oleh on 4/12/16.
 */
public class AuthActivity extends AppCompatActivity {

    private Fragment mCurrentFragment;
    private AuthPage mPage = AuthPage.LinkEmail;

    private android.app.FragmentManager mManager = getFragmentManager();
    private FragmentTransaction mTransaction;
    private ImageView mArrowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mArrowBtn = (ImageView) findViewById(R.id.auth_arrow);

        setArrowBtnBehaviour(mPage);
        launchFragment(mPage);
    }

    public enum AuthPage{
        LinkEmail, SetUsername, EnterPassword, ConnectAccount, SignInDope
    }

    private void setArrowBtnBehaviour(AuthPage page){
        switch (page){
            case LinkEmail:
                mArrowBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                break;
            case SetUsername:
                mArrowBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchPageHandler(AuthPage.LinkEmail);
                    }
                });
                break;
            case EnterPassword:
                mArrowBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchPageHandler(AuthPage.SetUsername);
                    }
                });
                break;
            case ConnectAccount:
                mArrowBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchPageHandler(AuthPage.EnterPassword);
                    }
                });
                break;
            case  SignInDope:
                mArrowBtn.setImageResource(R.drawable.white_cross);
                float scale = getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (20*scale + 0.5f);
                mArrowBtn.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
                mArrowBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                break;
        }
    }

    /**
     * Removes previous fragment, adds new fragment to activity;
     * changes title in toolbar;
     * changes appearance of toolbar buttons and their behaviour
     * @param newPage
     */
    protected void switchPageHandler(AuthPage newPage){
        removeFragment();
        mPage = newPage;
        setArrowBtnBehaviour(mPage);
        launchFragment(mPage);
    }

    private void launchFragment(AuthPage page){
        String fragmentTAG;
        switch (page){
            case LinkEmail:
                mCurrentFragment = new FragmentAuthLinkEmail();
                fragmentTAG = "LinkEmail";
                break;
            case SetUsername:
                mCurrentFragment = new FragmentAuthSetUsername();
                fragmentTAG = "PassRecovery";
                break;
            case EnterPassword:
                mCurrentFragment = new FragmentAuthEnterPassword();
                fragmentTAG = "PassRecovery";
                break;
            case ConnectAccount:
                mCurrentFragment = new FragmentAuthConnectAccount();
                fragmentTAG = "PassRecovery";
                break;
            case SignInDope:
                mCurrentFragment = new FragmentAuthSignInDope();
                fragmentTAG = "PassRecovery";
                break;
            default:
                mCurrentFragment = new FragmentAuthLinkEmail();
                fragmentTAG = "LinkEmail";
        }
        mTransaction = mManager.beginTransaction();
        mTransaction.add(R.id.auth_fragment_container, mCurrentFragment, fragmentTAG);
        mTransaction.commit();
    }

    private void removeFragment(){

        if(mCurrentFragment != null) {
            mTransaction = mManager.beginTransaction();
            mTransaction.remove(mCurrentFragment);
            mTransaction.commit();

        }
    }
}
