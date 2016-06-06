package com.elantix.dopeapp;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elantix.dopeapp.entities.ConversationInfo;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 3/19/16.
 */
public class FragmentNewMessage extends Fragment {

    private View fragmentView;
    private FancyButton fancyButton;
    private RelativeLayout bottomArea;
    private EditText mSearch;
    private RelativeLayout mSearchContainer;
    private RecyclerView list;
    public MessageContactAdapter adapter;
    private MessageActivity activity;

    public enum ListType{
        First, Second, Third
    }

    protected ListType currentListType;
    private static HttpChat http;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int mTotalItemCount = 0;
    final private int mPageCount = 50;
    private int mPageNum = 1;
    private String mQuery;
    public boolean isNewFetch = true;
    private boolean loading = true;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_new_message, container, false);
        activity = ((MessageActivity)getActivity());


        fancyButton = (FancyButton) fragmentView.findViewById(R.id.fancy_button);
        mSearch = (EditText) fragmentView.findViewById(R.id.search);
        mSearchContainer = (RelativeLayout) fragmentView.findViewById(R.id.searchContainer);
        bottomArea = (RelativeLayout) fragmentView.findViewById(R.id.bottom_area);
        list = (RecyclerView) fragmentView.findViewById(R.id.list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(layoutManager);

        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                sendGetCrossFollowingFriendsRequest(mQuery, mPageNum, mPageCount);
                            }


                        }
                    }
                }
            }
        });

        final Handler handler = new Handler();

        mSearch.addTextChangedListener(new TextWatcher() {
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
                        if (!mSearch.getText().toString().equals("")) {
                            mQuery = mSearch.getText().toString();
                        } else {
                            mQuery = null;
                        }
                        mTotalItemCount = 0;
                        isNewFetch = true;
                        mPageNum = 1;
                        sendGetCrossFollowingFriendsRequest(mQuery, mPageNum, mPageCount);
                    }
                }, 1300);
            }
        });

        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    handler.removeCallbacksAndMessages(null);
                    String query;
                    if (!mSearch.getText().toString().equals("")) {
                        query = mSearch.getText().toString();
                    } else {
                        query = null;
                    }
                    isNewFetch = true;
                    mPageNum = 1;
//                    sendFriendsRequest(null, query, null);
                    sendGetCrossFollowingFriendsRequest(query, mPageNum, mPageCount);
                    return true;
                }
                return false;
            }
        });

        Bundle bundle = getArguments();
        int type = bundle.getInt("type", -1);

        http = new HttpChat(getActivity());

        switch (type){
            case 1:
                currentListType = ListType.First;
                break;
            case 2:
                currentListType = ListType.Second;
                http.getCrossFollowingFriends(Utilities.sToken, null, null, null, null);
                break;
            case 3:
                currentListType = ListType.Third;
                http.getCrossFollowingFriends(Utilities.sToken, null, null, null, null);
                break;
            default:
                currentListType = ListType.First;
        }

        setProperFragmentAppearance();


        activity.rightToolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentListType = ListType.Second;
                mPageNum = 1;
                mQuery = null;
                isNewFetch = true;
                http.getCrossFollowingFriends(Utilities.sToken, mQuery, mPageNum, mPageCount, null);
                currentListType = ListType.Second;
