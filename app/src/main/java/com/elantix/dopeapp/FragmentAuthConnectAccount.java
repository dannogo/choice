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
public class FragmentAuthConnectAccount extends Fragment implements View.OnClickListener{
    private View mFragmentView;
    private FancyButton mFacebookBtn;
    private FancyButton mTwitterBtn;
    private FancyButton mEmailBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_auth_connect_account, container, false);
        mFragmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AuthActivity)getActivity()).switchPageHandler(AuthActivity.AuthPage.SignInDope);
            }
        });

        mFacebookBtn = (FancyButton) mFragmentView.findViewById(R.id.auth_connect_account_facebook_button);
        mTwitterBtn = (FancyButton) mFragmentView.findViewById(R.id.auth_connect_account_twitter_button);
        mEmailBtn = (FancyButton) mFragmentView.findViewById(R.id.auth_connect_account_email_button);
        mFacebookBtn.setOnClickListener(this);
        mTwitterBtn.setOnClickListener(this);
        mEmailBtn.setOnClickListener(this);

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
        mEmailBtn.getIconImageObject().setLayoutParams(lp1);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mFacebookBtn.getId()){
            ((AuthActivity)getActivity()).mIsRegularAuthorization = false;
            ((AuthActivity)getActivity()).onFblogin();
        }else if (id == mTwitterBtn.getId()){

        }else if (id == mEmailBtn.getId()){
            ((AuthActivity)getActivity()).switchPageHandler(AuthActivity.AuthPage.LinkEmail);
        }
    }
}
