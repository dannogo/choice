package com.elantix.dopeapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

        SharedPreferences prefs = getSharedPreferences(Utilities.MY_PREFS_NAME, MODE_PRIVATE);
        String restoredToken = prefs.getString("token", null);
        if (restoredToken != null) {
            Utilities.sToken = restoredToken;
            Utilities.sUid = prefs.getString("uid", null);
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
