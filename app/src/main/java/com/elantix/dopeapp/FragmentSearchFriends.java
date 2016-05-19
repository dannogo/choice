package com.elantix.dopeapp;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_search_friends, container, false);
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
//                handler.removeMessages(0);
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        String query;
                        if (!mSearchField.getText().toString().equals("")) {
                            query = mSearchField.getText().toString();
                        } else {
                            query = null;
                        }
                        sendFriendsRequest(null, query, null);

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
                    sendFriendsRequest(null, query, null);
                    return true;
                }
                return false;
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        if (Utilities.sToken == null){
            Toast.makeText(getActivity(), "You should log in first", Toast.LENGTH_SHORT).show();
        }else {
//            HttpKit http = new HttpKit(getActivity());
//            http.getFriends(Utilities.sToken, null, null, null, null);
            sendFriendsRequest(null, null, null);
        }

//        AdapterSearchFriends adapter = new AdapterSearchFriends(getActivity(), Utilities.FollowingListType.FriendsSearch, null, null, null);
//        recyclerView.setAdapter(adapter);

        return mFragmentView;
    }

    private void sendFriendsRequest(String page, String query, String count){
        HttpKit http = new HttpKit(getActivity());
        http.getFriends(Utilities.sToken, page, query, count, null);
    }

    public void setUsersInfo(ProfileInfo[] users, String[] usersIds, String[] followingsIds){
        AdapterSearchFriends adapter = new AdapterSearchFriends(getActivity(), Utilities.FollowingListType.FriendsSearch, users, usersIds, followingsIds);
        mRecyclerView.setAdapter(adapter);
    }
}
