package com.elantix.dopeapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import com.desarrollodroide.libraryfragmenttransactionextended.FragmentTransactionExtended;
import com.jirbo.adcolony.AdColony;
import com.jirbo.adcolony.AdColonyAd;
import com.jirbo.adcolony.AdColonyAdAvailabilityListener;
import com.jirbo.adcolony.AdColonyAdListener;

import java.util.HashMap;

/**
 * Created by oleh on 3/14/16.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        FragmentViewPager.CommunicatorOne, AdColonyAdAvailabilityListener,
        AdColonyAdListener{

    private Toolbar toolbar;
    private ImageView mLeftToolbarButton;
    public ImageView mRightToolbarButton;
    public ImageView mRightToolbarButton2;
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

    public TextView toolbarTitle;
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

    private LinearLayout mProfileOptionsPanel;
    private RelativeLayout mProfileOptionsShareProfile;
    private RelativeLayout mProfileOptionsBlockUser;
    private RelativeLayout mProfileOptionsReportUser;
    private RelativeLayout mProfileOptionsCancel;

    public Fragment mCurrentFragment;
    public Fragment mPreviousFragment;

    final private static int REPORT_POST_REQUEST_CODE = 2777;
    public ProgressDialog mProgressDialog;
    public ProgressDialog mAnotherProgressDialog;
    private int mContextOptionsDopeNum;

    Page page = Page.Daily;
    private boolean isContextOptionsPanelShown = false;
    public boolean isProfileOptionsPanelShown = false;
    public String mListUserId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.main_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Utilities.sDopeListType = null;
        findViews();
        switchPageHandler(page);
        AdColony.configure( this, "version:1.0,store:google", Utilities.ADCOLONY_APP_ID, Utilities.ADCOLONY_ZONE_ID);
        AdColony.addAdAvailabilityListener(this);

    }

    public void saveDopes(DopeInfo[] dopes){
        if (page == Page.Daily) {
//            mDopes10 = dopes;
            Utilities.sDopes10 = dopes;
        }else if (page == Page.Tranding){
//            mDopes100 = dopes;
            Utilities.sDopes100 = dopes;
        }else if (page == Page.FriendsDope){
            Utilities.sDopesFriendsFeed = dopes;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AdColony.resume(this);
//        MyFirebaseInstanceIDService service = new MyFirebaseInstanceIDService();
//        service.onTokenRefresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AdColony.pause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REPORT_POST_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            int result = data.getIntExtra("result", 0);

        }else if (requestCode == REPORT_POST_REQUEST_CODE && resultCode == Activity.RESULT_CANCELED){
            Toast.makeText(this, "Report Canceled" , Toast.LENGTH_SHORT).show();
        }
        if (requestCode == Utilities.SIGN_IN_UP){
            if (resultCode == Activity.RESULT_OK) {
                Log.w("MainActivity", "onActivityResult OK");
                switchPageHandler(Page.Profile);
            }else if (resultCode == Activity.RESULT_CANCELED){
                Log.w("MainActivity", "onActivityResult CANCEL");
            }
        }

    }

    // TODO:
    // Handle toolbar buttons and title for Friends Dope Fragment

    private void findViews(){
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mLeftToolbarButton = (ImageView) toolbar.findViewById(R.id.left_button);
        mRightToolbarButton = (ImageView) toolbar.findViewById(R.id.right_button);
        mRightToolbarButton2 = (ImageView) toolbar.findViewById(R.id.right_button_2);
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
        prepareProfileOptionsViews();

    }

    public void launchFragment(Page page){
        launchFragment(page, false);
    }

    public void launchFragment(Page page, boolean isRecovery){
        String fragmentTAG;
        Bundle bundle;
        switch (page){
            case Daily:
                mCurrentFragment = new FragmentViewPager();
                bundle = new Bundle();

                if (!isRecovery) {
                    ChainLink chainLinkDaily = new ChainLink(Page.Daily);
                    chainLinkDaily.bundleData.put("num", Utilities.sDopes10.length);
                    Utilities.sFragmentHistory.add(chainLinkDaily);
                    Log.e("MainActivity daily", "Utilities.sFragmentHistory.size(): " + Utilities.sFragmentHistory.size());
                }else{
                    if (Utilities.sFragmentHistory.get(Utilities.sFragmentHistory.size()-1).bundleData.get("position") != null) {
                        int position = (int) Utilities.sFragmentHistory.get(Utilities.sFragmentHistory.size() - 1).bundleData.get("position");
                        bundle.putInt("position", position);
                        Log.w("MainActivity daily", "bundle position: " + position);
                    }
                }

                bundle.putInt("num", Utilities.sDopes10.length);
                mCurrentFragment.setArguments(bundle);
                fragmentTAG = "FragmentDailyDope";
                break;
            case Tranding:
                mCurrentFragment = new FragmentViewPager();
                bundle = new Bundle();

                if (!isRecovery) {
                    ChainLink chainLinkTranding = new ChainLink(Page.Tranding);
                    chainLinkTranding.bundleData.put("num", Utilities.sDopes100.length);
                    Utilities.sFragmentHistory.add(chainLinkTranding);
                    Log.e("MainActivity tranding", "Utilities.sFragmentHistory.size(): " + Utilities.sFragmentHistory.size());
                }else {
                    int position = (int) Utilities.sFragmentHistory.get(Utilities.sFragmentHistory.size()-1).bundleData.get("position");
                    bundle.putInt("position", position);
                    Log.w("MainActivity daily", "bundle position: " + position);
                }

                bundle.putInt("num", Utilities.sDopes100.length);
                mCurrentFragment.setArguments(bundle);
                fragmentTAG = "FragmentTranding";
                break;
            case Friends:

                mCurrentFragment = new FragmentFriendsFirstScreen();
                fragmentTAG = "FragmentFriendsFirstScreen";
                break;
            case FriendsSearch:
                if (isRecovery){

                }else {
                    ChainLink chainLinkFriendsSearch = new ChainLink(Page.FriendsSearch);
//                  chainLinkFriendsSearch.bundleData.put("own", isOwn);
//                  chainLinkFriendsSearch.bundleData.put("uid", userId);
                    Utilities.sFragmentHistory.add(chainLinkFriendsSearch);
                }

                mCurrentFragment = new FragmentSearchFriends();
                fragmentTAG = "FragmentSearchFriends";
                break;
            case FriendsDope:

                mCurrentFragment = new FragmentViewPager();
                bundle = new Bundle();

                if (!isRecovery){
                    ChainLink chainLinkFriendsFeed = new ChainLink(Page.FriendsDope);
                    chainLinkFriendsFeed.bundleData.put("num", Utilities.sDopesFriendsFeed.length);
                    Utilities.sFragmentHistory.add(chainLinkFriendsFeed);
                }else{
                    if (Utilities.sFragmentHistory.get(Utilities.sFragmentHistory.size()-1).bundleData.get("position") != null) {
                        int position = (int) Utilities.sFragmentHistory.get(Utilities.sFragmentHistory.size() - 1).bundleData.get("position");
                        bundle.putInt("position", position);
                        Log.w("MainActivity daily", "bundle position: " + position);
                    }
                }

                bundle.putInt("num", Utilities.sDopesFriendsFeed.length);
                mCurrentFragment.setArguments(bundle);
                fragmentTAG = "FragmentFriendsDope";
                break;
            case Profile:

                ChainLink chainLinkProfile = new ChainLink(Page.Profile);
                if (Utilities.sToken != null){
                    if (Utilities.decorStyle == Utilities.DecorStyle.First) {
                        mCurrentFragment = new FragmentProfileOverview();
                    }else {
                        mCurrentFragment = new FragmentProfileOverview2();
                    }
                    bundle = new Bundle();
                    bundle.putBoolean("own", true);
                    chainLinkProfile.bundleData.put("own", true);
                    mCurrentFragment.setArguments(bundle);
                }else {
                    mCurrentFragment = new FragmentFriendsNotRegistered();
                }
                if (!isRecovery) {
                    Utilities.sFragmentHistory.add(chainLinkProfile);
                    Log.e("MainActivity", "history size: " + Utilities.sFragmentHistory.size());
                    Log.e("MinActivity", "isRecovery: "+isRecovery);
                }

                fragmentTAG = "FragmentFriendsNotRegistered";
                break;
            case ProfileOverview:
                ChainLink chainLinkProfileOverview = new ChainLink(Page.ProfileOverview);
                if (Utilities.decorStyle == Utilities.DecorStyle.First) {
                    mCurrentFragment = new FragmentProfileOverview();
                }else {
                    mCurrentFragment = new FragmentProfileOverview2();
                }
                bundle = new Bundle();
                if (isRecovery){
                    HashMap<String, Object> bundleData = Utilities.sFragmentHistory.get(Utilities.sFragmentHistory.size()-1).bundleData;
                    bundle.putString("uid", (String) bundleData.get("uid"));
                    bundle.putBoolean("own", (Boolean) bundleData.get("own"));
                    bundle.putBoolean("isRecovery", true);

                }else {

                    // NOT TESTED UNDER
                    DopeInfo curItem;
                    if (Utilities.sDopeListType == Utilities.DopeListType.Ten) {
//                        Log.e("MainActivity", "Utilities.sDopes10: "+Utilities.sDopes10);
//                        if (Utilities.sDopes10.length > mContextOptionsDopeNum) {
                            curItem = Utilities.sDopes10[mContextOptionsDopeNum];
//                        }
                    } else if (Utilities.sDopeListType == Utilities.DopeListType.Hundred){
//                        if (Utilities.sDopes100.length > mContextOptionsDopeNum) {
                            curItem = Utilities.sDopes100[mContextOptionsDopeNum];
//                        }
                    }else {
//                        if(Utilities.sDopesFriendsFeed.length > mContextOptionsDopeNum) {
                            curItem = Utilities.sDopesFriendsFeed[mContextOptionsDopeNum];
//                        }
                    }

//                    Log.e("MainActivity", "mListUserId: "+mListUserId);

                    String userId = (mListUserId == null) ? curItem.userId : mListUserId;

                    boolean isOwn = (userId.equals(Utilities.sUid)) ? true : false;
                    bundle.putBoolean("own", isOwn);
                    bundle.putString("uid", userId);
                    chainLinkProfileOverview.bundleData.put("own", isOwn);
                    chainLinkProfileOverview.bundleData.put("uid", userId);
                    Utilities.sFragmentHistory.add(chainLinkProfileOverview);
                }

                mCurrentFragment.setArguments(bundle);
                fragmentTAG = "FragmentProfileOverview";
                break;
            case ProfileFollowers:
                mCurrentFragment = new FragmentProfileFollowers();
                bundle = new Bundle();

                if (isRecovery){
                    Utilities.sCurProfile = (ProfileInfo) Utilities.sFragmentHistory.get(Utilities.sFragmentHistory.size()-1).data.get(0);
                }else {
                    ChainLink chainLinkProfileFollowers = new ChainLink(Page.ProfileFollowers);
                    ProfileInfo profileInfo = new ProfileInfo(Utilities.sCurProfile);
                    chainLinkProfileFollowers.data.add(profileInfo);
                    Utilities.sFragmentHistory.add(chainLinkProfileFollowers);
                }

                bundle.putBoolean("followers", true);
                mCurrentFragment.setArguments(bundle);
                fragmentTAG = "FragmentProfileFollowers";
                break;
            case ProfileFollowings:
                mCurrentFragment = new FragmentProfileFollowers();
                bundle = new Bundle();

                if (isRecovery){
                    Utilities.sCurProfile = (ProfileInfo) Utilities.sFragmentHistory.get(Utilities.sFragmentHistory.size()-1).data.get(0);
                }else {
                    ChainLink chainLinkProfileFollowings = new ChainLink(Page.ProfileFollowings);
                    ProfileInfo profileInfo = new ProfileInfo(Utilities.sCurProfile);
                    chainLinkProfileFollowings.data.add(profileInfo);
                    Utilities.sFragmentHistory.add(chainLinkProfileFollowings);
                }

                bundle.putBoolean("followers", false);

                mCurrentFragment.setArguments(bundle);
                fragmentTAG = "FragmentProfileFollowings";
                break;
            case ProfileSettings:
                mCurrentFragment = new FragmentProfileSettings();
                fragmentTAG = "FragmentProfileSettings";


                break;
            default:
                mCurrentFragment = new FragmentDailyDope();
                fragmentTAG = "FragmentDailyDope";
        }
        transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, mCurrentFragment, fragmentTAG);
        transaction.commit();
    }

    private void removeFragment(Page page){

        if(mCurrentFragment != null) {
            transaction = manager.beginTransaction();
            transaction.remove(mCurrentFragment);
            transaction.commit();

        }
    }

    /**
     * Removes previous fragment, adds new fragment to activity;
     * changes title in toolbar;
     * changes appearance of toolbar buttons and their behaviour
     * @param newPage
     */
    protected void switchPageHandler(Page newPage, boolean isRecovery){
        uncheckLowertabItem(page);
        removeFragment(page);
        page = newPage;
        checkLowertabItem(page);
        toolbarTitleAndButtonChangesHandler(page);
        HttpKit http = new HttpKit(this);

        Log.e("MainActivity launch", "isRecovery: "+isRecovery);
        if (!isRecovery) {
            if (page == Page.Daily) {
                http.get10Dopes(Utilities.sToken, null);
            } else if (page == Page.Tranding) {
//                HttpKit http = new HttpKit(this);
                http.get100Dopes(Utilities.sToken, null, null);
            }else if (page == Page.FriendsDope){
                http.getFriendsFeed(Utilities.sToken, null, null);
            }else{
                launchFragment(page, isRecovery);
            }
        }
        else {
            launchFragment(page, isRecovery);
        }
    }

    protected  void switchPageHandler(Page newPage){
        switchPageHandler(newPage, false);
    }

    // currently not in use
    protected void switchPageAnimatedHandler(Page newPage){
        uncheckLowertabItem(page);
        animatedLaunchFragment(newPage);
        page = newPage;
        checkLowertabItem(page);
        toolbarTitleAndButtonChangesHandler(page);
    }

    // currently not in use
    private void animatedLaunchFragment(Page newPage){

        Fragment fragmentTo;
        Bundle bundle;

        switch (newPage){
            case Daily:
                fragmentTo = new FragmentDailyDope();
                bundle = new Bundle();
                bundle.putInt("num", 1);
                fragmentTo.setArguments(bundle);
                break;
            case Tranding:
                fragmentTo = new FragmentDailyDope();
//                frag = new FragmentNoMessage();
                bundle = new Bundle();
                bundle.putInt("num", 2);
                fragmentTo.setArguments(bundle);
                break;
            case Friends:
                fragmentTo = new FragmentFriendsFirstScreen();
                break;
            case FriendsSearch:
                fragmentTo = new FragmentSearchFriends();
                break;
            case FriendsDope:
                fragmentTo = new FragmentDailyDope();
                bundle = new Bundle();
                bundle.putInt("num", 3);
                fragmentTo.setArguments(bundle);
                break;
            case Profile:
                if (Utilities.sToken != null){
                    if (Utilities.decorStyle == Utilities.DecorStyle.First) {
                        fragmentTo = new FragmentProfileOverview();
                    }else {
                        fragmentTo = new FragmentProfileOverview2();
                    }
                    bundle = new Bundle();
                    bundle.putBoolean("own", true);
                    fragmentTo.setArguments(bundle);
                }else {
                    fragmentTo = new FragmentFriendsNotRegistered();
                }
                break;
            case ProfileOverview:
                if (Utilities.decorStyle == Utilities.DecorStyle.First) {
                    fragmentTo = new FragmentProfileOverview();
                }else {
                    fragmentTo = new FragmentProfileOverview2();
                }
                bundle = new Bundle();
                bundle.putBoolean("own", false);
                fragmentTo.setArguments(bundle);
                break;
            case ProfileFollowers:
                fragmentTo = new FragmentProfileFollowers();
                bundle = new Bundle();
                bundle.putBoolean("followers", true);
                fragmentTo.setArguments(bundle);
                break;
            case ProfileFollowings:
                fragmentTo = new FragmentProfileFollowers();
                bundle = new Bundle();
                bundle.putBoolean("followers", false);
                fragmentTo.setArguments(bundle);
                break;
            default:
                fragmentTo = new FragmentDailyDope();
        }

        //        FragmentManager fm = getFragmentManager();
//        FragmentTransaction fragmentTransaction = manager.beginTransaction();

        // Just for demonstration
//        int animationType = Utilites.ANIMATION_TYPES[Utilites.sAnimationNumber];
//        int animationType = FragmentTransactionExtended.ZOOM_SLIDE_VERTICAL;
        int animationType = FragmentTransactionExtended.SLIDE_VERTICAL;

        transaction = manager.beginTransaction();
        FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(this,
                transaction, mCurrentFragment, fragmentTo, R.id.fragment_container);
        fragmentTransactionExtended.addTransition(animationType);
        fragmentTransactionExtended.commit();
        mCurrentFragment = fragmentTo;
    }

    public void launchCommentsActivity(){
        Intent intent = new Intent(MainActivity.this, MessageActivity.class);
        startActivity(intent);
    }

    protected void toolbarTitleAndButtonChangesHandler(Page page){
        switch (page){
            case Daily:
                toolbarTitle.setText(R.string.lower_tab_daily_dope);
                mLeftToolbarButton.setVisibility(View.GONE);
                mRightToolbarButton2.setVisibility(View.GONE);
                mRightToolbarButton.setVisibility(View.VISIBLE);
                mRightToolbarButton.setImageResource(R.drawable.dir_message);
                mRightToolbarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        launchCommentsActivity();
                    }
                });
                break;
            case Tranding:
                toolbarTitle.setText(R.string.lower_tab_tranding);
                mLeftToolbarButton.setVisibility(View.GONE);
                mRightToolbarButton2.setVisibility(View.GONE);
                mRightToolbarButton.setVisibility(View.VISIBLE);
                mRightToolbarButton.setImageResource(R.drawable.dir_message);
                mRightToolbarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        launchCommentsActivity();
                    }
                });
                break;
            case Friends:
                toolbarTitle.setText(R.string.lower_tab_friends);
                mLeftToolbarButton.setVisibility(View.GONE);
                mRightToolbarButton2.setVisibility(View.GONE);
                mRightToolbarButton.setVisibility(View.VISIBLE);
                mRightToolbarButton.setImageResource(R.drawable.dir_message);
                mRightToolbarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        launchCommentsActivity();
                    }
                });
                break;
            case FriendsSearch:
                toolbarTitle.setText(R.string.search_friends_toolbar_title);
                mLeftToolbarButton.setVisibility(View.VISIBLE);
                mRightToolbarButton2.setVisibility(View.GONE);
                mRightToolbarButton.setVisibility(View.GONE);
                mLeftToolbarButton.setImageResource(R.drawable.toolbar_left_arrow);
                mLeftToolbarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        switchPageHandler(Page.Friends);
                        Log.e("MainActivity", "FriendsSearch back arrow");
                        for(int i=0; i<Utilities.sFragmentHistory.size(); i++){
                            Log.w("MainActivity", "record "+i+": "+Utilities.sFragmentHistory.get(i).fragment);
                        }
                        Utilities.sFragmentHistory.remove(Utilities.sFragmentHistory.size() - 1);
                        if(Utilities.sFragmentHistory.size() > 0) {
                            switchPageHandler(Utilities.sFragmentHistory.get(Utilities.sFragmentHistory.size() - 1).fragment, true);
                        }else{

                        }
                    }
                });
                break;
            case FriendsDope:
                toolbarTitle.setText(R.string.lower_tab_friends);
                mLeftToolbarButton.setVisibility(View.VISIBLE);
                mRightToolbarButton2.setVisibility(View.GONE);
                mRightToolbarButton.setVisibility(View.VISIBLE);
                mRightToolbarButton.setImageResource(R.drawable.dir_message);
                mRightToolbarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        launchCommentsActivity();
                    }
                });
                mLeftToolbarButton.setImageResource(R.drawable.toolbar_magnifier_icon);
                mLeftToolbarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchPageHandler(Page.FriendsSearch);
                    }
                });
                break;
            case Profile:
                toolbarTitle.setText(R.string.lower_tab_profile);
                mLeftToolbarButton.setVisibility(View.VISIBLE);
                mRightToolbarButton2.setVisibility(View.GONE);
                if(Utilities.sToken != null) {
                    mRightToolbarButton.setVisibility(View.VISIBLE);
                }else {
                    mRightToolbarButton.setVisibility(View.GONE);
                }
                mLeftToolbarButton.setImageResource(R.drawable.toolbar_settings_icon);
                mLeftToolbarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("MainActivity", "Profile back arrow");
                        if (Utilities.sToken != null){
                            switchPageHandler(Page.ProfileSettings);
                        }
                    }
                });
                break;
            case ProfileOverview:
                mLeftToolbarButton.setVisibility(View.VISIBLE);
                mRightToolbarButton2.setVisibility(View.GONE);
                mRightToolbarButton.setVisibility(View.GONE);
                mLeftToolbarButton.setImageResource(R.drawable.toolbar_left_arrow);
                mLeftToolbarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("MainActivity", "ProfileOverview back arrow");
                        for(int i=0; i<Utilities.sFragmentHistory.size(); i++){
                            Log.w("MainActivity", "record "+i+": "+Utilities.sFragmentHistory.get(i).fragment);
                        }
                        Utilities.sFragmentHistory.remove(Utilities.sFragmentHistory.size() - 1);
                        switchPageHandler(Utilities.sFragmentHistory.get(Utilities.sFragmentHistory.size()-1).fragment, true);
                    }
                });
                break;
            case ProfileFollowers:
                toolbarTitle.setText(R.string.profile_followers_toolbar_title);
                mLeftToolbarButton.setVisibility(View.VISIBLE);
                mRightToolbarButton2.setVisibility(View.GONE);
                mRightToolbarButton.setVisibility(View.GONE);
                mLeftToolbarButton.setImageResource(R.drawable.toolbar_left_arrow);
                mLeftToolbarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Log.w("MainActivity", "ProfileFollowers back arrow");
