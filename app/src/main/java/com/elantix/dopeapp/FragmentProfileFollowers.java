package com.elantix.dopeapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by oleh on 4/3/16.
 */
public class FragmentProfileFollowers extends Fragment {

    private View mFragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_search_friends, container, false);

        RelativeLayout searchContainer = (RelativeLayout) mFragmentView.findViewById(R.id.search_friends_search_field_container);
        searchContainer.setVisibility(View.GONE);

        RecyclerView recyclerView = (RecyclerView) mFragmentView.findViewById(R.id.search_friends_friends_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        Bundle bundle = this.getArguments();
        Utilites.FollowingListType type;
        if (bundle.getBoolean("followers")){
            type = Utilites.FollowingListType.ProfileFollowers;
        }else{
            type = Utilites.FollowingListType.ProfileFollowing;
        }

        AdapterSearchFriends adapter = new AdapterSearchFriends(getActivity(), type);
        recyclerView.setAdapter(adapter);

        return mFragmentView;
    }
}
