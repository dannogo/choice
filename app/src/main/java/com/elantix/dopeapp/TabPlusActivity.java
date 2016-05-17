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

}
