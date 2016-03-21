package com.elantix.dopeapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 3/19/16.
 */
public class FragmentNewMessage extends Fragment {

    private View fragmentView;
    private FancyButton fancyButton;
//    private RelativeLayout bottomArea;
//    private EditText search;
    private RecyclerView list;
    private MessageContactAdapter adapter;

    public enum ListType{
        First, Second, Third
    }
    protected ListType currentListType = ListType.First;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_new_message, container, false);
        fancyButton = (FancyButton) fragmentView.findViewById(R.id.fancy_button);
//        search = (EditText) fragmentView.findViewById(R.id.search);
//        bottomArea = (RelativeLayout) fragmentView.findViewById(R.id.bottom_area);
        list = (RecyclerView) fragmentView.findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(layoutManager);
        adapter = new MessageContactAdapter(getActivity());
        list.setAdapter(adapter);

        MessageActivity messageActivity = ((MessageActivity)getActivity());

        messageActivity.rightToolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentListType = ListType.Second;
                adapter.listType = ListType.Second;
                adapter.notifyDataSetChanged();
                ((RelativeLayout)((MessageActivity)getActivity()).findViewById(R.id.bottom_area)).setVisibility(View.VISIBLE);
                ((MessageActivity)getActivity()).rightToolbarButton.setVisibility(View.GONE);
                ((MessageActivity)getActivity()).leftToolbarButton.setImageResource(R.drawable.toolbar_cross_icon);
                ((MessageActivity)getActivity()).toolbarTitle.setText(R.string.message_title_3);

                FancyButton fancyButton =((FancyButton)((MessageActivity)getActivity()).findViewById(R.id.fancy_button));
                fancyButton.setIconResource(R.drawable.button_users_icon);
                fancyButton.setText(getString(R.string.create_group));

            }
        });
        messageActivity.leftToolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentListType) {
                    case First:
//                        ((MessageActivity)getActivity()).rightToolbarButton.setVisibility(View.VISIBLE);
                        MessageActivity mA = ((MessageActivity)getActivity());
                        mA.switchPageHandler(MessageActivity.DirectMessages.NoMessage, R.string.message_title_1);
                        break;
                    case Second:
                        ((MessageActivity)getActivity()).leftToolbarButton.setImageResource(R.drawable.toolbar_left_arrow);
                        currentListType = ListType.First;
                        adapter.listType = ListType.First;
                        adapter.notifyDataSetChanged();
                        ((RelativeLayout)((MessageActivity)getActivity()).findViewById(R.id.bottom_area)).setVisibility(View.GONE);
                        ((MessageActivity)getActivity()).rightToolbarButton.setVisibility(View.VISIBLE);
                        break;
                    case Third:
                        ((MessageActivity)getActivity()).leftToolbarButton.setImageResource(R.drawable.toolbar_cross_icon);
                        currentListType = ListType.Second;
                        adapter.listType = ListType.Second;
                        adapter.notifyDataSetChanged();
                        adapter.checkedItems = new ArrayList<Integer>();
                        FancyButton fancyButton =((FancyButton)((MessageActivity)getActivity()).findViewById(R.id.fancy_button));
                        fancyButton.setIconResource(R.drawable.button_users_icon);
                        fancyButton.setText(getString(R.string.create_group));
                        break;

                }
            }
        });

        buttonsAppearanceHandling();
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final MessageActivity messageActivity = ((MessageActivity)getActivity());
        messageActivity.rightToolbarButton.setImageResource(R.drawable.edit_icon);
        messageActivity.rightToolbarButton.setVisibility(View.VISIBLE);
        ((FancyButton)messageActivity.findViewById(R.id.fancy_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FancyButton fancyButton =((FancyButton)((MessageActivity)getActivity()).findViewById(R.id.fancy_button));
                switch (currentListType) {
                    case First:
                        break;
                    case Second:
                        currentListType = ListType.Third;
                        adapter.listType = ListType.Third;
                        adapter.notifyDataSetChanged();
                        ((MessageActivity)getActivity()).leftToolbarButton.setImageResource(R.drawable.toolbar_left_arrow);
                        ((MessageActivity)getActivity()).toolbarTitle.setText(R.string.message_title_4);
                        fancyButton.setIconResource(R.drawable.paper_plane);
                        fancyButton.setText(getString(R.string.send_group_message));
                        break;
                    case Third:
                        messageActivity.switchPageHandler(MessageActivity.DirectMessages.TabPlus, R.string.message_title_5);
                        break;
                }
            }
        });
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
