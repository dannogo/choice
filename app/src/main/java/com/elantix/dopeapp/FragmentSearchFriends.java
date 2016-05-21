package com.elantix.dopeapp;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by oleh on 3/30/16.
 */
public class FragmentSearchFriends extends Fragment {

    private View mFragmentView;
    private RecyclerView mRecyclerView;
    private EditText mSearchField;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private HttpKit http;
    private int mTotalItemCount = 0;
    final private int mPageCount = 50;
    private int mPageNum = 1;
    private String mQuery;
    public boolean isNewFetch = true;
    AdapterSearchFriends mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_search_friends, container, false);

        http = new HttpKit(getActivity());
        mAdapter = new AdapterSearchFriends(getActivity(), null, null);

        mRecyclerView = (RecyclerView) mFragmentView.findViewById(R.id.search_friends_friends_list);
        mSearchField = (EditText) mFragmentView.findViewById(R.id.search_friends_search_field);
        final Handler handler = new Handler();

        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (!mSearchField.getText().toString().equals("")) {
                            mQuery = mSearchField.getText().toString();
                        } else {
                            mQuery = null;
                        }
                        mTotalItemCount = 0;
                        isNewFetch = true;
                        mPageNum = 1;
                        sendFriendsRequest(null, mQuery, null);
                    }
                }, 1300);
            }
        });

        mSearchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    handler.removeCallbacksAndMessages(null);
                    String query;
                    if (!mSearchField.getText().toString().equals("")) {
                        query = mSearchField.getText().toString();
                    }else {
                        query = null;
                    }
                    isNewFetch = true;
                    mPageNum = 1;
                    sendFriendsRequest(null, query, null);
                    return true;
                }
                return false;
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0){
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();


                    if (loading){
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount * 0.65){
                            loading = false;

                            if (mPageNum <= (mTotalItemCount / mPageCount)){
                                mPageNum++;
                                Log.e("SearchFriends", "mPageNum: "+mPageNum);
                                isNewFetch = false;
                                sendFriendsRequest(String.valueOf(mPageNum), mQuery, String.valueOf(mPageCount));
                            }


                        }
                    }
                }
            }
        });

        if (Utilities.sToken == null){
            Toast.makeText(getActivity(), "You should log in first", Toast.LENGTH_SHORT).show();
        }else {
            isNewFetch = true;
            mPageNum = 1;
            sendFriendsRequest(null, null, null);
        }

        return mFragmentView;
    }

    private void sendFriendsRequest(String page, String query, String count){
        http.getFriends(Utilities.sToken, page, query, count, null);
    }


    public void setUsersInfo(ProfileInfo[] users, int totalCount){
        loading = true;
        mTotalItemCount = totalCount;
        if (isNewFetch) {
            mAdapter = new AdapterSearchFriends(getActivity(), Utilities.FollowingListType.FriendsSearch, users);
            mRecyclerView.setAdapter(mAdapter);
        }else{
            int positionStart = mAdapter.mUsers.size()-1;
            for (int i=0; i<users.length; i++){
                mAdapter.mUsers.add(users[i]);
            }
            mAdapter.notifyItemRangeInserted(positionStart, users.length);
        }
    }
}
