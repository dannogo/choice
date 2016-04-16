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
 * Created by oleh on 4/12/16.
 */
public class FragmentAuthSignInDope extends Fragment {

    private View mFragmentView;
    private FancyButton mFacebookBtn;
    private FancyButton mTwitterBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mFragmentView = inflater.inflate(R.layout.fragment_auth_sign_in_dope, container, false);

        mFacebookBtn = (FancyButton) mFragmentView.findViewById(R.id.auth_sign_in_dope_facebook_button);
        mTwitterBtn = (FancyButton) mFragmentView.findViewById(R.id.auth_sign_in_dope_twitter_button);
        buttonsAppearanceHandling();

        return mFragmentView;
    }

    private void buttonsAppearanceHandling(){

        int iconRightMargin = (int) getResources().getDimension(R.dimen.fancy_button_icon_margin_right);
        int iconRegularSize = (int) getResources().getDimension(R.dimen.fancy_button_regular_icon_size);
        int iconRegularSize2 = (int) getResources().getDimension(R.dimen.fancy_button_regular_icon_size_2);

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(iconRegularSize2, iconRegularSize2);
        lp1.setMargins(0, 0, iconRightMargin, 0);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(iconRegularSize, iconRegularSize);
        lp2.setMargins(0, 0, iconRightMargin, 0);

        mFacebookBtn.getIconImageObject().setLayoutParams(lp1);
        mTwitterBtn.getIconImageObject().setLayoutParams(lp2);
    }
}
