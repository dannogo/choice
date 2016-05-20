package com.elantix.dopeapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by oleh on 4/3/16.
 */
public class FragmentProfileFollowers extends Fragment {

    private View mFragmentView;
    Utilities.FollowingListType type;
    RecyclerView recyclerView;
    String[] mFollowings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_search_friends, container, false);

        RelativeLayout searchContainer = (RelativeLayout) mFragmentView.findViewById(R.id.search_friends_search_field_container);
        searchContainer.setVisibility(View.GONE);

        recyclerView = (RecyclerView) mFragmentView.findViewById(R.id.search_friends_friends_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        Bundle bundle = this.getArguments();

        Boolean isFollowers = bundle.getBoolean("followers", true);
        String param6;
        if (isFollowers){
            type = Utilities.FollowingListType.ProfileFollowers;
            param6 = "followers";
        }else{
            type = Utilities.FollowingListType.ProfileFollowing;
            param6 = "followings";
        }
        HttpKit http = new HttpKit(getActivity());
        http.getFollowers(Utilities.sCurProfile.id, Utilities.sToken, null, null, null, null, param6);

        ChainLink chainLink = new ChainLink(MainActivity.Page.ProfileFollowers);
        chainLink.bundleData.put("followers", isFollowers);
        ProfileInfo profToChain = new ProfileInfo(Utilities.sCurProfile);
        chainLink.data.add(profToChain);

        Utilities.sFragmentHistory.add(chainLink);
        Log.w("FragmentProfileFollower", "HISTORYCHAIN LENGTH: " + Utilities.sFragmentHistory.size());

        return mFragmentView;
    }

//    public void setFollowersInfo(ProfileInfo[] followers, String[] followersIds, String[] followingsIds){
//        AdapterSearchFriends adapter = new AdapterSearchFriends(getActivity(), type, followers, followersIds, followingsIds);
//        recyclerView.setAdapter(adapter);
//    }

    public void setFollowersInfo(ProfileInfo[] followers){
        AdapterSearchFriends adapter = new AdapterSearchFriends(getActivity(), type, followers);
        recyclerView.setAdapter(adapter);
    }
}
