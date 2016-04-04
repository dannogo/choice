package com.elantix.dopeapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by oleh on 3/14/16.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private ImageView mLeftToolbarButton;
    private ImageView mRightToolbarButton;
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

    private WindowManager mWindowManager;
    private View mStatusBarOverlay;
    private View mOverlay;
    private LinearLayout mContextOptionsPanel;
    private RelativeLayout mContextOptionsSharePost;
    private RelativeLayout mContextOptionsViewProfile;
    private RelativeLayout mContextOptionsReportPost;
    private RelativeLayout mContextOptionsCancel;

    final private static int REPORT_POST_REQUEST_CODE = 2777;

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
        toolbarTitleAndButtonChangesHandler(page);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REPORT_POST_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            int result = data.getIntExtra("result", 0);
            Toast.makeText(this, getResources().getString(result) , Toast.LENGTH_SHORT).show();
        }else if (requestCode == REPORT_POST_REQUEST_CODE && resultCode == Activity.RESULT_CANCELED){
            Toast.makeText(this, "Report Canceled" , Toast.LENGTH_SHORT).show();
        }

    }

    // TODO:
    // Handle toolbar buttons and title for Friends Dope Fragment

    private void findViews(){
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mLeftToolbarButton = (ImageView) toolbar.findViewById(R.id.left_button);
        mRightToolbarButton = (ImageView) toolbar.findViewById(R.id.right_button);
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

        prepareContextOptionsViews();

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
            case FriendsSearch:
                frag = new FragmentSearchFriends();
                fragmentTAG = "FragmentSearchFriends";
                break;
            case FriendsDope:
                frag = new FragmentDailyDope();
                bundle = new Bundle();
                bundle.putInt("num", 3);
                frag.setArguments(bundle);
                fragmentTAG = "FragmentFriendsDope";
                break;
            case Profile:
                frag = new FragmentFriendsNotRegistered();
                fragmentTAG = "FragmentFriendsNotRegistered";
                break;
            case ProfileOverview:
                frag = new FragmentProfileOverview();
                fragmentTAG = "FragmentProfileOverview";
                break;
            case ProfileFollowers:
                frag = new FragmentProfileFollowers();
                bundle = new Bundle();
                bundle.putBoolean("followers", true);
                frag.setArguments(bundle);
                fragmentTAG = "FragmentProfileFollowers";
                break;
            case ProfileFollowings:
                frag = new FragmentProfileFollowers();
                bundle = new Bundle();
                bundle.putBoolean("followers", false);
                frag.setArguments(bundle);
                fragmentTAG = "FragmentProfileFollowings";
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
            case FriendsSearch:
                fragmentTAG = "FragmentSearchFriends";
                break;
            case FriendsDope:
                fragmentTAG = "FragmentFriendsDope";
                break;
            case Profile:
                fragmentTAG = "FragmentFriendsNotRegistered";
                break;
            case ProfileOverview:
                fragmentTAG = "FragmentProfileOverview";
                break;
            case ProfileFollowers:
                fragmentTAG = "FragmentProfileFollowers";
                break;
            case ProfileFollowings:
                fragmentTAG = "FragmentProfileFollowings";
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

    /**
     * Removes previous fragment, adds new fragment to activity;
     * changes title in toolbar;
     * changes appearance of toolbar buttons and their behaviour
     * @param newPage
     */
    protected void switchPageHandler(Page newPage){
        uncheckLowertabItem(page);
        removeFragment(page);
        page = newPage;
        checkLowertabItem(page);
        toolbarTitleAndButtonChangesHandler(page);
        launchFragment(page);
    }

    protected void toolbarTitleAndButtonChangesHandler(Page page){
        switch (page){
            case Daily:
                toolbarTitle.setText(R.string.lower_tab_daily_dope);
                mLeftToolbarButton.setVisibility(View.GONE);
                mRightToolbarButton.setVisibility(View.VISIBLE);
                mRightToolbarButton.setImageResource(R.drawable.dir_message);
                mRightToolbarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case Tranding:
                toolbarTitle.setText(R.string.lower_tab_tranding);
                mLeftToolbarButton.setVisibility(View.GONE);
                mRightToolbarButton.setVisibility(View.VISIBLE);
                mRightToolbarButton.setImageResource(R.drawable.dir_message);
                mRightToolbarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case Friends:
                toolbarTitle.setText(R.string.lower_tab_friends);
                mLeftToolbarButton.setVisibility(View.GONE);
                mRightToolbarButton.setVisibility(View.VISIBLE);
                mRightToolbarButton.setImageResource(R.drawable.dir_message);
                mRightToolbarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case FriendsSearch:
                toolbarTitle.setText(R.string.search_friends_toolbar_title);
                mLeftToolbarButton.setVisibility(View.VISIBLE);
                mRightToolbarButton.setVisibility(View.GONE);
                mLeftToolbarButton.setImageResource(R.drawable.toolbar_left_arrow);
                mLeftToolbarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchPageHandler(Page.Friends);
                    }
                });
                break;
            case FriendsDope:
                toolbarTitle.setText(R.string.lower_tab_friends);
                mLeftToolbarButton.setVisibility(View.VISIBLE);
                mRightToolbarButton.setVisibility(View.VISIBLE);
                mRightToolbarButton.setImageResource(R.drawable.dir_message);
                mLeftToolbarButton.setImageResource(R.drawable.toolbar_magnifier_icon);
                break;
            case Profile:
                toolbarTitle.setText(R.string.lower_tab_profile);
                mLeftToolbarButton.setVisibility(View.VISIBLE);
                mRightToolbarButton.setVisibility(View.GONE);
                mLeftToolbarButton.setImageResource(R.drawable.toolbar_settings_icon);
                mLeftToolbarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case ProfileOverview:
                toolbarTitle.setText(R.string.profile_overview_dummy_user_name);
                mLeftToolbarButton.setVisibility(View.VISIBLE);
                mRightToolbarButton.setVisibility(View.GONE);
                mLeftToolbarButton.setImageResource(R.drawable.toolbar_left_arrow);
                mLeftToolbarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case ProfileFollowers:
                toolbarTitle.setText(R.string.profile_followers_toolbar_title);
                mLeftToolbarButton.setVisibility(View.VISIBLE);
                mRightToolbarButton.setVisibility(View.GONE);
                mLeftToolbarButton.setImageResource(R.drawable.toolbar_left_arrow);
                mLeftToolbarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchPageHandler(Page.ProfileOverview);
                    }
                });
                break;

            case ProfileFollowings:
                toolbarTitle.setText(R.string.profile_following_toolbar_title);
                mLeftToolbarButton.setVisibility(View.VISIBLE);
                mRightToolbarButton.setVisibility(View.VISIBLE);
                mLeftToolbarButton.setImageResource(R.drawable.toolbar_left_arrow);
                mRightToolbarButton.setImageResource(R.drawable.toolbar_magnifier_icon);
                mLeftToolbarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchPageHandler(Page.ProfileOverview);
                    }
                });
                break;

            default:

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == dailyDopeLL.getId()){
            if (page != Page.Daily){
                switchPageHandler(Page.Daily);
            }
        }else if(id == trandingLL.getId()){
            if (page != Page.Tranding){
                switchPageHandler(Page.Tranding);
            }
        }else if (id == plusLL.getId()){
            Intent intent = new Intent(MainActivity.this, TabPlusActivity.class);
            startActivity(intent);
        }else if (id == friendsLL.getId()){
            if (page != Page.Friends){
                switchPageHandler(Page.Friends);
            }
        }else if(id == profileLL.getId()){
            if (page != Page.Profile){
                switchPageHandler(Page.Profile);
            }
        }else if (id == mContextOptionsSharePost.getId()){
            showContextOptions(false, null);
            Intent intent = new Intent(MainActivity.this, ShareDopeActivity.class);
            int num = 1;
            switch (page){
                case Daily:
                    num = 1;
                    break;
                case Tranding:
                    num = 2;
                    break;
                case FriendsDope:
                case Friends:
                    num = 3;
                    break;
            }
            intent.putExtra("num", num);
            startActivity(intent);
        }else if (id == mContextOptionsCancel.getId()){
            showContextOptions(false, null);
        }else if (id == mContextOptionsViewProfile.getId()){
            showContextOptions(false, null);
            switchPageHandler(Page.ProfileOverview);
        }else if (id == mContextOptionsReportPost.getId()){
            showContextOptions(false, null);
            Intent intent = new Intent(MainActivity.this, ReportPostActivity.class);
            intent.putExtra("post_id", 10);
            startActivityForResult(intent, REPORT_POST_REQUEST_CODE);
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
            case FriendsDope:
            case FriendsSearch:
            case Friends:
                if (friendsIcon == null || friendsText == null){
                    friendsIcon = (ImageView) findViewById(R.id.friends_icon);
                    friendsText = (TextView) findViewById(R.id.friends_text);
                }
                friendsIcon.setImageResource(R.drawable.lower_tab_active_friends);
                friendsText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.lower_bar_active_color));
                break;
            case ProfileFollowings:
            case ProfileFollowers:
            case ProfileOverview:
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
            case FriendsDope:
            case FriendsSearch:
            case Friends:
                friendsIcon.setImageResource(R.drawable.lower_tab_friends);
                friendsText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.lower_tab_text_color));
                break;
            case ProfileFollowings:
            case ProfileFollowers:
            case ProfileOverview:
            case Profile:
                profileIcon.setImageResource(R.drawable.lower_tab_profile);
                profileText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.lower_tab_text_color));
        }
    }

    public enum Page{
        Daily, Tranding, Friends, FriendsSearch, FriendsDope, Profile, ProfileOverview, ProfileFollowers, ProfileFollowings
    }


    /**
     * takes context panel views from xml, sets proper text, sets onClickListener
     */
    private void prepareContextOptionsViews(){
        mContextOptionsPanel = (LinearLayout) findViewById(R.id.main_activity_context_options_panel);
        mOverlay = findViewById(R.id.main_activity_overlay);

        TextView optionOneTextView = (TextView) mContextOptionsPanel.findViewById(R.id.context_options_1_textview);
        TextView optionTwoTextView = (TextView) mContextOptionsPanel.findViewById(R.id.context_options_2_textview);
        TextView optionThreeTextView = (TextView) mContextOptionsPanel.findViewById(R.id.context_options_3_textview);
        TextView optionCancelTextView = (TextView) mContextOptionsPanel.findViewById(R.id.context_options_cancel_textview);

        optionOneTextView.setText(R.string.main_activity_context_option_1_text);
        optionTwoTextView.setText(R.string.main_activity_context_option_2_text);
        optionThreeTextView.setText(R.string.main_activity_context_option_3_text);
        optionCancelTextView.setText(R.string.main_activity_context_option_cancel_text);

        mContextOptionsSharePost = (RelativeLayout) mContextOptionsPanel.findViewById(R.id.context_options_1);
        mContextOptionsViewProfile = (RelativeLayout) mContextOptionsPanel.findViewById(R.id.context_options_2);
        mContextOptionsReportPost = (RelativeLayout) mContextOptionsPanel.findViewById(R.id.context_options_3);
        mContextOptionsCancel = (RelativeLayout) mContextOptionsPanel.findViewById(R.id.context_options_cancel);

        mContextOptionsSharePost.setOnClickListener(this);
        mContextOptionsViewProfile.setOnClickListener(this);
        mContextOptionsReportPost.setOnClickListener(this);
        mContextOptionsCancel.setOnClickListener(this);

    }

    /**
     * Shows and Hides context options panel
     * @param show
     */
    protected void showContextOptions(Boolean show, Integer num){

        if (show){
            WindowManager.LayoutParams mLP = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    Utilites.getStatusBarHeight(MainActivity.this),
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
            mStatusBarOverlay = new View(MainActivity.this);
            mStatusBarOverlay.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            mStatusBarOverlay.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.context_options_overlay_color));

            mWindowManager.addView(mStatusBarOverlay, mLP);

            mOverlay.setVisibility(View.VISIBLE);

            Animation bottomUp = AnimationUtils.loadAnimation(MainActivity.this,
                    R.anim.bottom_up);
            mContextOptionsPanel.startAnimation(bottomUp);
            mContextOptionsPanel.setVisibility(View.VISIBLE);
        }else{
            mWindowManager.removeView(mStatusBarOverlay);

            mOverlay.setVisibility(View.GONE);

            Animation bottomDown = AnimationUtils.loadAnimation(MainActivity.this,
                    R.anim.bottom_down);
            mContextOptionsPanel.startAnimation(bottomDown);
            mContextOptionsPanel.setVisibility(View.GONE);
        }
    }
}
