package com.elantix.dopeapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.elantix.dopeapp.R;
//import com.google.android.gms.gcm.GcmPubSub;
//import com.google.android.gms.gcm.GoogleCloudMessaging;
//import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by oleh on 6/9/16.
 */
public class RegistrationService extends IntentService {
    public RegistrationService() {
        super("RegistrationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        try {
//            InstanceID myID = InstanceID.getInstance(this);
//            String registrationToken = myID.getToken(
//                    getString(R.string.gcm_defaultSenderId),
//                    GoogleCloudMessaging.INSTANCE_ID_SCOPE,
//                    null
//            );
//            Log.d("Registration Token", registrationToken);
//            GcmPubSub subscription = GcmPubSub.getInstance(this);
////            subscription.subscribe(registrationToken, "/topics/my_little_topic", null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}