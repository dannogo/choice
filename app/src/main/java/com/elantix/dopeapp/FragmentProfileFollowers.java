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

    private HttpKit http;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int mTotalItemCount = 0;
    final private int mPageCount = 50;
    private int mPageNum = 1;
    public boolean isNewFetch = true;
    AdapterSearchFriends mAdapter;
    private String param6;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_search_friends, container, false);

        RelativeLayout searchContainer = (RelativeLayout) mFragmentView.findViewById(R.id.search_friends_search_field_container);
        searchContainer.setVisibility(View.GONE);

        recyclerView = (RecyclerView) mFragmentView.findViewById(R.id.search_friends_friends_list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();


                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount * 0.65) {
                            loading = false;

                            if (mPageNum <= (mTotalItemCount / mPageCount)) {
                                mPageNum++;
                                Log.e("SearchFriends", "mPageNum: " + mPageNum);
                                isNewFetch = false;
//                                sendFriendsRequest(String.valueOf(mPageNum), mQuery, String.valueOf(mPageCount));
                                sentGetFollowersRequest(String.valueOf(mPageNum), String.valueOf(mPageCount));
                            }


                        }
                    }
                }
            }
        });

        Bundle bundle = this.getArguments();

        Boolean isFollowers = bundle.getBoolean("followers", true);

        if (isFollowers){
            type = Utilities.FollowingListType.ProfileFollowers;
            param6 = "followers";
        }else{
            type = Utilities.FollowingListType.ProfileFollowing;
            param6 = "followings";
        }
        http = new HttpKit(getActivity());
        sentGetFollowersRequest(String.valueOf(mPageNum), String.valueOf(mPageCount));

        ChainLink chainLink = new ChainLink(MainActivity.Page.ProfileFollowers);
        chainLink.bundleData.put("followers", isFollowers);
        ProfileInfo profToChain = new ProfileInfo(Utilities.sCurProfile);
        chainLink.data.add(profToChain);

        Utilities.sFragmentHistory.add(chainLink);
        Log.w("FragmentProfileFollower", "HISTORYCHAIN LENGTH: " + Utilities.sFragmentHistory.size());

        return mFragmentView;
    }

    private void sentGetFollowersRequest(String page, String count){
        http.getFollowers(Utilities.sCurProfile.id, Utilities.sToken, null, page, count, null, param6);
    }

    public void setFollowersInfo(ProfileInfo[] followers, int totalCount){

        loading = true;
        mTotalItemCount = totalCount;
        if (isNewFetch) {
            mAdapter = new AdapterSearchFriends(getActivity(), Utilities.FollowingListType.FriendsSearch, followers);
            recyclerView.setAdapter(mAdapter);
        }else{
            int positionStart = mAdapter.mUsers.size()-1;
            for (int i=0; i<followers.length; i++){
                mAdapter.mUsers.add(followers[i]);
            }
            mAdapter.notifyItemRangeInserted(positionStart, followers.length);
        }

    }
}