//                        for(int i=0; i<Utilities.sFragmentHistory.size(); i++){
//                            Log.w("MainActivity", "Before record "+i+": "+Utilities.sFragmentHistory.get(i).fragment);
//                        }
                        Utilities.sFragmentHistory.remove(Utilities.sFragmentHistory.size() - 1);
                        switchPageHandler(Utilities.sFragmentHistory.get(Utilities.sFragmentHistory.size()-1).fragment, true);
                        for(int i=0; i<Utilities.sFragmentHistory.size(); i++){
                            Log.w("MainActivity", "After record "+i+": "+Utilities.sFragmentHistory.get(i).fragment);
                        }
                    }
                });
                break;

            case ProfileFollowings:
                toolbarTitle.setText(R.string.profile_following_toolbar_title);
                mLeftToolbarButton.setVisibility(View.VISIBLE);
                mRightToolbarButton2.setVisibility(View.GONE);
                mRightToolbarButton.setVisibility(View.VISIBLE);
                mLeftToolbarButton.setImageResource(R.drawable.toolbar_left_arrow);
                mRightToolbarButton.setImageResource(R.drawable.toolbar_magnifier_icon);
                mLeftToolbarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Log.e("MainActivity", "FriendsSearch back arrow");
//                        for(int i=0; i<Utilities.sFragmentHistory.size(); i++){
//                            Log.w("MainActivity", "record "+i+": "+Utilities.sFragmentHistory.get(i).fragment);
//                        }
                        Utilities.sFragmentHistory.remove(Utilities.sFragmentHistory.size()-1);
                        switchPageHandler(Utilities.sFragmentHistory.get(Utilities.sFragmentHistory.size()-1).fragment, true);
                    }
                });
                break;

            case ProfileSettings:
                toolbarTitle.setText(R.string.profile_settings_toolbar_title);
                mLeftToolbarButton.setVisibility(View.VISIBLE);
                mRightToolbarButton2.setVisibility(View.GONE);
                mRightToolbarButton.setVisibility(View.GONE);
                mLeftToolbarButton.setImageResource(R.drawable.toolbar_left_arrow);
                mLeftToolbarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        switchPageHandler(Page.ProfileOverview, true);
                        switchPageHandler(Utilities.sFragmentHistory.get(Utilities.sFragmentHistory.size()-1).fragment, true);
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
                Utilities.sFragmentHistory.clear();
                switchPageHandler(Page.Daily);
            }
        }else if(id == trandingLL.getId()){
            if (page != Page.Tranding){
                Utilities.sFragmentHistory.clear();
                switchPageHandler(Page.Tranding);
            }
        }else if (id == plusLL.getId()){
            if (Utilities.sToken != null) {
                Intent intent = new Intent(MainActivity.this, TabPlusActivity.class);
                startActivity(intent);
            }else{
                if (page != Page.Profile) {
                    switchPageHandler(Page.Profile);
                }
            }
        }else if (id == friendsLL.getId()){
            if (page != Page.Friends){
                Utilities.sFragmentHistory.clear();
                HttpKit http = new HttpKit(this);
                http.checkMyFollowersPresence();

//                switchPageHandler(Page.Friends);
//                switchPageHandler(Page.FriendsDope);
            }
        }else if(id == profileLL.getId()){
            if (page != Page.Profile){
                Utilities.sFragmentHistory.clear();
                mListUserId = null;
                switchPageHandler(Page.Profile);
            }
        }else if (id == mContextOptionsSharePost.getId()){
            Intent intent = new Intent(MainActivity.this, ShareDopeActivity.class);
            intent.putExtra("dopeNum", mContextOptionsDopeNum);
            showContextOptions(false, null);
            startActivity(intent);
        }else if (id == mContextOptionsCancel.getId()){
            showContextOptions(false, null);
        }else if (id == mContextOptionsViewProfile.getId()){
            mListUserId = null;
            showContextOptions(false, null);
            switchPageHandler(Page.ProfileOverview);
        }else if (id == mContextOptionsReportPost.getId()){
            showContextOptions(false, null);
            Intent intent = new Intent(MainActivity.this, ReportPostActivity.class);
//            intent.putExtra("post_id", 10);
            intent.putExtra("dopeNum", mContextOptionsDopeNum);
            startActivityForResult(intent, REPORT_POST_REQUEST_CODE);
        }else if (id == mProfileOptionsShareProfile.getId()){
            Intent intent = new Intent(MainActivity.this, ShareProfileActivity.class);
            startActivity(intent);
            showProfileOptions(false, null);
        }else if (id == mProfileOptionsBlockUser.getId()){
            showProfileOptions(false, null);
        }else if (id == mProfileOptionsReportUser.getId()){
            showProfileOptions(false, null);
        }else if (id == mProfileOptionsCancel.getId()){
            showProfileOptions(false, null);
        }else if (id == mOverlay.getId()){
            showProfileOptions(false, null);
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
            case ProfileSettings:
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
            case ProfileSettings:
            case ProfileFollowings:
            case ProfileFollowers:
            case ProfileOverview:
            case Profile:
                profileIcon.setImageResource(R.drawable.lower_tab_profile);
                profileText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.lower_tab_text_color));
        }
    }

    @Override
    public void respond() {
        if (mCurrentFragment instanceof FragmentViewPager){
            ((FragmentViewPager)mCurrentFragment).showNextPage();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utilities.sFragmentHistory.clear();
    }

    @Override
    public void onBackPressed() {
        if (isContextOptionsPanelShown) {
            showContextOptions(false, null);
        }else if(isProfileOptionsPanelShown){
            showProfileOptions(false, null);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onAdColonyAdAvailabilityChange(boolean b, String s) {

    }

    @Override
    public void onAdColonyAdAttemptFinished(AdColonyAd adColonyAd) {

    }

    @Override
    public void onAdColonyAdStarted(AdColonyAd adColonyAd) {

    }

    public enum Page{
        Daily,
        Tranding,
        Friends,
        FriendsSearch,
        FriendsDope,
        Profile,
        ProfileOverview,
        ProfileFollowers,
        ProfileFollowings,
        ProfileSettings
    }


    /**
     * takes context panel views from xml, sets proper text, sets onClickListener
     */
    private void prepareContextOptionsViews(){
        mContextOptionsPanel = (LinearLayout) findViewById(R.id.main_activity_context_options_panel);
        mOverlay = findViewById(R.id.main_activity_overlay);
        mOverlay.setOnClickListener(this);

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

    private void prepareProfileOptionsViews(){
        mProfileOptionsPanel = (LinearLayout) findViewById(R.id.main_activity_profile_options_panel);

        TextView optionOneTextView = (TextView) mProfileOptionsPanel.findViewById(R.id.profile_options_1_textview);
        TextView optionTwoTextView = (TextView) mProfileOptionsPanel.findViewById(R.id.profile_options_2_textview);
        TextView optionThreeTextView = (TextView) mProfileOptionsPanel.findViewById(R.id.profile_options_3_textview);
        TextView optionCancelTextView = (TextView) mProfileOptionsPanel.findViewById(R.id.profile_options_cancel_textview);

        optionOneTextView.setText(R.string.main_activity_profile_option_1_text);
        optionTwoTextView.setText(R.string.main_activity_profile_option_2_text);
        optionThreeTextView.setText(R.string.main_activity_profile_option_3_text);
        optionCancelTextView.setText(R.string.main_activity_profile_option_cancel_text);

        mProfileOptionsShareProfile = (RelativeLayout) mProfileOptionsPanel.findViewById(R.id.profile_options_1);
        mProfileOptionsBlockUser = (RelativeLayout) mProfileOptionsPanel.findViewById(R.id.profile_options_2);
        mProfileOptionsReportUser = (RelativeLayout) mProfileOptionsPanel.findViewById(R.id.profile_options_3);
        mProfileOptionsCancel = (RelativeLayout) mProfileOptionsPanel.findViewById(R.id.profile_options_cancel);

        mProfileOptionsShareProfile.setOnClickListener(this);
        mProfileOptionsBlockUser.setOnClickListener(this);
        mProfileOptionsReportUser.setOnClickListener(this);
        mProfileOptionsCancel.setOnClickListener(this);

    }

    private void showHideOverlay(boolean show){
        if (show) {
            WindowManager.LayoutParams mLP = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    Utilities.getStatusBarHeight(MainActivity.this),
                    // Allows the view to be on top of the StatusBar
                    WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                    // Keeps the button presses from going to the background window
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            // Enables the notification to recieve touch events
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                            // Draws over status bar
                            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                    PixelFormat.TRANSLUCENT);

            mLP.gravity = Gravity.TOP | Gravity.CENTER;

            mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            mStatusBarOverlay = new View(MainActivity.this);
            mStatusBarOverlay.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            mStatusBarOverlay.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.context_options_overlay_color));

            mWindowManager.addView(mStatusBarOverlay, mLP);

            mOverlay.setVisibility(View.VISIBLE);
        }else{
            if(mStatusBarOverlay.getWindowToken() != null){
                mWindowManager.removeView(mStatusBarOverlay);
            }
            mOverlay.setVisibility(View.GONE);
        }
    }

    public void showProfileOptions(Boolean show, Integer profileId){
        showHideOverlay(show);

        if (show){
            isProfileOptionsPanelShown = true;

            Animation bottomUp = AnimationUtils.loadAnimation(MainActivity.this,
                    R.anim.bottom_up);
            mProfileOptionsPanel.startAnimation(bottomUp);
            mProfileOptionsPanel.setVisibility(View.VISIBLE);
        }else{
            isProfileOptionsPanelShown = false;
            Animation bottomDown = AnimationUtils.loadAnimation(MainActivity.this,
                    R.anim.bottom_down);
            mProfileOptionsPanel.startAnimation(bottomDown);
            mProfileOptionsPanel.setVisibility(View.GONE);
        }
    }

    /**
     * Shows and Hides context options panel
     * @param show
     */
    protected void showContextOptions(Boolean show, Integer dopeNum){

        if (dopeNum != null) {
            mContextOptionsDopeNum = dopeNum;
        }

        showHideOverlay(show);

        if (show){
            isContextOptionsPanelShown = true;
            Animation bottomUp = AnimationUtils.loadAnimation(MainActivity.this,
                    R.anim.bottom_up);
            mContextOptionsPanel.startAnimation(bottomUp);
            mContextOptionsPanel.setVisibility(View.VISIBLE);
        }else{
            isContextOptionsPanelShown = false;
            Animation bottomDown = AnimationUtils.loadAnimation(MainActivity.this,
                    R.anim.bottom_down);
            mContextOptionsPanel.startAnimation(bottomDown);
            mContextOptionsPanel.setVisibility(View.GONE);
        }
    }
}
