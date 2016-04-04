package com.elantix.dopeapp;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mikhaellopez.circularimageview.CircularImageView;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 3/31/16.
 */
public class FragmentProfileOverview extends Fragment implements View.OnClickListener{

    View mFragmentView;
    CircularImageView mAvatar;
    private FancyButton mFollowButton;
    private FancyButton mShareProfileButton;
    private LinearLayout mDopesInfo;
    private LinearLayout mFollowersInfo;
    private LinearLayout mFollowingsInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_profile_overview, container, false);

        mAvatar = (CircularImageView) mFragmentView.findViewById(R.id.profile_overview_avatar);

        mFollowButton = (FancyButton) mFragmentView.findViewById(R.id.profile_overview_follow_button);
        mFollowButton.getIconImageObject().setLayoutParams(new LinearLayout.LayoutParams(50, 50));

        mShareProfileButton = (FancyButton) mFragmentView.findViewById(R.id.profile_overview_share_profile_button);
        mShareProfileButton.setOnClickListener(this);

        mDopesInfo = (LinearLayout) mFragmentView.findViewById(R.id.profile_overview_dopes_info);
        mFollowersInfo = (LinearLayout) mFragmentView.findViewById(R.id.profile_overview_followers_info);
        mFollowingsInfo = (LinearLayout) mFragmentView.findViewById(R.id.profile_overview_followings_info);
        mDopesInfo.setOnClickListener(this);
        mFollowersInfo.setOnClickListener(this);
        mFollowingsInfo.setOnClickListener(this);

        RecyclerView recyclerView = (RecyclerView) mFragmentView.findViewById(R.id.profile_overview_posted_dopes_list);
        recyclerView.setFocusable(false);

        AdapterProfileOverview adapter = new AdapterProfileOverview(getActivity());
        recyclerView.setAdapter(adapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        return mFragmentView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mShareProfileButton.getId()){
            Intent intent = new Intent(getActivity(), ShareProfileActivity.class);
            getActivity().startActivity(intent);
        }else if (id == mDopesInfo.getId()){

        }else if (id == mFollowersInfo.getId()){
            ((MainActivity)getActivity()).switchPageHandler(MainActivity.Page.ProfileFollowers);
        }else if (id == mFollowingsInfo.getId()){
            ((MainActivity)getActivity()).switchPageHandler(MainActivity.Page.ProfileFollowings);
        }
    }
}
