package com.elantix.dopeapp;

import android.app.Fragment;
import android.app.FragmentTransaction;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        launchFragment(directMessages);

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


    }

    private void removeFragment(DirectMessages fragEnum){

        String fragmentTAG;
        switch (fragEnum){
            case NoMessage:
                fragmentTAG = "FragmentNoMessage";
                break;
            case NewMessage1:
                fragmentTAG = "FragmentNewMessage1";
                break;
            case TabPlus:
                fragmentTAG = "FragmentTabPlus";
                break;
            default:
                fragmentTAG = "FragmentNoMessage";
        }
        Fragment fragment = getFragmentManager().findFragmentByTag(fragmentTAG);
        if(fragment != null) {
            transaction = manager.beginTransaction();
            transaction.remove(fragment);
            transaction.commit();
        }
    }

    protected void switchPageHandler(DirectMessages fragEnum, int title){
        removeFragment(directMessages);
        directMessages = fragEnum;
        toolbarTitle.setText(title);
        launchFragment(directMessages);
    }

    private void launchFragment(DirectMessages directMessages){
        Fragment frag;
        String fragmentTAG;
        switch (directMessages){
            case NoMessage:
                frag = new FragmentNoMessage();
                fragmentTAG = "FragmentNoMessage";
                break;
            case NewMessage1:
                frag = new FragmentNewMessage();
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
                frag = new FragmentTabPlus();
                fragmentTAG = "FragmentTabPlus";
                break;
            default:
                frag = new FragmentNoMessage();
                fragmentTAG = "FragmentNoMessage";
        }
        transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, frag, fragmentTAG);
        transaction.commit();
    }

    public enum DirectMessages{
        NoMessage, NewMessage1, Chat, GroupChat, GroupChatSettings, TabPlus
    }

}
