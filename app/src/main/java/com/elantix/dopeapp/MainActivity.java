package com.elantix.dopeapp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by oleh on 3/14/16.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private LinearLayout lowerTab;

    private LinearLayout dailyDopeLL;
    private ImageView dailyDopeIcon;
    private TextView dailyDopeText;

    private LinearLayout trandingLL;
    private ImageView trandingIcon;
    private TextView trandingText;

    private LinearLayout plusLL;

    private LinearLayout friendsLL;
    private ImageView friendsIcon;
    private TextView friendsText;

    private LinearLayout profileLL;
    private ImageView profileIcon;
    private TextView profileText;

    private TextView toolbarTitle;
    private android.app.FragmentManager manager = getFragmentManager();
    private FragmentTransaction transaction;

    Page page = Page.Daily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.main_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        findViews();
        launchFragment(page);



    }

    private void findViews(){
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        lowerTab = (LinearLayout) findViewById(R.id.lower_tab);

        dailyDopeLL = (LinearLayout) findViewById(R.id.daily_dope_linlay);
        dailyDopeLL.setOnClickListener(this);
        trandingLL = (LinearLayout) findViewById(R.id.tranding_linlay);
        trandingLL.setOnClickListener(this);
        plusLL = (LinearLayout) findViewById(R.id.plus_linlay);
        plusLL.setOnClickListener(this);
        friendsLL = (LinearLayout) findViewById(R.id.friends_linlay);
        friendsLL.setOnClickListener(this);
        profileLL = (LinearLayout) findViewById(R.id.profile_linlay);
        profileLL.setOnClickListener(this);
    }

    private void launchFragment(Page page){
        Fragment frag;
        String fragmentTAG;
        Bundle bundle;
        switch (page){
            case Daily:
                frag = new FragmentDailyDope();
                bundle = new Bundle();
                bundle.putInt("num", 1);
                frag.setArguments(bundle);
                fragmentTAG = "FragmentDailyDope";
                break;
            case Tranding:
                frag = new FragmentDailyDope();
//                frag = new FragmentNoMessage();
                bundle = new Bundle();
                bundle.putInt("num", 2);
                frag.setArguments(bundle);
//                fragmentTAG = "FragmentNoMessage";
                fragmentTAG = "FragmentTranding";
                break;
            case Friends:
                frag = new FragmentFriendsFirstScreen();
                fragmentTAG = "FragmentFriendsFirstScreen";
                break;
            case Profile:
                frag = new FragmentFriendsNotRegistered();
                fragmentTAG = "FragmentFriendsNotRegistered";
                break;
            default:
                frag = new FragmentDailyDope();
                fragmentTAG = "FragmentDailyDope";
        }
        transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, frag, fragmentTAG);
        transaction.commit();
    }

    private void removeFragment(Page page){

        String fragmentTAG;
        switch (page){
            case Daily:
                fragmentTAG = "FragmentDailyDope";
                break;
            case Tranding:
                fragmentTAG = "FragmentTranding";
//                fragmentTAG = "FragmentNoMessage";
                break;
            case Friends:
                fragmentTAG = "FragmentFriendsFirstScreen";
                break;
            case Profile:
                fragmentTAG = "FragmentFriendsNotRegistered";
                break;
            default:
                fragmentTAG = "FragmentDailyDope";
        }
        Fragment fragment = getFragmentManager().findFragmentByTag(fragmentTAG);
        if(fragment != null) {
            transaction = manager.beginTransaction();
            transaction.remove(fragment);
            transaction.commit();
        }
    }

    private void switchPageHandler(Page newPage, int title){
        uncheckLowertabItem(page);
        removeFragment(page);
        page = newPage;
        checkLowertabItem(page);
        toolbarTitle.setText(title);
        launchFragment(page);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == dailyDopeLL.getId()){
            if (page != Page.Daily){
                switchPageHandler(Page.Daily, R.string.lower_tab_daily_dope);
            }
        }else if(id == trandingLL.getId()){
            if (page != Page.Tranding){
                switchPageHandler(Page.Tranding, R.string.lower_tab_tranding);
            }
        }else if (id == plusLL.getId()){
            Toast.makeText(MainActivity.this, "Plus", Toast.LENGTH_SHORT).show();
        }else if (id == friendsLL.getId()){
            if (page != Page.Friends){
                switchPageHandler(Page.Friends, R.string.lower_tab_friends);
            }
        }else if(id == profileLL.getId()){
            if (page != Page.Profile){
                switchPageHandler(Page.Profile, R.string.lower_tab_profile);
            }
        }
    }

    private void checkLowertabItem(Page page){
        switch (page){
            case Daily:
                if (dailyDopeIcon == null || dailyDopeText == null){
                    dailyDopeIcon = (ImageView) findViewById(R.id.daily_dope_icon);
                    dailyDopeText = (TextView) findViewById(R.id.daily_dope_text);
                }
                dailyDopeIcon.setImageResource(R.drawable.lower_tab_active_daily);
                dailyDopeText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.lower_bar_active_color));
                break;
            case Tranding:
                if (trandingIcon == null || trandingText == null){
                    trandingIcon = (ImageView) findViewById(R.id.tranding_icon);
                    trandingText = (TextView) findViewById(R.id.tranding_text);
                }
                trandingIcon.setImageResource(R.drawable.lower_tab_active_tranding);
                trandingText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.lower_bar_active_color));
                break;
            case Friends:
                if (friendsIcon == null || friendsText == null){
                    friendsIcon = (ImageView) findViewById(R.id.friends_icon);
                    friendsText = (TextView) findViewById(R.id.friends_text);
                }
                friendsIcon.setImageResource(R.drawable.lower_tab_active_friends);
                friendsText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.lower_bar_active_color));
                break;
            case Profile:
                if (profileIcon == null || profileText == null){
                    profileIcon = (ImageView) findViewById(R.id.profile_icon);
                    profileText = (TextView) findViewById(R.id.profile_text);
                }
                profileIcon.setImageResource(R.drawable.lower_tab_active_profile);
                profileText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.lower_bar_active_color));
                break;
        }
    }

    private void uncheckLowertabItem(Page page){
        switch (page){
            case Daily:
                if (dailyDopeIcon == null || dailyDopeText == null){
                    dailyDopeIcon = (ImageView) findViewById(R.id.daily_dope_icon);
                    dailyDopeText = (TextView) findViewById(R.id.daily_dope_text);
                }
                dailyDopeIcon.setImageResource(R.drawable.lower_tab_daily);
                dailyDopeText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.lower_tab_text_color));
                break;
            case Tranding:
                trandingIcon.setImageResource(R.drawable.lower_tab_tranding);
                trandingText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.lower_tab_text_color));
                break;
            case Friends:
                friendsIcon.setImageResource(R.drawable.lower_tab_friends);
                friendsText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.lower_tab_text_color));
                break;
            case Profile:
                profileIcon.setImageResource(R.drawable.lower_tab_profile);
                profileText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.lower_tab_text_color));
        }
    }

    public enum Page{
        Daily, Tranding, Friends, Profile
    }
}
