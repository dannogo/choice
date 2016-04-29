package com.elantix.dopeapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.IOException;


/**
 * Created by oleh on 4/4/16.
 */
public class ProfileSettingsActivity extends AppCompatActivity implements View.OnClickListener{

    private WindowManager mWindowManager;
    private View mStatusBarOverlay;
    private View mOverlay;
    private LinearLayout mContextOptionsPanel;
    private RelativeLayout mTakePhotoButton;
    private RelativeLayout mChooseFromLibraryButton;
    private RelativeLayout mSearchWebButton;
    private RelativeLayout mContextCancelButton;
    private RelativeLayout mAvatarContainer;

    private CircularImageView mAvatar;
    private ImageView mCameraIcon;
    private Uri mPhotoUri = null;
    private TextView mCancelToolbarBtn;
    private TextView mDoneToolbarBtn;
    private Uri mTempImage = null;
    private EditText mUsernameEdittext;
    private EditText mFirstLastNamesEdittext;
    private LinearLayout mChangeGender;
    private TextView mGenderTextField;
    private TextView mGenderAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        mAvatarContainer = (RelativeLayout) findViewById(R.id.profile_settings_avatar_container);
        mAvatarContainer.setOnClickListener(this);
        mCancelToolbarBtn = (TextView) findViewById(R.id.profile_settings_left_button);
        mDoneToolbarBtn = (TextView) findViewById(R.id.profile_settings_right_button);
        mCancelToolbarBtn.setOnClickListener(this);
        mDoneToolbarBtn.setOnClickListener(this);
        mUsernameEdittext = (EditText) findViewById(R.id.profile_edit_username_edittext);
        mChangeGender = (LinearLayout) findViewById(R.id.profile_settings_activity_change_gender);
        mChangeGender.setOnClickListener(this);
        mGenderTextField = (TextView) findViewById(R.id.profile_settings_activity_gender_textview);
        mGenderAction = (TextView) findViewById(R.id.profile_settings_activity_gender_action);

        mFirstLastNamesEdittext = (EditText) findViewById(R.id.profile_edit_first_last_names_edittext);

        if (!Utilities.sCurProfile.username.isEmpty()){
            mUsernameEdittext.setText(Utilities.sCurProfile.username);
        }
        if (!Utilities.sCurProfile.fullname.isEmpty()){
            mFirstLastNamesEdittext.setText(Utilities.sCurProfile.fullname);
        }



        // Replaced
//        if (!Utilities.profileFirstLastNames.isEmpty()){
//            mFirstLastNamesEdittext.setText(Utilities.profileFirstLastNames);
//        }
//        if (!Utilities.profileUsername.isEmpty()) {
//            mUsernameEdittext.setText(Utilities.profileUsername);
//        }
        // Replaced


        prepareContextPanelViews();


//        mTempImage = Utilities.avatarUri; // replaced


