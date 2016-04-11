package com.elantix.dopeapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.Random;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by oleh on 3/17/16.
 */
public class FragmentDailyDope extends Fragment implements View.OnClickListener{

    protected View fragmentView;
    private LinearLayout mInfoBar;
    ImageView mOptionPicture1;
    ImageView mOptionPicture2;
    String someTitle;
    ChoiceAnimationHelper mAnimHelper = null;
    int mDopeNum;
    private FragmentViewPager.CommunicatorOne mCommOne;

    // newInstance constructor for creating fragment with arguments
    public static FragmentDailyDope newInstance(int page, String title) {
        FragmentDailyDope fragmentDailyDope = new FragmentDailyDope();
        Bundle args = new Bundle();
        args.putInt("dopeNum", page);
        args.putString("someString", title);
        fragmentDailyDope.setArguments(args);
        return fragmentDailyDope;
    }



    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        mDopeNum = bundle.getInt("dopeNum", 0);
        someTitle = bundle.getString("someTitle");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_daily_dope, container, false);
        mCommOne = (FragmentViewPager.CommunicatorOne) getActivity();

        ImageButton moreButton = (ImageButton) fragmentView.findViewById(R.id.more_button);
        final Bundle bundle = this.getArguments();
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).showContextOptions(true, bundle.getInt("num"));
            }
        });

        mInfoBar = (LinearLayout) fragmentView.findViewById(R.id.daily_dope_info_bar);
        mInfoBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CommentsActivity.class);
                startActivity(intent);
            }
        });

        if (Utilities.sRateStateBackups[mDopeNum] != null){
            applyRestoredRateData();
        }

        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        ImageView blurredPicture1 = (ImageView) fragmentView.findViewById(R.id.blurred_picture_1);
        ImageView blurredPicture2 = (ImageView) fragmentView.findViewById(R.id.blurred_picture_2);

        mOptionPicture1 = (ImageView) fragmentView.findViewById(R.id.option_picture_1);
        mOptionPicture2 = (ImageView) fragmentView.findViewById(R.id.option_picture_2);
        mOptionPicture1.setOnClickListener(this);
        mOptionPicture2.setOnClickListener(this);

        int image1;
        int image2;

        switch (mDopeNum){
            case 0:
                image1 = R.drawable.girl3;
                image2 = R.drawable.girl4;
                break;
            case 1:
                image1 = R.drawable.donald;
                image2 = R.drawable.ted;
                break;
            case 2:
                image1 = R.drawable.bernie;
                image2 = R.drawable.hillary;
                break;
            case 3:
                image1 = R.drawable.earth;
                image2 = R.drawable.mars;
                break;
            case 4:
                image1 = R.drawable.fork;
                image2 = R.drawable.spoon;
                break;
            case 5:
                image1 = R.drawable.hell;
                image2 = R.drawable.heaven;
                break;
            case 6:
                image1 = R.drawable.cat1;
                image2 = R.drawable.cat2;
                break;
            case 7:
                image1 = R.drawable.cat3;
                image2 = R.drawable.cat4;
                break;
            case 8:
                image1 = R.drawable.dog1;
                image2 = R.drawable.dog2;
                break;
            case 9:
                image1 = R.drawable.dog3;
                image2 = R.drawable.dog4;
                break;
            default:
                image1 = R.drawable.girl3;
                image2 = R.drawable.girl4;
        }


        Glide.with(this).load(image1).into(mOptionPicture1);
        Glide.with(this).load(image2).into(mOptionPicture2);

        Glide.with(this).load(image1)
                .bitmapTransform(new BlurTransformation(getActivity()))
                .into(blurredPicture1);

        Glide.with(this).load(image2)
                .bitmapTransform(new BlurTransformation(getActivity()))
                .into(blurredPicture2);

    }

    private void launchRateAnimation(ChoiceAnimationHelper.ChoiceSide side){

        Random rand = new Random();
        int min = 15;
        int max = 85;
        int randomNum = rand.nextInt((max - min) + 1) + min;

        mAnimHelper = new ChoiceAnimationHelper(getActivity(), fragmentView);
        mAnimHelper.setParameters(side, randomNum, Utilities.sRateAnimationDirection);
        mAnimHelper.draw(true);
        Utilities.sRateStateBackups[mDopeNum] = new RateStateBackup(side, randomNum, Utilities.sRateAnimationDirection);
        Utilities.sRateAnimationDirection = !Utilities.sRateAnimationDirection;
    }

    private void applyRestoredRateData(){
        ChoiceAnimationHelper.ChoiceSide side = Utilities.sRateStateBackups[mDopeNum].chosenSide;
        int leftRate = Utilities.sRateStateBackups[mDopeNum].leftRate;
        Boolean directionInside = Utilities.sRateStateBackups[mDopeNum].directionInside;

        mAnimHelper = new ChoiceAnimationHelper(getActivity(), fragmentView);
        mAnimHelper.setParameters(side, leftRate, directionInside);
        mAnimHelper.draw(false);
    }

    private void showNextPage(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCommOne.respond();
            }
        }, 1200);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mOptionPicture1.getId()){
            if (mAnimHelper == null){
                launchRateAnimation(ChoiceAnimationHelper.ChoiceSide.Left);
                showNextPage();
            }
        }else if (id == mOptionPicture2.getId()){

            if (mAnimHelper == null){
                launchRateAnimation(ChoiceAnimationHelper.ChoiceSide.Right);
                showNextPage();
            }
        }

    }
}
