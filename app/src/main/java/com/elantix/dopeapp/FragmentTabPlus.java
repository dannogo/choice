package com.elantix.dopeapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.elantix.dopeapp.utils.DecodeUtils;

import java.io.File;
import java.io.FileOutputStream;
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
    private String mDir;


    private ImageView leftPlusButton;
    private ImageView rightPlusButton;
    private RelativeLayout leftWorkingPart;
    private RelativeLayout rightWorkingPart;
    private ImageButton leftToolbarButton;
    private EditText usersQuestion;
    private ImageViewTouch imageLeft;
    private ImageViewTouch imageRight;
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
        mDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/DopeAppPics/";

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
//        mDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/DopeAppPics/";
        File newdir = new File(mDir);
        newdir.mkdirs();
        String randomString = Utilities.createRandomFileName();
        String file = mDir+randomString+".jpg";
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

        Utilities.hasPermissionInManifest(getActivity(), MediaStore.ACTION_IMAGE_CAPTURE);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentUri);

        startActivityForResult(cameraIntent, Utilities.CAPTURE_IMAGE_WITH_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utilities.CAPTURE_IMAGE_WITH_CAMERA && resultCode == Activity.RESULT_OK) {
            setImageIntoImageViewTouch(currentSide, ImageSource.Camera, null);
        }else if (requestCode == Utilities.PICK_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK){
            Uri selectedImage = data.getData();
            setImageIntoImageViewTouch(currentSide, ImageSource.Gallery, selectedImage);
        }
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

//        if (currentUri != null) {
//            getActivity().getContentResolver().delete(currentUri, null, null);
//        }

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
        imageLeft.setDrawingCacheEnabled(true);
        imageRight = (ImageViewTouch) fragmentView.findViewById(R.id.imageRight);
        imageRight.setDisplayType(ImageViewTouchBase.DisplayType.FIT_IF_BIGGER);
        imageRight.setDrawingCacheEnabled(true);
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
                    Utilities.getStatusBarHeight(getActivity()),
                    // Allows the view to be on top of the StatusBar
                    WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                    // Keeps the button presses from going to the background window
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
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
            try {
                mWindowManager.removeView(statusBarOverlay);
            }catch (IllegalArgumentException e){
                Log.e("FragmentTablPlus", "statusBarOverlay not found");
            }

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

    @Override
    public void onClick(View v) {
        if (v.getId() == sendNewMessageButton.getId()){

            boolean hasLeftImage = (imageLeft.getDrawable() != null);
            boolean hasRightImage = (imageRight.getDrawable() != null);
            boolean hasQuestion = !usersQuestion.getText().toString().isEmpty();
            if(hasLeftImage && hasRightImage && hasQuestion) {

                ImageResizeSaveTask task = new ImageResizeSaveTask();
                task.execute();

            }else{

                String dialogMessage;
                if (!hasLeftImage && !hasRightImage && !hasQuestion){
                    dialogMessage = "Add both images and ask a question";
                }else if (!hasLeftImage && !hasRightImage && hasQuestion) {
                    dialogMessage = "Add both images";
                }else if (hasLeftImage && hasRightImage && !hasQuestion){
                    dialogMessage = "Ask a question";
                }else if (hasLeftImage && !hasRightImage && hasQuestion){
                    dialogMessage = "Add left image";
                }else if (!hasLeftImage && hasRightImage && hasQuestion) {
                    dialogMessage = "Add right image";
                }else if(!hasLeftImage && hasRightImage && !hasQuestion){
                    dialogMessage = "Add left image and ask a question";
                }else if(hasLeftImage && !hasRightImage && !hasQuestion){
                    dialogMessage = "Add right image and ask a question";
                }else{
                    dialogMessage = "Add both images and ask a question";
                }

                new AlertDialog.Builder(getActivity())
                        .setTitle("Almost Done")
                        .setMessage(dialogMessage)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
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
            getActivity().startActivityForResult(intent, Utilities.PICK_IMAGE_FROM_WEB);
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
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Utilities.PICK_IMAGE_FROM_GALLERY);
    }


    class ImageResizeSaveTask extends AsyncTask<Void, Void, String[]>{

        private Bitmap leftBitmap;
        private Bitmap rightBitmap;
        private String TAG = "FragmentTabPlus Task";
        TabPlusActivity activity = ((TabPlusActivity)getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            activity.mProgressDialog = ProgressDialog.show(getActivity(), null, "Preparing to upload...", true);
            imageLeft.destroyDrawingCache();
            imageRight.destroyDrawingCache();
            imageLeft.buildDrawingCache();
            imageRight.buildDrawingCache();
            leftBitmap = imageLeft.getDrawingCache(true);
            rightBitmap = imageRight.getDrawingCache(true);

        }

        @Override
        protected String[] doInBackground(Void... params) {

            File fileList = new File(mDir);
            if (fileList != null){
                File[] filenames = fileList.listFiles();
                for (File tmpf : filenames){
                    tmpf.delete();
                }
            }

            float scaleCoefficient = (float) 320 / (float) leftBitmap.getWidth();

            Bitmap newLeftbmp = Utilities.createTrimmedBitmap(Bitmap.createScaledBitmap(leftBitmap, 320, (int) ((float) leftBitmap.getHeight() * scaleCoefficient), true));
            Bitmap newRightbmp = Utilities.createTrimmedBitmap(Bitmap.createScaledBitmap(rightBitmap, 320, (int) ((float)rightBitmap.getHeight() * scaleCoefficient), true));

            String randomString1 = Utilities.createRandomFileName();
//            String file1 = mDir+randomString1+".png";
            String file1 = mDir+randomString1+".jpeg";
            File newfile1 = new File(file1);

            String randomString2 = Utilities.createRandomFileName();
//            String file2 = mDir+randomString2+".png";
            String file2 = mDir+randomString2+".jpeg";
            File newfile2 = new File(file2);

            Log.d(TAG, "file1: "+file1+"\nfile2: "+file2);

            FileOutputStream outLeft = null;
            FileOutputStream outRight = null;
            try {
                outLeft = new FileOutputStream(newfile1);
                outRight = new FileOutputStream(newfile2);
//                newLeftbmp.compress(Bitmap.CompressFormat.PNG, 100, outLeft);
//                newRightbmp.compress(Bitmap.CompressFormat.PNG, 100, outRight);
                newLeftbmp.compress(Bitmap.CompressFormat.JPEG, 100, outLeft);
                newRightbmp.compress(Bitmap.CompressFormat.JPEG, 100, outRight);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (outLeft != null) {
                        outLeft.close();
                    }
                    if (outRight != null) {
                        outRight.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String[] result = {file1, file2};
            return result;
        }

        @Override
        protected void onPostExecute(final String[] result) {
            super.onPostExecute(result);
            Log.w(TAG, "left: "+ result[0]+"\nright: "+ result[1]);

            activity.uploadedPicture1 = null;
            activity.uploadedPicture2 = null;


            if (activity.mDialogId == -1){
                HttpKit http = new HttpKit(getActivity());
                http.createDope(Utilities.sToken, usersQuestion.getText().toString(), result[0], result[1]);
            }else{
                HttpChat http = new HttpChat(getActivity());
                http.sendDopeToChat(Utilities.sToken, String.valueOf(activity.mDialogId), usersQuestion.getText().toString(), result[0], result[1], String.valueOf(activity.mChatExists));
            }




        }
    }

}
