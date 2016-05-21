package com.elantix.dopeapp;

import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 3/28/16.
 */
public class ShareDopeActivity extends AppCompatActivity implements View.OnClickListener {

    private FancyButton mCopyLinkButton;
    private DopeInfo mCurItem;
    public TextView mLinkField;
    public ProgressDialog mProgressDialog;
    public String mLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide statusbar of Android
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_dope);

        mLinkField = (TextView) findViewById(R.id.share_dope_link_text);
        mCopyLinkButton = (FancyButton) findViewById(R.id.share_dope_copy_link_button);
        mCopyLinkButton.setOnClickListener(this);
        buttonsAppearenceHandling();

        Intent intent = getIntent();

        int dopeNum = intent.getIntExtra("dopeNum", 1);
        if(dopeNum == -1){
            mCurItem = new DopeInfo();
            mCurItem.id = intent.getStringExtra("dopeId");
            mCurItem.photo1 = Uri.parse(intent.getStringExtra("leftPic"));
            mCurItem.photo2 = Uri.parse(intent.getStringExtra("rightPic"));
            mCurItem.question = intent.getStringExtra("question");
        }else{

            if (Utilities.sDopeListType == Utilities.DopeListType.Ten){
                mCurItem = Utilities.sDopes10[dopeNum];
            }else if (Utilities.sDopeListType == Utilities.DopeListType.Hundred){
                mCurItem = Utilities.sDopes100[dopeNum];
            }else{
                mCurItem = Utilities.sDopesFriendsFeed[dopeNum];
            }
        }


//        HttpKit http = new HttpKit(ShareDopeActivity.this);
//        http.shareDope(mCurItem.id, Utilities.sToken);

        mLink = "http://dopeapi.elantix.net/dope/"+mCurItem.id;
        mLinkField.setText(mLink);

        Uri image1 = mCurItem.photo1;
        Uri image2 = mCurItem.photo2;

        TextView question = (TextView) findViewById(R.id.share_dope_question_text);
        question.setText(mCurItem.question);

        ImageView optionPicture1 = (ImageView) findViewById(R.id.share_dope_picture_1);
        ImageView optionPicture2 = (ImageView) findViewById(R.id.share_dope_picture_2);

        Glide.with(this).load(image1).into(optionPicture1);
        Glide.with(this).load(image2).into(optionPicture2);

        ImageButton closeToolbarButton = (ImageButton) findViewById(R.id.share_dope_left_toolbar_button);
        closeToolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mCopyLinkButton.getId()){
            ClipboardManager clipMan = (ClipboardManager) getSystemService(ShareDopeActivity.this.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("dopeLink", mLink);
            clipMan.setPrimaryClip(clip);
            Toast.makeText(ShareDopeActivity.this, "Link to dope copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    private void buttonsAppearenceHandling(){
        android.view.ViewGroup.LayoutParams params = mCopyLinkButton.getLayoutParams();
        int height = params.height;

        mCopyLinkButton.getIconImageObject().setLayoutParams(new LinearLayout.LayoutParams(70, 70));
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height);
//        float n = getResources().getDimension(R.dimen.friends_profile_message_margin_4);
        float n = 0.0f;
        lp1.setMargins(0, (int)n, 0, 0);
        mCopyLinkButton.setLayoutParams(lp1);

    }
}
