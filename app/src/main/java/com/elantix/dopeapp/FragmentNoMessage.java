package com.elantix.dopeapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 3/19/16.
 */
public class FragmentNoMessage extends Fragment {

    View fragmentView;
    private FancyButton sendNewMessageButton;
    MessageActivity messageActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_dir_mess_nomessage, container, false);
        sendNewMessageButton = (FancyButton) fragmentView.findViewById(R.id.send_new_message);


        buttonsAppearanceHandling();
        ((MessageActivity)getActivity()).toolbarTitle.setText(R.string.message_title_1);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        messageActivity = ((MessageActivity)getActivity());
        sendNewMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageActivity.switchPageHandler(MessageActivity.DirectMessages.NewMessage1, 2);
            }
        });
    }

    private void buttonsAppearanceHandling(){
        android.view.ViewGroup.LayoutParams params = sendNewMessageButton.getLayoutParams();
        int height = params.height;

        sendNewMessageButton.getIconImageObject().setLayoutParams(new LinearLayout.LayoutParams(80, 80));
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        float n = getActivity().getResources().getDimension(R.dimen.friends_profile_message_margin_4);
        lp1.setMargins(0, (int)n, 0, 0);
        sendNewMessageButton.setLayoutParams(lp1);
    }
}