        mTempImage = (!Utilities.sCurProfile.avatar.isEmpty()) ? Uri.parse(Utilities.sCurProfile.avatar) : null;
        setProperAvatarState();

    }

    private void prepareContextPanelViews(){
        mOverlay = findViewById(R.id.profile_settings_activity_overlay);
        mOverlay.setOnClickListener(this);

        mContextOptionsPanel = (LinearLayout) findViewById(R.id.profile_settings_activity_context_options_panel);
        mContextOptionsPanel.setOnClickListener(this);
        mTakePhotoButton = (RelativeLayout) mContextOptionsPanel.findViewById(R.id.context_options_1);
        mTakePhotoButton.setOnClickListener(this);
        mChooseFromLibraryButton = (RelativeLayout) mContextOptionsPanel.findViewById(R.id.context_options_2);
        mChooseFromLibraryButton.setOnClickListener(this);
        mSearchWebButton = (RelativeLayout) mContextOptionsPanel.findViewById(R.id.context_options_3);
        mSearchWebButton.setOnClickListener(this);
        mContextCancelButton = (RelativeLayout) mContextOptionsPanel.findViewById(R.id.context_options_cancel);
        mContextCancelButton.setOnClickListener(this);


        mCameraIcon = (ImageView) findViewById(R.id.profile_settings_camera_icon);
        mAvatar = (CircularImageView) findViewById(R.id.profile_settings_avatar);
    }

    /**
     * Shows and Hides context options panel
     * @param show
     */
    private void showHideContextOptions(Boolean show){

        if (show){
            WindowManager.LayoutParams mLP = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    Utilities.getStatusBarHeight(ProfileSettingsActivity.this),
                    // Allows the view to be on top of the StatusBar
                    WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                    // Keeps the button presses from going to the background window
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            // Enables the notification to recieve touch events
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                            // Draws over status bar
                            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                    PixelFormat.TRANSLUCENT);

            mLP.gravity =  Gravity.TOP|Gravity.CENTER;

            mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            mStatusBarOverlay = new View(ProfileSettingsActivity.this);
            mStatusBarOverlay.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            mStatusBarOverlay.setBackgroundColor(ContextCompat.getColor(ProfileSettingsActivity.this, R.color.context_options_overlay_color));

            mWindowManager.addView(mStatusBarOverlay, mLP);

            mOverlay.setVisibility(View.VISIBLE);

            Animation bottomUp = AnimationUtils.loadAnimation(ProfileSettingsActivity.this,
                    R.anim.bottom_up);
            mContextOptionsPanel.startAnimation(bottomUp);
            mContextOptionsPanel.setVisibility(View.VISIBLE);
        }else{
            mWindowManager.removeView(mStatusBarOverlay);

            mOverlay.setVisibility(View.GONE);

            Animation bottomDown = AnimationUtils.loadAnimation(ProfileSettingsActivity.this,
                    R.anim.bottom_down);
            mContextOptionsPanel.startAnimation(bottomDown);
            mContextOptionsPanel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mAvatarContainer.getId()){
            showHideContextOptions(true);
        }else if (id == mOverlay.getId()){
            showHideContextOptions(false);
        }else if (id == mContextCancelButton.getId()){
            showHideContextOptions(false);
        }else if (id == mTakePhotoButton.getId()){
            takePicture();
            showHideContextOptions(false);
        }else if (id == mChooseFromLibraryButton.getId()){
            Utilities.chooseFromLibrary(ProfileSettingsActivity.this);
            showHideContextOptions(false);
        }else if (id == mSearchWebButton.getId()){
            Intent intent = new Intent(ProfileSettingsActivity.this, SearchWebActivity.class);
            startActivityForResult(intent, Utilities.PICK_IMAGE_FROM_WEB);
            showHideContextOptions(false);
        }else if (id == mCancelToolbarBtn.getId()){
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }else if (id == mDoneToolbarBtn.getId()){
            if (mTempImage != null) {
//                Utilities.avatarUri = mTempImage;
                Utilities.sCurProfile.avatar = mTempImage.toString();
            }
//            Utilities.profileUsername = mUsernameEdittext.getText().toString();
//            Utilities.profileFirstLastNames = mFirstLastNamesEdittext.getText().toString();

            Utilities.sCurProfile.username = mUsernameEdittext.getText().toString();
            Utilities.sCurProfile.fullname = mFirstLastNamesEdittext.getText().toString();

            Intent returnIntent = new Intent();
//            returnIntent.putExtra("result",result);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }else if (id == mChangeGender.getId()){
            final Dialog dialog = new Dialog(ProfileSettingsActivity.this);
            dialog.setContentView(R.layout.gender_pick_dialog);
            dialog.setTitle("Pick gender");
            dialog.setCancelable(true);

            RadioButton rd1 = (RadioButton) dialog.findViewById(R.id.male);
            RadioButton rd2 = (RadioButton) dialog.findViewById(R.id.female);
            rd1.setButtonDrawable(android.R.color.transparent);
            rd2.setButtonDrawable(android.R.color.transparent);
            rd1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mGenderTextField.setText(R.string.profile_gender_male);
                    mGenderAction.setText(R.string.profile_gender_action_change_gender);
                    dialog.dismiss();
                }
            });
            rd2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mGenderTextField.setText(R.string.profile_gender_female);
                    mGenderAction.setText(R.string.profile_gender_action_change_gender);
                    dialog.dismiss();
                }
            });

            // now that the dialog is set up, it's time to show it
            dialog.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utilities.CAPTURE_IMAGE_WITH_CAMERA && resultCode == Activity.RESULT_OK) {
            putSelectedImageAsAvatar(null);
        }else if (requestCode == Utilities.PICK_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK){
            putSelectedImageAsAvatar(data.getData());
        }else if (requestCode == Utilities.PICK_IMAGE_FROM_WEB && resultCode == Activity.RESULT_OK){
            int imagePosition = data.getIntExtra("result", 999);
            Toast.makeText(this, "Image: " + imagePosition, Toast.LENGTH_SHORT).show();
        }

    }

    private void putSelectedImageAsAvatar(Uri selectedImage){
        if (selectedImage == null) {
            mTempImage = mPhotoUri;
        }else {
            mTempImage = selectedImage;
        }
        setProperAvatarState();
    }


    private void setProperAvatarState(){
        if (mTempImage == null){
            mCameraIcon.setVisibility(View.VISIBLE);
            mAvatar.setVisibility(View.GONE);
        }else{
            mCameraIcon.setVisibility(View.GONE);
            mAvatar.setVisibility(View.VISIBLE);
            Glide.with(this).load(mTempImage).into(mAvatar);
        }
    }

    /**
     * Creates new dir and file for picture and launches camera with ACTION_IMAGE_CAPTURE Intent
     */
    private void takePicture(){
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/DopeAppPics/";
        File newdir = new File(dir);
        newdir.mkdirs();
        String randomString = Utilities.createRandomFileName();
        String file = dir+randomString+".jpg";
        File newfile = new File(file);

        try {
            newfile.createNewFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        mPhotoUri = Uri.fromFile(newfile);
        Utilities.hasPermissionInManifest(ProfileSettingsActivity.this, MediaStore.ACTION_IMAGE_CAPTURE);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
        startActivityForResult(cameraIntent, Utilities.CAPTURE_IMAGE_WITH_CAMERA);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWindowManager != null && mStatusBarOverlay != null && mStatusBarOverlay.getParent() != null) {
            mWindowManager.removeView(mStatusBarOverlay);

        }
        mOverlay.setVisibility(View.GONE);
        mContextOptionsPanel.setVisibility(View.GONE);

    }
}
