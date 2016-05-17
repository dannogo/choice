package com.elantix.dopeapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by oleh on 3/30/16.
 */
public class FragmentSearchFriends extends Fragment {

    private View mFragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_search_friends, container, false);
        RecyclerView recyclerView = (RecyclerView) mFragmentView.findViewById(R.id.search_friends_friends_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        AdapterSearchFriends adapter = new AdapterSearchFriends(getActivity(), Utilities.FollowingListType.FriendsSearch, null, null, null);
        recyclerView.setAdapter(adapter);

        return mFragmentView;
    }
}
