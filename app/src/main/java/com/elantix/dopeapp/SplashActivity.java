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
import android.widget.TextView;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 4/14/16.
 */
public class SplashActivity extends AppCompatActivity {

    private SplashPage mPage = SplashPage.First;
    private Fragment mCurrentFragment;
    private android.app.FragmentManager mManager = getFragmentManager();
    private FragmentTransaction mTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        launchFragment(mPage);

        final TextView title = (TextView) findViewById(R.id.splash_title);
        final TextView text = (TextView) findViewById(R.id.splash_text);
        final ImageView dot1 = (ImageView) findViewById(R.id.splash_dot_1);
        final ImageView dot2 = (ImageView) findViewById(R.id.splash_dot_2);
        final ImageView dot3 = (ImageView) findViewById(R.id.splash_dot_3);
        final ImageView dot4 = (ImageView) findViewById(R.id.splash_dot_4);

        final FancyButton nextBtn = (FancyButton) findViewById(R.id.splash_next_button);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mPage){
                    case First:
                        title.setText(R.string.splash_lower_2_title);
                        text.setText(R.string.splash_lower_2_text);
                        dot1.setVisibility(View.GONE);
                        dot2.setVisibility(View.VISIBLE);
//                        switchPageHandler(SplashPage.Second);
                        ((FragmentSplashFirst)mCurrentFragment).launchRateAnimation();
                        mPage = SplashPage.Second;
                        break;
                    case Second:
                        title.setText(R.string.splash_lower_3_title);
                        text.setText(R.string.splash_lower_3_text);
                        dot2.setVisibility(View.GONE);
                        dot3.setVisibility(View.VISIBLE);
                        switchPageHandler(SplashPage.Third);
                        break;
                    case Third:
                        title.setText(R.string.splash_lower_4_title);
                        text.setText(R.string.splash_lower_4_text);
                        nextBtn.setText(getResources().getString(R.string.splash_lower_button_text_2));
                        dot3.setVisibility(View.GONE);
                        dot4.setVisibility(View.VISIBLE);
                        switchPageHandler(SplashPage.Fourth);
                        break;
                    case Fourth:
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        });


    }

    public enum SplashPage{
        First, Second, Third, Fourth
    }

    /**
     * Removes previous fragment, adds new fragment to activity;
     * changes title in toolbar;
     * changes appearance of toolbar buttons and their behaviour
     * @param newPage
     */
    protected void switchPageHandler(SplashPage newPage){
        removeFragment();
        mPage = newPage;
//        setArrowBtnBehaviour(mPage);
        launchFragment(mPage);
    }

    private void launchFragment(SplashPage page){
        String fragmentTAG;
        switch (page){
            case First:
                mCurrentFragment = new FragmentSplashFirst();
                fragmentTAG = "First";
                break;
            case Second:

                fragmentTAG = "Second";
                break;
            case Third:
                mCurrentFragment = new FragmentSplashThird();
                fragmentTAG = "Third";
                break;
            case Fourth:
                mCurrentFragment = new FragmentSplashFourth();
                fragmentTAG = "Fourth";
                break;
            default:
                mCurrentFragment = new FragmentSplashFirst();
                fragmentTAG = "First";
        }
        mTransaction = mManager.beginTransaction();
        mTransaction.add(R.id.splash_fragment_container, mCurrentFragment, fragmentTAG);
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
