package com.elantix.dopeapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Random;

/**
 * Created by oleh on 4/14/16.
 */
public class FragmentSplashFirst extends Fragment {
    private View mFragmentView;
    private ImageView mOptionPicture1;
    private ImageView mOptionPicture2;
    ChoiceAnimationHelper mAnimHelper = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_splash_first, container, false);
        return mFragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        mOptionPicture1 = (ImageView) mFragmentView.findViewById(R.id.option_picture_1);
        mOptionPicture2 = (ImageView) mFragmentView.findViewById(R.id.option_picture_2);

        int image1 = R.drawable.splash_puppy;
        int image2 = R.drawable.splash_cat;

        Glide.with(this).load(image1).into(mOptionPicture1);
        Glide.with(this).load(image2).into(mOptionPicture2);

    }

    public void launchRateAnimation(){
        mAnimHelper = new ChoiceAnimationHelper(getActivity(), mFragmentView);
        mAnimHelper.setParameters(ChoiceAnimationHelper.ChoiceSide.Left, 63, true);
        mAnimHelper.draw(true);
    }
}
