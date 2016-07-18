package com.elantix.dopeapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Arrays;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by oleh on 4/12/16.
 */
public class FragmentAuthSignInDope extends Fragment implements View.OnClickListener{

    private View mFragmentView;
    private FancyButton mFacebookBtn;
    private FancyButton mTwitterBtn;
    private TextView mAlreadyHaveUsername;
    private TwitterAuthClient twitterAuthClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mFragmentView = inflater.inflate(R.layout.fragment_auth_sign_in_dope, container, false);

        mFacebookBtn = (FancyButton) mFragmentView.findViewById(R.id.auth_sign_in_dope_facebook_button);
        mFacebookBtn.setOnClickListener(this);
        mTwitterBtn = (FancyButton) mFragmentView.findViewById(R.id.auth_sign_in_dope_twitter_button);
//        mTwitterBtn.setOnClickListener(this);
        mAlreadyHaveUsername = (TextView) mFragmentView.findViewById(R.id.auth_sign_in_already_have_username);
        mAlreadyHaveUsername.setOnClickListener(this);

        buttonsAppearanceHandling();

        return mFragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("AuthSignInDope", "onViewCreated");

//        twitterAuthClient= new TwitterAuthClient();

        mTwitterBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ((AuthActivity)getActivity()).onTwitterLogin();
//                twitterAuthClient.authorize(getActivity(), new com.twitter.sdk.android.core.Callback<TwitterSession>() {
//
//                    @Override
//                    public void success(Result<TwitterSession> twitterSessionResult) {
//                        TwitterSession sessionData = twitterSessionResult.data;
//                        String uid = String.valueOf(sessionData.getUserId());
//                        Log.w("WelcomeActivity", "userId: " + uid);
//                        String username = sessionData.getUserName();
////                        Log.w("WelcomeActivity", "username: " + username);
//                        sessionData.getAuthToken();
////                        sessionData.getUserId()
//
//                        HttpKit http = new HttpKit(getActivity());
//                        String[] params = {"twitter", uid, null};
//                        http.checkUsername("tw_" + uid, "twitter", params);
//
////                        TwitterAuthClient authClient = new TwitterAuthClient();
////                        authClient.requestEmail(new TwitterSession(sessionData.getAuthToken(), sessionData.getUserId(), username), new Callback<String>() {
////                            @Override
////                            public void success(Result<String> result) {
////                                // Do something with the result, which provides the email address
////                                Log.w("WelcomeActivity", "Email should be here: "+result.data.toString());
////                            }
////
////                            @Override
////                            public void failure(TwitterException exception) {
////                                // Do something on failure
////                                Log.w("WelcomeActivity", "Email failure: "+exception.toString());
////                            }
////                        });
//                    }
//
//                    @Override
//                    public void failure(TwitterException e) {
//                        e.printStackTrace();
//                    }
//                });


            }
        });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            Log.e("FragmentAuthSignInDope", "onActivityResult");
        if (requestCode == Utilities.SIGN_IN) {
            if(resultCode == Activity.RESULT_OK){
                ((AuthActivity)getActivity()).afterLoginAction();
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mFacebookBtn.getId()){
            ((AuthActivity)getActivity()).onFblogin();
        }else if (id == mTwitterBtn.getId()) {

        }else if(id == mAlreadyHaveUsername.getId()){
            Intent intent = new Intent(getActivity(), SignInActivity.class);
            intent.putExtra("activity", "auth");
            startActivityForResult(intent, Utilities.SIGN_IN);
        }
    }

}
