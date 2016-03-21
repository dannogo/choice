package com.elantix.dopeapp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 3/19/16.
 */
public class FragmentTabPlus extends Fragment {

    View fragmentView;
    private FancyButton sendNewMessageButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_tab_plus, container, false);
        sendNewMessageButton = (FancyButton) fragmentView.findViewById(R.id.send_new_message);
        buttonsAppearanceHandling();
        sendNewMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                getActivity().startActivity(intent);
            }
        });

        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final MessageActivity messageActivity = ((MessageActivity)getActivity());
        messageActivity.leftToolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageActivity.switchPageHandler(MessageActivity.DirectMessages.NewMessage1, R.string.message_title_2);
            }
        });
    }

    private void buttonsAppearanceHandling(){
        android.view.ViewGroup.LayoutParams params = sendNewMessageButton.getLayoutParams();
        int height = params.height;

        sendNewMessageButton.getIconImageObject().setLayoutParams(new LinearLayout.LayoutParams(80, 80));
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        float n = getActivity().getResources().getDimension(R.dimen.friends_profile_message_margin_4);
        lp1.setMargins(0, (int)n, 0, 0);
        lp1.addRule(RelativeLayout.CENTER_IN_PARENT);
        sendNewMessageButton.setLayoutParams(lp1);
    }
}
