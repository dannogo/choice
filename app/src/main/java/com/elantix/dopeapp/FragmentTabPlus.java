package com.elantix.dopeapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elantix.dopeapp.utils.DecodeUtils;

import java.io.File;
import java.io.IOException;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 3/19/16.
 */
public class FragmentTabPlus extends Fragment implements View.OnClickListener{

    View fragmentView;
    private FancyButton sendNewMessageButton;

    private ImageView leftPlusButton;
    private ImageView rightPlusButton;
    private RelativeLayout leftWorkingPart;
    private RelativeLayout rightWorkingPart;
    private ImageButton leftToolbarButton;
    private EditText usersQuestion;
    private ImageViewTouch imageLeft;
    private ImageViewTouch imageRight;
    private static final int CAPTURE_IMAGE_WITH_CAMERA = 1888;
    private static final int PICK_IMAGE_FROM_GALLERY = 1887;
    private static final int PICK_IMAGE_FROM_WEB = 1886;
    private Uri[] outputFileUris = {null, null};
    private Side currentSide;
    private View overlay;
    private LinearLayout contextOptionsPanel;
    private RelativeLayout contextOptionsTakePhoto;
    private RelativeLayout contextOptionsChooseFromLibrary;
    private RelativeLayout contextOptionsSearchWeb;
    private RelativeLayout contextOptionsCancel;

    WindowManager mWindowManager;
    View statusBarOverlay;

