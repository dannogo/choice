package com.elantix.dopeapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.twitter.sdk.android.core.Callback;
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

import java.net.HttpURLConnection;
import java.util.Arrays;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
//    private static final String TWITTER_KEY = "0O7XQmGoUBfxKlIDKX16vCwYP";
//    private static final String TWITTER_SECRET = "kh6y9zIDTqBfWZ6bNSwiLqC2pE9Rm8Ci9rYa4dqDuPZuOBxCsx";

    private static final String TWITTER_KEY = "iczt8Wb15b2V0IJPq3JdnCQ1p";
    private static final String TWITTER_SECRET = "5cPrXX8qkZgQGPJr0YGE8KOzkQTwxbpaP6b2mQFDUtMIsoZIDn";

//    private static final String TWITTER_KEY = "709348373665603584-OjrqfRi1m004RsVH5vaG4mA4cu9PVUr";
//    private static final String TWITTER_SECRET = "KvI8nf97XZFz6UW5nHLiZtVwpTEpzleBnvZPnbdS4Uepy";

    CallbackManager callbackManager;
    private FancyButton facebookButton;
    FancyButton twitterButton;
    TwitterAuthClient twitterAuthClient;
    private TextView mSkipLogin;
    private Button mSignInNowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig));
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_welcome);

        mSkipLogin = (TextView) findViewById(R.id.skipLogin);
        mSkipLogin.setOnClickListener(this);
//        getSupportActionBar().hide();

        mSignInNowButton = (Button) findViewById(R.id.signInNowButton);
        mSignInNowButton.setOnClickListener(this);

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
                        TwitterSession sessionData = twitterSessionResult.data;
                        String uid = String.valueOf(sessionData.getUserId());
//                        Log.w("WelcomeActivity", "userId: " + uid);
                        String username = sessionData.getUserName();
//                        Log.w("WelcomeActivity", "username: " + username);
                        sessionData.getAuthToken();
//                        sessionData.getUserId()

                        TwitterAuthClient authClient = new TwitterAuthClient();
                        authClient.requestEmail(new TwitterSession(sessionData.getAuthToken(), sessionData.getUserId(), username), new Callback<String>() {
                            @Override
                            public void success(Result<String> result) {
                                // Do something with the result, which provides the email address
                                Log.w("WelcomeActivity", "Email should be here: "+result.data.toString());
                            }

                            @Override
                            public void failure(TwitterException exception) {
                                // Do something on failure
                                Log.w("WelcomeActivity", "Email failure: "+exception.toString());
                            }
                        });
                    }

                    @Override
                    public void failure(TwitterException e) {
                        e.printStackTrace();
                    }
                });


            }
        });


        buttonsAppearanceHandling();


    }

    public void afterLoginAction(){
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void buttonsAppearanceHandling(){

        int iconRightMargin = (int) getResources().getDimension(R.dimen.fancy_button_icon_margin_right);
        int iconRegularSize = (int) getResources().getDimension(R.dimen.fancy_button_regular_icon_size);
        int iconRegularSize2 = (int) getResources().getDimension(R.dimen.fancy_button_regular_icon_size_2);

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(iconRegularSize2, iconRegularSize2);
        lp1.setMargins(0, 0, iconRightMargin, 0);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(iconRegularSize, iconRegularSize);
        lp2.setMargins(0, 0, iconRightMargin, 0);

        facebookButton.getIconImageObject().setLayoutParams(lp1);
        twitterButton.getIconImageObject().setLayoutParams(lp2);
    }

    // Private method to handle Facebook login and callback
    private void onFblogin()
    {
        callbackManager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(WelcomeActivity.this, Arrays.asList("email", "public_profile"));

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
                                                HttpKit http = new HttpKit(WelcomeActivity.this);
                                                String[] params = {"facebook", str_id, str_email};
                                                http.regAndLogin(params);

//                                                String str_firstname = json.getString("first_name");
//                                                String str_lastname = json.getString("last_name");


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                });
//                                .executeAsync();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }else {
            twitterAuthClient.onActivityResult(requestCode, resultCode, data);
        }
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mSkipLogin.getId()){
            Intent intent = new Intent(WelcomeActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
        }else if (id == mSignInNowButton.getId()){
            Intent intent = new Intent(WelcomeActivity.this, SignInActivity.class);
            WelcomeActivity.this.startActivity(intent);
        }
    }
}
