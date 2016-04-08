package com.elantix.dopeapp;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.commit451.nativestackblur.NativeStackBlur;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by oleh on 3/17/16.
 */
public class FragmentDailyDope extends Fragment implements View.OnClickListener{

    protected View fragmentView;
    private LinearLayout mInfoBar;
    ImageView mOptionPicture1;
    ImageView mOptionPicture2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_daily_dope, container, false);
        ImageButton moreButton = (ImageButton) fragmentView.findViewById(R.id.more_button);
        final Bundle bundle = this.getArguments();
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).showContextOptions(true, bundle.getInt("num"));
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
        Bundle bundle = this.getArguments();

        int image1;
        int image2;

//        if (bundle.getInt("num") == 1) {
//            image1 = R.drawable.girl3;
//            image2 = R.drawable.girl4;
//        }else if(bundle.getInt("num") == 2){
//            image1 = R.drawable.donald;
//            image2 = R.drawable.ted;
//        }else{
//            image1 = R.drawable.bernie;
//            image2 = R.drawable.hillary;
//        }

        switch (Utilities.sDopeNumber){
            case 0:
                image1 = R.drawable.girl3;
                image2 = R.drawable.girl4;
                Utilities.sDopeNumber++;
                break;
            case 1:
                image1 = R.drawable.donald;
                image2 = R.drawable.ted;
                Utilities.sDopeNumber++;
                break;
            case 2:
                image1 = R.drawable.bernie;
                image2 = R.drawable.hillary;
                Utilities.sDopeNumber++;
                break;
            case 3:
                image1 = R.drawable.earth;
                image2 = R.drawable.mars;
                Utilities.sDopeNumber++;
                break;
            case 4:
                image1 = R.drawable.fork;
                image2 = R.drawable.spoon;
                Utilities.sDopeNumber++;
                break;
            case 5:
                image1 = R.drawable.hell;
                image2 = R.drawable.heaven;
                Utilities.sDopeNumber++;
                break;
            case 6:
                image1 = R.drawable.cat1;
                image2 = R.drawable.cat2;
                Utilities.sDopeNumber++;
                break;
            case 7:
                image1 = R.drawable.cat3;
                image2 = R.drawable.cat4;
                Utilities.sDopeNumber++;
                break;
            case 8:
                image1 = R.drawable.dog1;
                image2 = R.drawable.dog2;
                Utilities.sDopeNumber++;
                break;
            case 9:
                image1 = R.drawable.dog3;
                image2 = R.drawable.dog4;
                Utilities.sDopeNumber = 0;
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

        // DEPRECATED
//        Bitmap pic1 = BitmapFactory.decodeResource(getResources(), image1);
//        Bitmap bm1 = NativeStackBlur.process(pic1, 100);
//        blurredPicture1.setImageBitmap(bm1);

//        Bitmap pic2 = BitmapFactory.decodeResource(getResources(), image2);
//        Bitmap bm2 = NativeStackBlur.process(pic2, 100)
//        blurredPicture2.setImageBitmap(bm2);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mOptionPicture1.getId()){
            if (Utilities.ANIMATION_TYPES.length == Utilities.sAnimationNumber) {
                Utilities.sAnimationNumber = 0;
            }
//            Utilites.showExtremelyShortToast(getActivity(), String.valueOf(Utilites.ANIMATION_TYPES[Utilites.sAnimationNumber]));
            ((MainActivity) getActivity()).switchPageAnimatedHandler((((MainActivity) getActivity()).page));
            Utilities.sAnimationNumber++;
        }else if (id == mOptionPicture2.getId()){
            if (Utilities.ANIMATION_TYPES.length == Utilities.sAnimationNumber) {
                Utilities.sAnimationNumber = 0;
            }
//            Utilites.showExtremelyShortToast(getActivity(), String.valueOf(Utilites.ANIMATION_TYPES[Utilites.sAnimationNumber]));
            ((MainActivity)getActivity()).switchPageAnimatedHandler((((MainActivity) getActivity()).page));
            Utilities.sAnimationNumber++;
        }
    }
}
