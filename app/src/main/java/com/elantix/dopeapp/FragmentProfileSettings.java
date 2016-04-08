package com.elantix.dopeapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by oleh on 4/7/16.
 */
public class FragmentProfileSettings extends Fragment implements View.OnClickListener{
    private View mFragmentView;
    private LinearLayout mEditProfile;
    private LinearLayout mChangePassword;
    private LinearLayout mAboutDope;
    private LinearLayout mShareWithFriends;
    private LinearLayout mContactUs;
    private TextView mRestorePurchases;
    private TextView mLogOut;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_profile_settings, container, false);

        mEditProfile = (LinearLayout) mFragmentView.findViewById(R.id.profile_settings_fragment_edit_profile);
        mChangePassword= (LinearLayout) mFragmentView.findViewById(R.id.profile_settings_fragment_change_password);
        mAboutDope = (LinearLayout) mFragmentView.findViewById(R.id.profile_settings_fragment_about_dope);
        mShareWithFriends = (LinearLayout) mFragmentView.findViewById(R.id.profile_settings_fragment_share_with_friends);
        mContactUs = (LinearLayout) mFragmentView.findViewById(R.id.profile_settings_fragment_contact_us);
        mRestorePurchases = (TextView) mFragmentView.findViewById(R.id.profile_settings_fragment_restore_purchases);
        mLogOut = (TextView) mFragmentView.findViewById(R.id.profile_settings_fragment_log_out);

        mEditProfile.setOnClickListener(this);
        mChangePassword.setOnClickListener(this);
        mAboutDope.setOnClickListener(this);
        mShareWithFriends.setOnClickListener(this);
        mContactUs.setOnClickListener(this);
        mRestorePurchases.setOnClickListener(this);
        mLogOut.setOnClickListener(this);


        return mFragmentView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mEditProfile.getId()){
            Intent intent = new Intent(getActivity(), ProfileSettingsActivity.class);
            getActivity().startActivity(intent);
        }else if (id == mChangePassword.getId()){
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            getActivity().startActivity(intent);
        }else if (id == mAboutDope.getId()){
            Intent intent = new Intent(getActivity(), AboutDopeActivity.class);
            getActivity().startActivity(intent);
        }else if (id == mShareWithFriends.getId()){
            Intent intent = new Intent(getActivity(), ShareProfileActivity.class);
            intent.putExtra("own", true);
            getActivity().startActivity(intent);
        }else if (id == mContactUs.getId()){
            Utilities.contactUs(getActivity());
        }else if (id == mRestorePurchases.getId()){

        }else if (id == mLogOut.getId()){
            Utilities.logOutWithConfirmation(getActivity());
        }
    }
}