//                adapter.listType = ListType.Second;
//                adapter.notifyDataSetChanged();

                setProperFragmentAppearance();
            }
        });
        activity.leftToolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentListType) {
                    case First:
                        activity.finish();
                        break;
                    case Second:
                        currentListType = ListType.First;
                        mPageNum = 1;
                        mQuery = null;
                        http.getConversationList(Utilities.sToken, Utilities.sUid, String.valueOf(mPageNum), String.valueOf(mPageCount));

//                        adapter.listType = ListType.First;
//                        adapter.notifyDataSetChanged();
                        break;
                    case Third:
                        currentListType = ListType.Second;
                        adapter.listType = ListType.Second;
                        adapter.notifyDataSetChanged();
//                        adapter.checkedItems = new ArrayList<Integer>();
                        adapter.selectedIds = new ArrayList<Integer>();
                        break;

                }
                setProperFragmentAppearance();
            }
        });

        buttonsAppearanceHandling();

        if (currentListType == ListType.First){
            setDataToAdapter(((MessageActivity) getActivity()).convCount, ((MessageActivity) getActivity()).conversations);
        }

        return fragmentView;
    }

    private void sendGetCrossFollowingFriendsRequest(String query, Integer page, Integer count){
        String exlude_ids = null;
        http.getCrossFollowingFriends(Utilities.sToken, query, page, count, exlude_ids);
    }

    public void setDataToAdapter(int count, ArrayList<ConversationInfo> conversations){
        loading = true;
        mTotalItemCount = count;
        if (isNewFetch) {
            adapter = new MessageContactAdapter(getActivity(), conversations, currentListType);
            list.setAdapter(adapter);
        }else{
            int positionStart = adapter.mConvs.size()-1;
            for (int i=0; i<conversations.size(); i++){
                adapter.mConvs.add(conversations.get(i));
            }
            adapter.notifyItemRangeInserted(positionStart, conversations.size());
        }
    }

    public void setDataToAdapter(int count, ProfileInfo[] users){
        Log.e("NewMessage", "setDataToAdapter Profile");
        loading = true;
        mTotalItemCount = count;
        if (isNewFetch) {
            adapter = new MessageContactAdapter(getActivity(), users, currentListType);
            list.setAdapter(adapter);
        }else{
            int positionStart = adapter.mUsers.size()-1;
            for (int i=0; i<users.length; i++){
                adapter.mUsers.add(users[i]);
            }
            adapter.notifyItemRangeInserted(positionStart, users.length);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentListType) {
                    case First:
                        break;
                    case Second:
                        currentListType = ListType.Third;
                        adapter.listType = ListType.Third;
                        adapter.notifyDataSetChanged();
                        setProperFragmentAppearance();
                        break;
                    case Third:
//                        activity.switchPageHandler(MessageActivity.DirectMessages.TabPlus, R.string.message_title_5);
                        if (MessageContactAdapter.selectedIds.size() > 0) {
                            StringBuilder builder = new StringBuilder("");
                            String prefix = "";
                            for (int i = 0; i < MessageContactAdapter.selectedIds.size(); i++) {
                                builder.append(prefix);
                                prefix = ",";
                                builder.append(String.valueOf(MessageContactAdapter.selectedIds.get(i)));
                            }

//                        Log.e("FragmentNewMessage", builder.toString());
                            http.createConversation(Utilities.sToken, builder.toString());
                        }else {
                            Toast.makeText(getActivity(), "Choose at least one interlocutor", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
    }

    public void removeConversation(int position){
        if (adapter != null && currentListType == ListType.First) {
            adapter.mConvs.remove(position);
            adapter.notifyItemRemoved(position);
        }
    }

    private void setProperFragmentAppearance(){
        switch (currentListType){
            case First:
                mSearchContainer.setVisibility(View.GONE);
                bottomArea.setVisibility(View.GONE);
                activity.toolbarTitle.setText(R.string.message_title_2);
                activity.rightToolbarButton.setVisibility(View.VISIBLE);
                activity.leftToolbarButton.setImageResource(R.drawable.toolbar_left_arrow);
                activity.rightToolbarButton.setImageResource(R.drawable.edit_icon);
                break;
            case Second:
                mSearchContainer.setVisibility(View.VISIBLE);
                bottomArea.setVisibility(View.VISIBLE);
                activity.rightToolbarButton.setVisibility(View.GONE);
                activity.leftToolbarButton.setImageResource(R.drawable.toolbar_cross_icon);
                activity.toolbarTitle.setText(R.string.message_title_3);
                fancyButton.setIconResource(R.drawable.button_users_icon);
                fancyButton.setText(getString(R.string.create_group));
                break;
            case Third:
                mSearchContainer.setVisibility(View.VISIBLE);
                activity.leftToolbarButton.setImageResource(R.drawable.toolbar_left_arrow);
                activity.toolbarTitle.setText(R.string.message_title_4);
                fancyButton.setIconResource(R.drawable.paper_plane);
                fancyButton.setText(getString(R.string.send_group_message));
                break;
        }
    }

    private void buttonsAppearanceHandling(){
        android.view.ViewGroup.LayoutParams params = fancyButton.getLayoutParams();
        int height = params.height;

        fancyButton.getIconImageObject().setLayoutParams(new LinearLayout.LayoutParams(80, 80));
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        float n = getActivity().getResources().getDimension(R.dimen.friends_profile_message_margin_4);
        lp1.setMargins(0, (int)n, 0, 0);
        lp1.addRule(RelativeLayout.CENTER_IN_PARENT);
        fancyButton.setLayoutParams(lp1);

    }
}
