package com.elantix.dopeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.commit451.nativestackblur.NativeStackBlur;

/**
 * Created by oleh on 4/3/16.
 */
public class DopeStatisticsActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton mLeftToolbarButton;
    private LinearLayout mComments;
    private LinearLayout mShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dope_statistics);

        mLeftToolbarButton = (ImageButton) findViewById(R.id.left_button);
        mLeftToolbarButton.setImageResource(R.drawable.toolbar_left_arrow);
        mLeftToolbarButton.setOnClickListener(this);
        ImageButton rightToolbarButton = (ImageButton) findViewById(R.id.right_button);
        rightToolbarButton.setVisibility(View.GONE);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.dope_statistics_toolbar_title);

        mComments = (LinearLayout) findViewById(R.id.dope_statistics_comments);
        mShare = (LinearLayout) findViewById(R.id.dope_statistics_share);
        mComments.setOnClickListener(this);
        mShare.setOnClickListener(this);

        ImageView blurredPicture1 = (ImageView) findViewById(R.id.dope_statistics_blurred_picture_1);
        ImageView blurredPicture2 = (ImageView) findViewById(R.id.dope_statistics_blurred_picture_2);

        ImageView optionPicture1 = (ImageView) findViewById(R.id.dope_statistics_option_picture_1);
        ImageView optionPicture2 = (ImageView) findViewById(R.id.dope_statistics_option_picture_2);

//        Bundle bundle = this.getArguments();

        int image1 = R.drawable.girl3;
        int image2 = R.drawable.girl4;

        Glide.with(this).load(image1).into(optionPicture1);
        Glide.with(this).load(image2).into(optionPicture2);

        Bitmap pic1 = BitmapFactory.decodeResource(getResources(), image1);
        Bitmap bm1 = NativeStackBlur.process(pic1, 100);
        blurredPicture1.setImageBitmap(bm1);

        Bitmap pic2 = BitmapFactory.decodeResource(getResources(), image2);
        Bitmap bm2 = NativeStackBlur.process(pic2, 100);
        blurredPicture2.setImageBitmap(bm2);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mLeftToolbarButton.getId()){
            finish();
        }else if (id == mComments.getId()){
            Intent intent = new Intent(DopeStatisticsActivity.this, CommentsActivity.class);
            startActivity(intent);
        }else if (id == mShare.getId()){
            Intent intent = new Intent(DopeStatisticsActivity.this, ShareDopeActivity.class);
            intent.putExtra("num", 1);
            startActivity(intent);
        }
    }
}
