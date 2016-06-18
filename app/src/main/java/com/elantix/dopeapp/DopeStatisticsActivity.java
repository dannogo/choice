package com.elantix.dopeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
//import com.commit451.nativestackblur.NativeStackBlur;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by oleh on 4/3/16.
 */
public class DopeStatisticsActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton mLeftToolbarButton;
    private LinearLayout mComments;
    private LinearLayout mShare;
    private String mDopeId;
    DopeInfo mInfo;
    private TextView mCommentsCntView;
    public static int sNumOfComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dope_statistics);

        Intent intent = getIntent();
        mInfo = new DopeInfo();
        mDopeId = intent.getStringExtra("id");
        mInfo.id = mDopeId;
        mInfo.userId = intent.getStringExtra("uid");
        mInfo.votesAll = intent.getIntExtra("votes", 0);
        mInfo.question = intent.getStringExtra("question");
        mInfo.percent1 = intent.getIntExtra("percent1", 0);
        mInfo.percent2 = intent.getIntExtra("percent2", 0);
        mInfo.comments = intent.getIntExtra("comments", 0);
        mInfo.photo1 = Uri.parse(intent.getStringExtra("photo1"));
        mInfo.photo2 = Uri.parse(intent.getStringExtra("photo2"));

        sNumOfComments = mInfo.comments;

        TextView votesCntView = (TextView) findViewById(R.id.dope_statistics_votes);
        votesCntView.setText("" + mInfo.votesAll + " Votes");
        TextView questionView = (TextView) findViewById(R.id.dope_statistics_question);
        questionView.setText(mInfo.question);
        TextView leftRate = (TextView) findViewById(R.id.dope_statistics_left_image_rate);
        leftRate.setText(""+mInfo.percent1+"%");
        TextView rightRate = (TextView) findViewById(R.id.dope_statistics_right_image_rate);
        int percent2 = (mInfo.percent1 != 0) ? 100 - mInfo.percent1 : mInfo.percent2;
        rightRate.setText(""+percent2+"%");
        mCommentsCntView = (TextView) findViewById(R.id.dope_statistics_number_of_comments);
        mCommentsCntView.setText("" + mInfo.comments);


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

        Glide.with(this).load(mInfo.photo1).into(optionPicture1);
        Glide.with(this).load(mInfo.photo2).into(optionPicture2);

        Glide.with(this).load(mInfo.photo1)
                .bitmapTransform(new BlurTransformation(this))
                .into(blurredPicture1);

        Glide.with(this).load(mInfo.photo2)
                .bitmapTransform(new BlurTransformation(this))
                .into(blurredPicture2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCommentsCntView.setText(""+sNumOfComments);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mLeftToolbarButton.getId()){
            finish();
        }else if (id == mComments.getId()){
            Intent intent = new Intent(DopeStatisticsActivity.this, CommentsActivity.class);
            intent.putExtra("dopeId", mDopeId);
            intent.putExtra("publisherId", mInfo.userId);
            intent.putExtra("question", mInfo.question);
            intent.putExtra("votesCnt", mInfo.votesAll);
            startActivity(intent);
        }else if (id == mShare.getId()){

            Intent intent = new Intent(DopeStatisticsActivity.this, ShareDopeActivity.class);
            intent.putExtra("dopeNum", -1);
            intent.putExtra("dopeId", mInfo.id);
            intent.putExtra("leftPic", mInfo.photo1.toString());
            intent.putExtra("rightPic", mInfo.photo2.toString());
            intent.putExtra("question", mInfo.question);

            startActivity(intent);
        }
    }
}
