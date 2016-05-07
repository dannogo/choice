package com.elantix.dopeapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.ParagraphStyle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 3/31/16.
 */
public class FragmentProfileOverview extends Fragment implements View.OnClickListener{

    private View mFragmentView;
    private CircularImageView mAvatar;
    private ImageView mAvatarPlaceHolder;
    private FancyButton mFollowButton;
    private FancyButton mShareProfileButton;
    private LinearLayout mDopesInfo;
    private LinearLayout mFollowersInfo;
    private LinearLayout mFollowingsInfo;
    private Boolean mIsOwn;
//    private static int sNumberOfOwnDopes = 0;
    private TextView mUsername;
    private TextView mFirstLastNames;
    private LinearLayout mNoDopesLayout;
    private FancyButton mNoDopesButton;
    private RecyclerView mRecyclerView;
    private TextView mNumberOfDopesView;

    // TODO:
    // rename variable for Following/Edit_Profile buttons
    // change icon in Edit Profile button

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_profile_overview, container, false);

        Utilities.sCurProfile = null;
        Bundle bundle = this.getArguments();
        mIsOwn = bundle.getBoolean("own");

        mUsername = (TextView) mFragmentView.findViewById(R.id.profile_overview_user_name);
        mFirstLastNames = (TextView) mFragmentView.findViewById(R.id.profile_overview_user_description);
        mNoDopesLayout = (LinearLayout) mFragmentView.findViewById(R.id.profile_overview_there_are_no_dopes);
        mNumberOfDopesView = (TextView) mFragmentView.findViewById(R.id.profile_overview_number_of_dopes);
        mNoDopesButton = (FancyButton) mFragmentView.findViewById(R.id.profile_overview_there_are_no_dopes_button);
        mNoDopesButton.setOnClickListener(this);

        mAvatar = (CircularImageView) mFragmentView.findViewById(R.id.profile_overview_avatar);
        mAvatarPlaceHolder = (ImageView) mFragmentView.findViewById(R.id.profile_settings_user_icon);
        mRecyclerView = (RecyclerView) mFragmentView.findViewById(R.id.profile_overview_posted_dopes_list);

        mFollowButton = (FancyButton) mFragmentView.findViewById(R.id.profile_overview_follow_button);
        mFollowButton.setOnClickListener(this);
        ImageView followButtonIconView = mFollowButton.getIconImageObject();
        if (mIsOwn){
            followButtonIconView.setImageResource(R.drawable.profile_edit_pencil_white);
            mFollowButton.getTextViewObject().setText(R.string.profile_settings_edit_profile_left_button_text);
            followButtonIconView.setLayoutParams(new LinearLayout.LayoutParams(60, 60));

//            String username = (Utilities.profileUsername.isEmpty()) ? getActivity().getResources().getString(R.string.profile_settings_username_placeholder_text) : Utilities.profileUsername;
//            mUsername.setText(username);
//            String firstLastNames = (Utilities.profileFirstLastNames.isEmpty()) ? getActivity().getResources().getString(R.string.profile_settings_firstlastnames_placeholder_text) : Utilities.profileFirstLastNames;
//            mFirstLastNames.setText(firstLastNames);
//            mNumberOfDopesView.setText(String.valueOf(sNumberOfOwnDopes));

//            if (sNumberOfOwnDopes > 0){
//                mNoDopesLayout.setVisibility(View.GONE);
//                mRecyclerView.setVisibility(View.VISIBLE);
//                configurateRecyclerView();
//            }else{
//                mNoDopesLayout.setVisibility(View.VISIBLE);
//                mRecyclerView.setVisibility(View.GONE);
//            }

        }else{
            followButtonIconView.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
            mAvatar.setVisibility(View.VISIBLE);
//            Glide.with(this).load(R.drawable.ania2).into(mAvatar);
        }

        mShareProfileButton = (FancyButton) mFragmentView.findViewById(R.id.profile_overview_share_profile_button);
        mShareProfileButton.getIconImageObject().setVisibility(View.GONE);
        mShareProfileButton.setOnClickListener(this);

        mDopesInfo = (LinearLayout) mFragmentView.findViewById(R.id.profile_overview_dopes_info);
        mFollowersInfo = (LinearLayout) mFragmentView.findViewById(R.id.profile_overview_followers_info);
        mFollowingsInfo = (LinearLayout) mFragmentView.findViewById(R.id.profile_overview_followings_info);
        mDopesInfo.setOnClickListener(this);
        mFollowersInfo.setOnClickListener(this);
        mFollowingsInfo.setOnClickListener(this);

