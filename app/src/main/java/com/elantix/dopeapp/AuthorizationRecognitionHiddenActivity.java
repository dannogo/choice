package com.elantix.dopeapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.elantix.dopeapp.services.MyFirebaseInstanceIDService;

/**
 * Created by oleh on 4/19/16.
 */
public class AuthorizationRecognitionHiddenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.elantix.dopeapp",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }

//        Utilities.ANDROID_ID = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                MyFirebaseInstanceIDService service = new MyFirebaseInstanceIDService();
                service.onTokenRefresh();
            }
        });

        SharedPreferences prefs = getSharedPreferences(Utilities.MY_PREFS_NAME, MODE_PRIVATE);
        String restoredToken = prefs.getString("token", null);
        if (restoredToken != null) {
            Utilities.sToken = restoredToken;
            Utilities.sUid = prefs.getString("uid", null);

            if (Utilities.sMyProfile == null){
                Utilities.sMyProfile = new ProfileInfo();
            }
            Utilities.sMyProfile.id = Utilities.sUid;
            Utilities.sMyProfile.avatar = prefs.getString("avatar", "");
            Utilities.sMyProfile.username = prefs.getString("username", "");
            Utilities.sMyProfile.fullname = prefs.getString("fullname", "");
            Utilities.sMyProfile.email = prefs.getString("email", "");

            Intent intent = new Intent(AuthorizationRecognitionHiddenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(AuthorizationRecognitionHiddenActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }


    }
}
