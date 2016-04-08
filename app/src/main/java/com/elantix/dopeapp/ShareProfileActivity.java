package com.elantix.dopeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.internal.AttributionIdentifiers;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.w3c.dom.Text;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 4/3/16.
 */
public class ShareProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private FancyButton mCopyLinkButton;
    private ImageButton mCloseButton;
    private CircularImageView mAvatar;
    private ImageView mAvatarPlaceholder;
    private TextView mUsername;
    private TextView mFirstLastNames;

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

        mUsername = (TextView) findViewById(R.id.share_profile_user_name);
        mFirstLastNames = (TextView) findViewById(R.id.share_profile_first_last_names);
        mAvatar = (CircularImageView) findViewById(R.id.share_profile_avatar);
        mAvatarPlaceholder = (ImageView) findViewById(R.id.profile_settings_user_icon);
        Boolean isOwn = getIntent().getBooleanExtra("own", false);
        if (isOwn){
            if (Utilities.avatarUri != null) {
                Log.w("ShareProfileActivity", "Avatar is NOT NULL");
                Glide.with(this).load(Utilities.avatarUri).into(mAvatar);
            }else{
                Log.w("ShareProfileActivity", "Avatar is NULL");
                mAvatar.setVisibility(View.GONE);
                mAvatarPlaceholder.setVisibility(View.VISIBLE);
            }
            String username = (Utilities.profileUsername.isEmpty()) ? getResources().getString(R.string.profile_settings_username_placeholder_text) : Utilities.profileUsername;
            mUsername.setText(username);
            String firstLastNames = (Utilities.profileFirstLastNames.isEmpty()) ? getResources().getString(R.string.profile_settings_firstlastnames_placeholder_text) : Utilities.profileFirstLastNames;
            mFirstLastNames.setText(firstLastNames);
        }else {
            Glide.with(this).load(R.drawable.ania2).into(mAvatar);
            Log.w("ShareProfileActivity", "NOT is own");
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mCloseButton.getId()){
            finish();
        }
    }
}
