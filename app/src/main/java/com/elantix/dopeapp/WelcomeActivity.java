package com.elantix.dopeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import mehdi.sakout.fancybuttons.FancyButton;

import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class WelcomeActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "0O7XQmGoUBfxKlIDKX16vCwYP";
    private static final String TWITTER_SECRET = "kh6y9zIDTqBfWZ6bNSwiLqC2pE9Rm8Ci9rYa4dqDuPZuOBxCsx";

    CallbackManager callbackManager;
//    private TwitterLoginButton loginButton;
    private FancyButton facebookButton;
    FancyButton twitterButton;
    TwitterAuthClient twitterAuthClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig));

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_welcome);
//        getSupportActionBar().hide();

        Button signInNowButton = (Button) findViewById(R.id.signInNowButton);
        signInNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, SignInActivity.class);
                WelcomeActivity.this.startActivity(intent);
            }
        });

        facebookButton = (FancyButton) findViewById(R.id.facebook_login_button);
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFblogin();
            }
        });


        twitterAuthClient= new TwitterAuthClient();

        twitterButton = (FancyButton) findViewById(R.id.twitter_login_button);
        twitterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                twitterAuthClient.authorize(WelcomeActivity.this, new com.twitter.sdk.android.core.Callback<TwitterSession>() {

                    @Override
                    public void success(Result<TwitterSession> twitterSessionResult) {
                        // Success
                    }

                    @Override
                    public void failure(TwitterException e) {
                        e.printStackTrace();
                    }
                });


            }
        });
//        buttonsAppearenceHandling();


    }

    private void buttonsAppearenceHandling(){
        android.view.ViewGroup.LayoutParams params = facebookButton.getLayoutParams();
        int height = params.height;

        facebookButton.getIconImageObject().setLayoutParams(new LinearLayout.LayoutParams(80, 80));
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        float n = getResources().getDimension(R.dimen.friends_profile_message_margin_4);
        lp1.setMargins(0, (int)n, 0, 0);
        facebookButton.setLayoutParams(lp1);

        twitterButton.getIconImageObject().setLayoutParams(new LinearLayout.LayoutParams(80, 80));
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        n = getResources().getDimension(R.dimen.friends_profile_message_margin_5);
        lp2.setMargins(0, (int) n, 0, 0);
        twitterButton.setLayoutParams(lp2);


    }

    // Private method to handle Facebook login and callback
    private void onFblogin()
    {
        callbackManager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("Success");
                        GraphRequest.newMeRequest(
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

                                                String str_email = json.getString("email");
                                                String str_id = json.getString("id");
                                                String str_firstname = json.getString("first_name");
                                                String str_lastname = json.getString("last_name");

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                }).executeAsync();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        twitterAuthClient.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
}
