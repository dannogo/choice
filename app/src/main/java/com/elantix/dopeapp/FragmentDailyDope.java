package com.elantix.dopeapp;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.commit451.nativestackblur.NativeStackBlur;

/**
 * Created by oleh on 3/17/16.
 */
public class FragmentDailyDope extends Fragment {

    protected View fragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_daily_dope, container, false);

        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        ImageView blurredPicture1 = (ImageView) fragmentView.findViewById(R.id.blurred_picture_1);
        ImageView blurredPicture2 = (ImageView) fragmentView.findViewById(R.id.blurred_picture_2);

        ImageView optionPicture1 = (ImageView) fragmentView.findViewById(R.id.option_picture_1);
        ImageView optionPicture2 = (ImageView) fragmentView.findViewById(R.id.option_picture_2);

        Bundle bundle = this.getArguments();

        int image1;
        int image2;

        if (bundle.getInt("num") == 1) {
            image1 = R.drawable.girl3;
            image2 = R.drawable.girl4;
        }else{
            image1 = R.drawable.donald;
            image2 = R.drawable.ted;
        }

        Glide.with(this).load(image1).into(optionPicture1);
        Glide.with(this).load(image2).into(optionPicture2);

        Bitmap pic1 = BitmapFactory.decodeResource(getResources(), image1);
        Bitmap bm1 = NativeStackBlur.process(pic1, 100);
        blurredPicture1.setImageBitmap(bm1);

        Bitmap pic2 = BitmapFactory.decodeResource(getResources(), image2);
        Bitmap bm2 = NativeStackBlur.process(pic2, 100);
        blurredPicture2.setImageBitmap(bm2);
    }
}
