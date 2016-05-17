package com.elantix.dopeapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import io.fabric.sdk.android.Fabric;

/**
 * Created by oleh on 4/12/16.
 */
public class AuthActivity extends AppCompatActivity {

    public Fragment mCurrentFragment;
    private AuthPage mPage = AuthPage.LinkEmail;

    private android.app.FragmentManager mManager = getFragmentManager();
    private FragmentTransaction mTransaction;
    private ImageView mArrowBtn;
    private CallbackManager callbackManager;
    public String mUsername;
    public String mPassword;
    public String mEmail;
    public boolean mIsRegularAuthorization = true;
    public ProgressDialog mProgressDialog;
    private static final String TWITTER_KEY = "iczt8Wb15b2V0IJPq3JdnCQ1p";
    private static final String TWITTER_SECRET = "5cPrXX8qkZgQGPJr0YGE8KOzkQTwxbpaP6b2mQFDUtMIsoZIDn";
    private TwitterAuthClient twitterAuthClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig));

        setContentView(R.layout.activity_auth);

        String fragmentToLaunch = getIntent().getStringExtra("fragment");
        if (fragmentToLaunch.equals("SignInDope")){
            mPage = AuthPage.SignInDope;
        }else{
            mPage = AuthPage.SetUsername;
        }

        mArrowBtn = (ImageView) findViewById(R.id.auth_arrow);

        setArrowBtnBehaviour(mPage);
        launchFragment(mPage);
    }

    public enum AuthPage{
        LinkEmail, SetUsername, EnterPassword, ConnectAccount, SignInDope
    }

    private void setArrowBtnBehaviour(AuthPage page){
        switch (page){
            case SetUsername:
                mArrowBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        switchPageHandler(AuthPage.LinkEmail);
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_CANCELED, returnIntent);
                        finish();
                    }
                });
                break;
            case EnterPassword:
                mArrowBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchPageHandler(AuthPage.SetUsername);
                    }
                });
                break;
            case ConnectAccount:
                mArrowBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchPageHandler(AuthPage.EnterPassword);
                    }
                });
                break;
            case LinkEmail:
                mArrowBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchPageHandler(AuthPage.ConnectAccount);
                    }
                });
                break;
            case  SignInDope:
                mArrowBtn.setImageResource(R.drawable.white_cross);
                float scale = getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (20*scale + 0.5f);
                mArrowBtn.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
                mArrowBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_CANCELED, returnIntent);
                        finish();
                    }
                });
                break;
        }
    }

    /**
     * Removes previous fragment, adds new fragment to activity;
     * changes title in toolbar;
     * changes appearance of toolbar buttons and their behaviour
     * @param newPage
     */
    protected void switchPageHandler(AuthPage newPage){
        removeFragment();
        mPage = newPage;
        setArrowBtnBehaviour(mPage);
        launchFragment(mPage);
    }

    private void launchFragment(AuthPage page){
        String fragmentTAG;
        switch (page){
            case SetUsername:
                mCurrentFragment = new FragmentAuthSetUsername();
                fragmentTAG = "SetUsername";
                break;
            case EnterPassword:
                mCurrentFragment = new FragmentAuthEnterPassword();
                fragmentTAG = "EnterPassword";
                break;
            case ConnectAccount:
                mCurrentFragment = new FragmentAuthConnectAccount();
                fragmentTAG = "ConnectAccount";
                break;
            case LinkEmail:
                mCurrentFragment = new FragmentAuthLinkEmail();
                fragmentTAG = "LinkEmail";
                break;
            case SignInDope:
                mCurrentFragment = new FragmentAuthSignInDope();
                fragmentTAG = "SignInDope";
                break;
            default:
                mCurrentFragment = new FragmentAuthLinkEmail();
                fragmentTAG = "LinkEmail";
        }
        mTransaction = mManager.beginTransaction();
        mTransaction.add(R.id.auth_fragment_container, mCurrentFragment, fragmentTAG);
        mTransaction.commit();
    }

    private void removeFragment(){

        if(mCurrentFragment != null) {
            mTransaction = mManager.beginTransaction();
            mTransaction.remove(mCurrentFragment);
            mTransaction.commit();

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }else {
//            twitterAuthClient.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void afterLoginAction(){
        Log.w("AuthActivity", "afterLoginAction");
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", "success");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    protected void onTwitterLogin(){
        twitterAuthClient= new TwitterAuthClient();

        twitterAuthClient.authorize(AuthActivity.this, new com.twitter.sdk.android.core.Callback<TwitterSession>() {

            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {
                TwitterSession sessionData = twitterSessionResult.data;
                String uid = String.valueOf(sessionData.getUserId());
                Log.w("WelcomeActivity", "userId: " + uid);
                String username = sessionData.getUserName();
//                Log.w("WelcomeActivity", "username: " + username);
                sessionData.getAuthToken();
//                sessionData.getUserId()

                HttpKit http = new HttpKit(AuthActivity.this);
                String[] params = {"twitter", uid, null};
                http.checkUsername("tw_" + uid, "twitter", params);

//                        TwitterAuthClient authClient = new TwitterAuthClient();
//                        authClient.requestEmail(new TwitterSession(sessionData.getAuthToken(), sessionData.getUserId(), username), new Callback<String>() {
//                            @Override
//                            public void success(Result<String> result) {
//                                // Do something with the result, which provides the email address
//                                Log.w("WelcomeActivity", "Email should be here: "+result.data.toString());
//                            }
//
//                            @Override
//                            public void failure(TwitterException exception) {
//                                // Do something on failure
//                                Log.w("WelcomeActivity", "Email failure: "+exception.toString());
//                            }
//                        });
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });

    }

    // Private method to handle Facebook login and callback
    protected void onFblogin() {
        callbackManager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(AuthActivity.this, Arrays.asList("email", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("Success");
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject json, GraphResponse response) {
                                        if (response.getError() != null) {
                                            // handle error
                                            System.out.println("ERROR");
                                        } else {
                                            System.out.println("Success");
                                            try {

                                                String jsonresult = String.valueOf(json);
                                                System.out.println("JSON Result" + jsonresult);

                                                String str_id = json.getString("id");
                                                String str_email = json.getString("email");
                                                HttpKit http = new HttpKit(AuthActivity.this);

                                                if (mIsRegularAuthorization) {
                                                    String[] params = {"facebook", str_id, str_email};
                                                    http.checkUsername("fb_" + str_id, "facebook", params);
                                                }else{
                                                    String[] params = {"connected_facebook", str_id, str_email, mUsername, mPassword};
                                                    mIsRegularAuthorization = true;
                                                    http.regAndLogin(params);
                                                }
//                                                String str_firstname = json.getString("first_name");
//                                                String str_lastname = json.getString("last_name");


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        Log.d("Cancel", "On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("Error", error.toString());
                    }
                });
    }
}
