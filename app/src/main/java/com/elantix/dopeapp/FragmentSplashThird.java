package com.elantix.dopeapp;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by oleh on 4/14/16.
 */
public class FragmentSplashThird extends Fragment {
    private View mFragmentView;
    private ImageView mPuppy;
    private ImageView mKitty;
    private LeftBasedMessage msg1;
    private LeftBasedMessage msg2;
    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_splash_third, container, false);

        mPuppy = (ImageView) mFragmentView.findViewById(R.id.splash_3_puppy);
        mKitty = (ImageView) mFragmentView.findViewById(R.id.splash_3_cat);
        final Bitmap bitmapPuppy = ((BitmapDrawable)mPuppy.getDrawable()).getBitmap();
        final Bitmap bitmapKitty = ((BitmapDrawable)mKitty.getDrawable()).getBitmap();
        msg1 = (LeftBasedMessage) mFragmentView.findViewById(R.id.splash_3_comment_field_1);
        msg2 = (LeftBasedMessage) mFragmentView.findViewById(R.id.splash_3_comment_field_2);



        ViewTreeObserver vto = mKitty.getViewTreeObserver();
        mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Bitmap resultPuppy = Utilities.getOneSideRoundedBitmap(bitmapPuppy, mPuppy, true, 30);
                mPuppy.setImageBitmap(resultPuppy);
                Bitmap resultKitty = Utilities.getOneSideRoundedBitmap(bitmapKitty, mKitty, false, 30);
                mKitty.setImageBitmap(resultKitty);

                msg1.setMessageBackground(R.color.splash_comment_fields_color);
                msg2.setMessageBackground(R.color.splash_comment_fields_color);
            }
        };
        vto.addOnGlobalLayoutListener(mGlobalLayoutListener);


        return mFragmentView;
    }

    @Override
    public void onDestroy() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            mKitty.getViewTreeObserver().removeGlobalOnLayoutListener(mGlobalLayoutListener);
        } else {
            mKitty.getViewTreeObserver().removeOnGlobalLayoutListener(mGlobalLayoutListener);
        }
        super.onDestroy();
    }
}
