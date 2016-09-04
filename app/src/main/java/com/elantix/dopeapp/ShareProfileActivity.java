package com.elantix.dopeapp;

import android.app.ProgressDialog;
import android.net.Uri;
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
//import com.facebook.internal.AttributionIdentifiers;
//import com.facebook.internal.Utility;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.w3c.dom.Text;

import java.io.File;

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
    public ProgressDialog mProgressDialog;
    private FancyButton mFacebookBtn;
    private FancyButton mMoreBtn;
    private FancyButton mWhatsAppBtn;
    private FancyButton mInstagramBtn;
    private String mLink;
    private String username;

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
        mFacebookBtn = (FancyButton) findViewById(R.id.share_panel_facebook_button);
        mFacebookBtn.setOnClickListener(this);
        mWhatsAppBtn = (FancyButton) findViewById(R.id.share_panel_whatsapp_button);
        mWhatsAppBtn.setOnClickListener(this);
        mInstagramBtn = (FancyButton) findViewById(R.id.share_panel_instagram_button);
        mInstagramBtn.setOnClickListener(this);

        TextView linkField = (TextView) findViewById(R.id.share_profile_link_text);
        mLink = "http://dopeapi.elantix.net/user/"+Utilities.sCurProfile.id;
        linkField.setText(mLink);

        mUsername = (TextView) findViewById(R.id.share_profile_user_name);
        mFirstLastNames = (TextView) findViewById(R.id.share_profile_first_last_names);
        mAvatar = (CircularImageView) findViewById(R.id.share_profile_avatar);
        mAvatarPlaceholder = (ImageView) findViewById(R.id.profile_settings_user_icon);
        Boolean isOwn = getIntent().getBooleanExtra("own", false);

        if (Utilities.sCurProfile.avatar != null) {
            if (Utilities.sCurProfile.avatar.startsWith("http")) {
                Glide.with(this).load(Uri.parse(Utilities.sCurProfile.avatar)).into(mAvatar);
            }else {
                Glide.with(this).load(new File(Utilities.sCurProfile.avatar).getPath()).into(mAvatar);
            }
        }else{
            mAvatar.setVisibility(View.GONE);
            mAvatarPlaceholder.setVisibility(View.VISIBLE);
        }
        username = (Utilities.sCurProfile.username.isEmpty()) ? getResources().getString(R.string.profile_settings_username_placeholder_text) : Utilities.sCurProfile.username;
        mUsername.setText(username);
        String firstLastNames = (Utilities.sCurProfile.fullname.isEmpty()) ? getResources().getString(R.string.profile_settings_firstlastnames_placeholder_text) : Utilities.sCurProfile.fullname;
        mFirstLastNames.setText(firstLastNames);

        mMoreBtn = (FancyButton) findViewById(R.id.share_panel_more_button);
        mMoreBtn.setOnClickListener(this);
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
        }else if(id == mFacebookBtn.getId()){
            Utilities.shareOnFacebook(ShareProfileActivity.this, mLink, Uri.parse(Utilities.sCurProfile.avatar), username);
        }else if (id == mMoreBtn.getId()){
            Utilities.initShareIntent(ShareProfileActivity.this, Uri.parse(Utilities.sCurProfile.avatar), username, mLink);
        }else if(id == mWhatsAppBtn.getId()){
            Utilities.initShareIntent(ShareProfileActivity.this, Uri.parse(Utilities.sCurProfile.avatar), username, mLink, "com.whatsapp");
        }else if(id == mInstagramBtn.getId()){
            Utilities.initShareIntent(ShareProfileActivity.this, Uri.parse(Utilities.sCurProfile.avatar), username, mLink, "com.instagram.android");
        }
    }
}
