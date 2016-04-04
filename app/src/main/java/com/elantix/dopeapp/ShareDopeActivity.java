package com.elantix.dopeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 3/28/16.
 */
public class ShareDopeActivity extends AppCompatActivity implements View.OnClickListener {

    FancyButton mCopyLinkButton;

    // TODO:
    // Increase close toolbar button clickable area size

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide statusbar of Android
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_dope);

        mCopyLinkButton = (FancyButton) findViewById(R.id.share_dope_copy_link_button);
        buttonsAppearenceHandling();

        int image1;
        int image2;
        int num = getIntent().getIntExtra("num", 1);
        if (num == 1){
            image1 = R.drawable.girl3;
            image2 = R.drawable.girl4;
        }else if (num == 2){
            image1 = R.drawable.donald;
            image2 = R.drawable.ted;
        }else{
            image1 = R.drawable.bernie;
            image2 = R.drawable.hillary;
        }

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