    // TODO:
    // set back button behaviour both for toolbar back button and back navigation bar button
    // handle checking presence of both pictures to going forward (Dialog)
    // Add search web
    // get pictures from views to send them to the server
    // delete taken pictures before going to the next page

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_tab_plus, container, false);
        getViewObjectsFromXml();
        sendNewMessageButton.setText(getActivity().getResources().getString(R.string.tab_plus_button_text));
        buttonsAppearanceHandling();
        TextView title = (TextView) fragmentView.findViewById(R.id.toolbar_title);
        title.setText(getActivity().getResources().getString(R.string.tab_plus_button_text));
        leftToolbarButton.setImageResource(R.drawable.toolbar_left_arrow);

        return fragmentView;
    }

    private enum Side{
        Left, Right
    }

    private enum ImageSource{
        Camera, Gallery, Web
    }

    /**
     * Creates new dir and file for picture and launches camera with ACTION_IMAGE_CAPTURE Intent
     * @param side
     */
    private void takePicture(Side side){
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/DopeAppPics/";
        File newdir = new File(dir);
        newdir.mkdirs();
        String randomString = Utilites.createRandomFileName();
        String file = dir+randomString+".jpg";
        File newfile = new File(file);
        currentSide = side;

        try {
            newfile.createNewFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Uri currentUri = Uri.fromFile(newfile);
        if (side == Side.Left) {
            outputFileUris[0] = Uri.fromFile(newfile);
        }else if (side == Side.Right){
            outputFileUris[1] = Uri.fromFile(newfile);
        }

        Utilites.hasPermissionInManifest(getActivity(), MediaStore.ACTION_IMAGE_CAPTURE);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentUri);

        startActivityForResult(cameraIntent, CAPTURE_IMAGE_WITH_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.w("On Activity Result", "Occur");
        if (requestCode == CAPTURE_IMAGE_WITH_CAMERA && resultCode == Activity.RESULT_OK) {
            setImageIntoImageViewTouch(currentSide, ImageSource.Camera, null);
        }else if (requestCode == PICK_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK){
            Uri selectedImage = data.getData();
            setImageIntoImageViewTouch(currentSide, ImageSource.Gallery, selectedImage);
        }

        // Moved to TabPlusActivity
//        else if (requestCode == PICK_IMAGE_FROM_WEB && resultCode == Activity.RESULT_OK){
//            Log.w("On Activity Result", "My case Occurs");
//            int imagePosition = data.getIntExtra("result", 999);
//            Toast.makeText(getActivity(), "Image: "+imagePosition, Toast.LENGTH_SHORT).show();
//        }
    }


    /**
     * Sets an image from uri to the chosen side
     * @param side
     */
    public void setImageIntoImageViewTouch(Side side, ImageSource source, Uri selectedImage){

        int size = (int)(Math.min(leftWorkingPart.getWidth(), leftWorkingPart.getHeight())/0.55);
        final ImageViewTouch inWhichToPut;
        Uri currentUri;

        switch (side){
            case Left:
                inWhichToPut = imageLeft;
                currentUri = outputFileUris[0];
                break;
            case Right:
                inWhichToPut = imageRight;
                currentUri = outputFileUris[1];
                break;
            default:
                inWhichToPut = imageLeft;
                currentUri = outputFileUris[0];
        }

        Bitmap bitmap;

        switch (source){
            case Camera:
                bitmap = DecodeUtils.decode(getActivity(), currentUri, size, size);
                break;
            case Gallery:
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                } catch (IOException e) {
                    bitmap = DecodeUtils.decode(getActivity(), currentUri, size, size);
                    e.printStackTrace();
                }
                break;
            default:
                bitmap = DecodeUtils.decode(getActivity(), currentUri, size, size);
        }
        if (null != bitmap) {

            inWhichToPut.setOnDrawableChangedListener(
                    new ImageViewTouchBase.OnDrawableChangeListener() {
                        @Override
                        public void onDrawableChanged(final Drawable drawable) {

                        }
                    }
            );
            inWhichToPut.setImageBitmap(bitmap, null, -1, -1);

        } else {
            Toast.makeText(getActivity(), "Failed to load the image", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * finds views by id and sets OnClickListeners for some of them
     */
    private void getViewObjectsFromXml(){
        sendNewMessageButton = (FancyButton) fragmentView.findViewById(R.id.send_new_message);
        sendNewMessageButton.setOnClickListener(this);
        usersQuestion = (EditText) fragmentView.findViewById(R.id.users_question);
        leftWorkingPart = (RelativeLayout) fragmentView.findViewById(R.id.left_working_part);
        rightWorkingPart = (RelativeLayout) fragmentView.findViewById(R.id.right_working_part);
        leftWorkingPart.setOnClickListener(this);
        rightWorkingPart.setOnClickListener(this);
        leftPlusButton = (ImageView) fragmentView.findViewById(R.id.left_plus_button);
        rightPlusButton = (ImageView) fragmentView.findViewById(R.id.right_plus_button);
        leftPlusButton.setOnClickListener(this);
        rightPlusButton.setOnClickListener(this);
        rightWorkingPart.setOnClickListener(this);
        ImageButton rightToolbarButton = (ImageButton) fragmentView.findViewById(R.id.right_button);
        rightToolbarButton.setVisibility(View.GONE);
        leftToolbarButton = (ImageButton) fragmentView.findViewById(R.id.left_button);
        leftToolbarButton.setOnClickListener(this);
        imageLeft = (ImageViewTouch) fragmentView.findViewById(R.id.imageLeft);
        imageLeft.setDisplayType(ImageViewTouchBase.DisplayType.FIT_IF_BIGGER);
        imageRight = (ImageViewTouch) fragmentView.findViewById(R.id.imageRight);
        imageRight.setDisplayType(ImageViewTouchBase.DisplayType.FIT_IF_BIGGER);
        overlay = fragmentView.findViewById(R.id.overlay);
        overlay.setOnClickListener(this);

        contextOptionsPanel = (LinearLayout) fragmentView.findViewById(R.id.context_options_panel);
        contextOptionsPanel.setOnClickListener(this);
        contextOptionsTakePhoto = (RelativeLayout) contextOptionsPanel.findViewById(R.id.context_options_1);
        contextOptionsTakePhoto.setOnClickListener(this);
        contextOptionsChooseFromLibrary = (RelativeLayout) contextOptionsPanel.findViewById(R.id.context_options_2);
        contextOptionsChooseFromLibrary.setOnClickListener(this);
        contextOptionsSearchWeb = (RelativeLayout) contextOptionsPanel.findViewById(R.id.context_options_3);
        contextOptionsSearchWeb.setOnClickListener(this);
        contextOptionsCancel = (RelativeLayout) contextOptionsPanel.findViewById(R.id.context_options_cancel);
        contextOptionsCancel.setOnClickListener(this);
    }

    /**
     * Programmatically decreases icon size because FancyButtons library doesn`t allow this using xml
     */
    private void buttonsAppearanceHandling(){
        android.view.ViewGroup.LayoutParams params = sendNewMessageButton.getLayoutParams();
        int height = params.height;

        sendNewMessageButton.getIconImageObject().setLayoutParams(new LinearLayout.LayoutParams(80, 80));
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        float n = getActivity().getResources().getDimension(R.dimen.friends_profile_message_margin_4);
        lp1.setMargins(0, (int) n, 0, 0);
        lp1.addRule(RelativeLayout.CENTER_IN_PARENT);
        sendNewMessageButton.setLayoutParams(lp1);
    }


    /**
     * Shows and Hides context options panel
     * @param show
     */
    private void showHideContextOptions(Boolean show){

        if (show){
            WindowManager.LayoutParams mLP = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(),
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

            mWindowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
            statusBarOverlay = new View(getActivity());
            statusBarOverlay.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            statusBarOverlay.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.context_options_overlay_color));

            mWindowManager.addView(statusBarOverlay, mLP);

            overlay.setVisibility(View.VISIBLE);

            Animation bottomUp = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.bottom_up);
            contextOptionsPanel.startAnimation(bottomUp);
            contextOptionsPanel.setVisibility(View.VISIBLE);
        }else{
            mWindowManager.removeView(statusBarOverlay);

            overlay.setVisibility(View.GONE);

            Animation bottomDown = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.bottom_down);
            contextOptionsPanel.startAnimation(bottomDown);
            contextOptionsPanel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWindowManager != null && statusBarOverlay != null && statusBarOverlay.getParent() != null) {
            mWindowManager.removeView(statusBarOverlay);

        }
            overlay.setVisibility(View.GONE);
            contextOptionsPanel.setVisibility(View.GONE);

    }

    /**
     * Returns height of status bar
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == sendNewMessageButton.getId()){

            boolean hasLeftImage = (imageLeft.getDrawable() != null);
            boolean hasRightImage = (imageRight.getDrawable() != null);
            boolean hasQuestion = !usersQuestion.getText().toString().isEmpty();
            if(hasLeftImage && hasRightImage && hasQuestion) {
                ((TabPlusActivity)getActivity()).switchPageHandler(TabPlusActivity.PlusPage.Done);
            }else{
                Toast.makeText(getActivity(), "left: " + hasLeftImage + "\nright: " + hasRightImage + "\nquestion: " + hasQuestion, Toast.LENGTH_SHORT).show();
            }

        }else if (v.getId() == leftWorkingPart.getId()){
        }else if(v.getId() == rightWorkingPart.getId()){
        }else if (v.getId() == leftPlusButton.getId()){
            currentSide = Side.Left;
            showHideContextOptions(true);
        }else if (v.getId() == rightPlusButton.getId()){
            currentSide = Side.Right;
            showHideContextOptions(true);
        }else if (v.getId() == leftToolbarButton.getId()){
            getActivity().finish();
        }else if(v.getId() == overlay.getId()){
            showHideContextOptions(false);
        }else if(v.getId() == contextOptionsTakePhoto.getId()){
            showHideContextOptions(false);
            takePicture(currentSide);
        }
        else if(v.getId() == contextOptionsChooseFromLibrary.getId()){
            chooseFromLibrary();
        }
        else if(v.getId() == contextOptionsSearchWeb.getId()){
            Intent intent = new Intent(getActivity(), SearchWebActivity.class);
            getActivity().startActivityForResult(intent, PICK_IMAGE_FROM_WEB);
        }
        else if(v.getId() == contextOptionsCancel.getId()){
            showHideContextOptions(false);
        }else if(v.getId() == contextOptionsPanel.getId()){
//            Log.w("Log", "Options Panel");
        }
    }




    /**
     * lauches ACTION_GET_CONTENT (choosing picture from phone`s gallery)
     */
    private void chooseFromLibrary(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_FROM_GALLERY);
    }
}
