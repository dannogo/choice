package com.elantix.dopeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 4/3/16.
 */
public class ShareProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private FancyButton mCopyLinkButton;
    private ImageButton mCloseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_profile);

        mCopyLinkButton = (FancyButton) findViewById(R.id.share_profile_copy_link_button);
        buttonsAppearenceHandling();
        mCloseButton = (ImageButton) findViewById(R.id.share_profile_left_toolbar_button);
        mCloseButton.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mCloseButton.getId()){
            finish();
        }
    }
}
