package com.elantix.dopeapp;

import android.util.Log;

import com.elantix.dopeapp.Utilities;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by oleh on 6/13/16.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("InstanceIDService", "Refreshed token: " + refreshedToken);
        Utilities.FirebaseCloudToken = refreshedToken;
    }
}
