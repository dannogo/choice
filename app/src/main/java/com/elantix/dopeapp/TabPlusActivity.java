package com.elantix.dopeapp;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by oleh on 3/24/16.
 */
public class TabPlusActivity extends AppCompatActivity {

    PlusPage page = PlusPage.Plus;
    private android.app.FragmentManager manager = getFragmentManager();
    private FragmentTransaction transaction;
    public ProgressDialog mProgressDialog;
    public String uploadedPicture1;
    public String uploadedPicture2;
    public String dopeQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_plus);

        launchFragment(page);

    }

    // Not in use
    public void createDope(){
        HttpKit http = new HttpKit(TabPlusActivity.this);
        http.createDope(Utilities.sToken, dopeQuestion, uploadedPicture1, uploadedPicture2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utilities.PICK_IMAGE_FROM_WEB && resultCode == Activity.RESULT_OK){
            int imagePosition = data.getIntExtra("result", 999);
            Toast.makeText(this, "Image: "+imagePosition, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Launches chosen fragment
     * @param page
     */
    public void launchFragment(PlusPage page){
        Fragment frag;
        String fragmentTAG;
        switch (page){
            case Plus:
                frag = new FragmentTabPlus();
                fragmentTAG = "FragmentTabPlus";
                break;
            case Done:
                frag = new FragmentTabPlusDone();
                fragmentTAG = "FragmentTabPlusDone";
                break;
            default:
                frag = new FragmentTabPlus();
                fragmentTAG = "FragmentTabPlus";
        }
        transaction = manager.beginTransaction();
        transaction.add(R.id.activity_tab_plus_fragment_container, frag, fragmentTAG);
        transaction.commit();

    }

    /**
     * Removes previous Fragment and lauches new one
     * @param newPage
     */
    protected void switchPageHandler(PlusPage newPage){
        removeFragment(page);
        page = newPage;
        launchFragment(page);
    }

    /**
     * Removes fragment from activity
     * @param page
     */
    private void removeFragment(PlusPage page){

        String fragmentTAG;
        switch (page){
            case Plus:
                fragmentTAG = "FragmentTabPlus";
                break;
            case Done:
                fragmentTAG = "FragmentTabPlusDone";
                break;
            default:
                fragmentTAG = "FragmentTabPlus";
        }
        Fragment fragment = getFragmentManager().findFragmentByTag(fragmentTAG);
        if(fragment != null) {
            transaction = manager.beginTransaction();
            transaction.remove(fragment);
            transaction.commit();
        }
    }

    public enum PlusPage{
        Plus, Done
    }

//    public void resizeAndSaveImages(Bitmap leftBmp, Bitmap rightBmp, Uri leftUri, Uri rightUri){
//        Object[] params = {leftBmp, rightBmp, leftUri, rightUri};
//        ImageResizeSaveTask task = new ImageResizeSaveTask();
//        task.execute(params);
//    }
//
//    class ImageResizeSaveTask extends AsyncTask<Object, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(Object... params) {
//            Bitmap leftBmp = (Bitmap) params[0];
//            Bitmap rightBmp = (Bitmap) params[1];
//            Uri leftUri = (Uri) params[2];
//            Uri rightUri = (Uri) params[3];
//
//            float scaleFactor = leftBmp.getWidth()/320;
//            Bitmap newLeftbmp = Bitmap.createScaledBitmap(leftBmp, 320, (int)(leftBmp.getHeight()*scaleFactor), true);
//            Bitmap newRightbmp = Bitmap.createScaledBitmap(rightBmp, 320, (int)(rightBmp.getHeight()*scaleFactor), true);
////            Log.w("FragmentTabPlus", "left: "+newLeftbmp);
//
//            FileOutputStream outLeft = null;
//            FileOutputStream outRight = null;
//            try {
//                outLeft = new FileOutputStream(leftUri.getPath());
//                outRight = new FileOutputStream(rightUri.getPath());
//                newLeftbmp.compress(Bitmap.CompressFormat.PNG, 100, outLeft);
//                newRightbmp.compress(Bitmap.CompressFormat.PNG, 100, outRight);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    if (outLeft != null) {
//                        outLeft.close();
//                    }
//                    if (outRight != null) {
//                        outRight.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//        }
//    }
}
