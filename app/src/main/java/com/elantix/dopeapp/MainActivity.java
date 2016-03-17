package com.elantix.dopeapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.commit451.nativestackblur.NativeStackBlur;


/**
 * Created by oleh on 3/14/16.
 */
public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.main_app_bar);
        TextView toolbraTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView blurredPicture1 = (ImageView) findViewById(R.id.blurred_picture_1);
        ImageView blurredPicture2 = (ImageView) findViewById(R.id.blurred_picture_2);

        ImageView optionPicture1 = (ImageView) findViewById(R.id.option_picture_1);
        ImageView optionPicture2 = (ImageView) findViewById(R.id.option_picture_2);

        Glide.with(this).load(R.drawable.girl3).into(optionPicture1);
        Glide.with(this).load(R.drawable.girl4).into(optionPicture2);

        Bitmap pic1 = BitmapFactory.decodeResource(getResources(),
                R.drawable.girl3);
        Bitmap bm1 = NativeStackBlur.process(pic1, 100);
        blurredPicture1.setImageBitmap(bm1);

        Bitmap pic2 = BitmapFactory.decodeResource(getResources(),
                R.drawable.girl4);
        Bitmap bm2 = NativeStackBlur.process(pic2, 100);
        blurredPicture2.setImageBitmap(bm2);

//        Blurry.with(this).radius(25).sampling(2).onto((ViewGroup) blurredLinearLayout);

//        Blurry.with(this).capture(toolbar).into(blurredPicture1);

    }
}
