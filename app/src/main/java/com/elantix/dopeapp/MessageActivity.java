package com.elantix.dopeapp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 3/19/16.
 */
public class MessageActivity extends AppCompatActivity {

    private android.app.FragmentManager manager = getFragmentManager();
    private FragmentTransaction transaction;
    protected DirectMessages directMessages = DirectMessages.NoMessage;
    protected Toolbar toolbar;
    TextView toolbarTitle;
    ImageButton leftToolbarButton;
    ImageButton rightToolbarButton;
    RelativeLayout bottomArea;
    FancyButton fancyButton;
    private HttpKit http;

    public Fragment mCurrentFragment;

    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
//        launchFragment(directMessages);

        toolbar = (Toolbar) findViewById(R.id.main_app_bar);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.message_title_1);
        fancyButton = (FancyButton) findViewById(R.id.fancy_button);
        leftToolbarButton = (ImageButton) toolbar.findViewById(R.id.left_button);
        leftToolbarButton.setImageResource(R.drawable.tollbar_left_arrow_dark);
        rightToolbarButton = (ImageButton) toolbar.findViewById(R.id.right_button);
        rightToolbarButton.setVisibility(View.GONE);
        rightToolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        bottomArea = (RelativeLayout) findViewById(R.id.bottom_area);

        http = new HttpKit(MessageActivity.this);
        http.getConversationList(Utilities.sToken, Utilities.sUid, null, null);
    }



    private void toolbarStuffChangeHandler(DirectMessages fragEnum){
//        NoMessage, NewMessage1, Chat, GroupChat, GroupChatSettings, TabPlus
        switch (fragEnum){
            case NoMessage:
                toolbarTitle.setText(R.string.message_title_1);
                break;
        }
    }

    // new approach
    private void removeFragment(){
        if(mCurrentFragment != null) {
            transaction = manager.beginTransaction();
            transaction.remove(mCurrentFragment);
            transaction.commit();
        }
    }

    public void switchPageHandler(DirectMessages fragEnum){
        switchPageHandler(fragEnum, -1);
    }

    protected void switchPageHandler(DirectMessages fragEnum, int bundleData){
        removeFragment();
        directMessages = fragEnum;
        launchFragment(directMessages, bundleData);
    }

    public void launchFragment(DirectMessages directMessages){
        launchFragment(directMessages, -1);
    }

    public void launchFragment(DirectMessages directMessages, int bundleData){
        Fragment frag;
        String fragmentTAG;
        switch (directMessages){
            case NoMessage:
                mCurrentFragment = new FragmentNoMessage();
                fragmentTAG = "FragmentNoMessage";
                break;
            case NewMessage1:
                mCurrentFragment = new FragmentNewMessage();
                fragmentTAG = "FragmentNewMessage1";
                break;
//            case Chat:
//
//                break;
//            case GroupChat:
//
//                break;
//            case GroupChatSettings:
//
//                break;
            case TabPlus:
                mCurrentFragment = new FragmentTabPlus();
                fragmentTAG = "FragmentTabPlus";
                break;
            default:
                mCurrentFragment = new FragmentNoMessage();
                fragmentTAG = "FragmentNoMessage";
        }
        Bundle bundle = new Bundle();
        bundle.putInt("type", bundleData);
        mCurrentFragment.setArguments(bundle);

        transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, mCurrentFragment, fragmentTAG);
        transaction.commit();
    }

    public enum DirectMessages{
        NoMessage, NewMessage1, Chat, GroupChat, GroupChatSettings, TabPlus
    }

}