//        if (!mIsOwn) {
//            mNoDopesLayout.setVisibility(View.GONE);
//            mRecyclerView.setVisibility(View.VISIBLE);
//
//            configurateRecyclerView();
//        }

        String uid = (mIsOwn) ? Utilities.sUid : bundle.getString("uid");
        HttpKit http = new HttpKit(getActivity());
        http.getProfileInformation(uid, Utilities.sToken);
        http.getUsersDopes(uid, Utilities.sToken, null, null);

        return mFragmentView;
    }

    public void drawUserDopesPanel(DopeInfo[] dopes){
        if (dopes != null && dopes.length > 0){
            mNoDopesLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            configurateRecyclerView(dopes);
        }else{
            if (mIsOwn){
                mNoDopesLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }else {
                mNoDopesLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Configurates recyclerView
     */
    private void configurateRecyclerView(DopeInfo[] dopes){
        mRecyclerView.setFocusable(false);

        AdapterProfileOverview adapter = new AdapterProfileOverview(getActivity(), dopes, mIsOwn);
        mRecyclerView.setAdapter(adapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }

    public void setInfo(ProfileInfo info){

        Utilities.sCurProfile = info;
        Log.w("ProfileOverview", "info.id: "+info.id+"\nsUid: "+Utilities.sUid);
        if (info.id.equals(Utilities.sUid)){
            Utilities.sMyProfile = info;
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(Utilities.MY_PREFS_NAME, getActivity().MODE_PRIVATE).edit();
            editor.putString("uid", Utilities.sUid);
            editor.putString("token", Utilities.sToken);
            editor.putString("avatar", info.avatar);
            editor.putString("username", info.username);
            editor.putString("fullname", info.fullname);
            editor.putString("email", info.email);
            editor.commit();

//            public String username;
//            public String fullname;
//            public String email;
//            public String avatar;
//            public String id;
//            public String parent_id;
//            public String user_id;
//            public String dope_id;
//            public String comment;
//            public String date_create;

        }
//        Uri avatar = Uri.parse(info.avatar);
//        Glide.with(getActivity()).load(avatar).into(mAvatar);
        mUsername.setText(info.username);
        mFirstLastNames.setText(info.fullname);
        mNumberOfDopesView.setText(String.valueOf(info.dopes));

        if (Utilities.sCurProfile.avatar == null || Utilities.sCurProfile.avatar.isEmpty()){
        Log.e("ProfileOverview", "ava: "+Utilities.sCurProfile.avatar);
            mAvatarPlaceHolder.setVisibility(View.VISIBLE);
        }else{
            mAvatar.setVisibility(View.VISIBLE);
            if (Utilities.sCurProfile.avatar.startsWith("http")) {
                Glide.with(this).load(Uri.parse(Utilities.sCurProfile.avatar)).into(mAvatar);
            }else {
                Glide.with(this).load(new File(Utilities.sCurProfile.avatar).getPath()).into(mAvatar);
            }
        }

        TextView followersNum = (TextView) mFragmentView.findViewById(R.id.profile_overview_number_of_followERS);
        TextView followingsNum = (TextView) mFragmentView.findViewById(R.id.profile_overview_number_of_followINGS);
        followersNum.setText(String.valueOf(info.followers));
        followingsNum.setText(String.valueOf(info.followings));
    }





    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == mShareProfileButton.getId()){
            Intent intent = new Intent(getActivity(), ShareProfileActivity.class);
            intent.putExtra("own", mIsOwn);
            getActivity().startActivity(intent);
        }else if (id == mDopesInfo.getId()){

        }else if (id == mFollowersInfo.getId()){
            ((MainActivity)getActivity()).switchPageHandler(MainActivity.Page.ProfileFollowers);
        }else if (id == mFollowingsInfo.getId()){
            ((MainActivity)getActivity()).switchPageHandler(MainActivity.Page.ProfileFollowings);
        }else if(id == mFollowButton.getId()){
            if (mIsOwn){
                Intent i = new Intent(getActivity(), ProfileSettingsActivity.class);
                startActivityForResult(i, Utilities.EDIT_PROFILE);
            }
        }else if(id == mNoDopesButton.getId()){
            Intent intent = new Intent(getActivity(), TabPlusActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Utilities.EDIT_PROFILE) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                if (Utilities.sCurProfile.avatar != null) {
                    if (Utilities.sCurProfile.avatar.startsWith("http")) {
                        Glide.with(this).load(Uri.parse(Utilities.sCurProfile.avatar)).into(mAvatar);
                    }else {
                        Glide.with(this).load(new File(Utilities.sCurProfile.avatar).getPath()).into(mAvatar);
                    }
                    mAvatarPlaceHolder.setVisibility(View.GONE);
                    mAvatar.setVisibility(View.VISIBLE);
                }
                if (Utilities.sCurProfile.username != null && !Utilities.sCurProfile.username.isEmpty()) {
                    mUsername.setText(Utilities.sCurProfile.username);
                }
                if (Utilities.sCurProfile.fullname != null && !Utilities.sCurProfile.fullname.isEmpty()) {
                    mFirstLastNames.setText(Utilities.sCurProfile.fullname);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }
}
