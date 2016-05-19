package com.elantix.dopeapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 3/17/16.
 */
public class FragmentFriendsFirstScreen extends Fragment {

    protected View fragmentView;
    FancyButton fancyButtonInstagram;
    FancyButton fancyButtonSearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_friends_first_screen, container, false);
        fancyButtonInstagram = (FancyButton) fragmentView.findViewById(R.id.instagram_button);
        fancyButtonSearch = (FancyButton) fragmentView.findViewById(R.id.search_button);
        buttonsAppearenceHandling();
        fancyButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).switchPageHandler(MainActivity.Page.FriendsSearch);
            }
        });


        return fragmentView;
    }

    private void buttonsAppearenceHandling(){
        android.view.ViewGroup.LayoutParams params = fancyButtonInstagram.getLayoutParams();
        int height = params.height;

        fancyButtonInstagram.getIconImageObject().setLayoutParams(new LinearLayout.LayoutParams(80, 80));
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        float n = getActivity().getResources().getDimension(R.dimen.friends_profile_message_margin_4);
        lp1.setMargins(0, (int)n, 0, 0);
        fancyButtonInstagram.setLayoutParams(lp1);

        fancyButtonSearch.getIconImageObject().setLayoutParams(new LinearLayout.LayoutParams(80, 80));
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        n = getActivity().getResources().getDimension(R.dimen.friends_profile_message_margin_5);
        lp2.setMargins(0, (int)n, 0, 0);
        fancyButtonSearch.setLayoutParams(lp2);


    }
}
