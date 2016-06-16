package com.elantix.dopeapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
//import android.support.v7.util.ThreadUtil;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

/**
 * Created by oleh on 6/13/16.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final int MESSAGE_NOTIFICATION_ID = 435345;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        createNotification(remoteMessage.getNotification());
    }

    // Creates notification based on title and body received
    private void createNotification(RemoteMessage.Notification notification) {
        Context context = getBaseContext();

        Intent intent = new Intent(this, MainActivity.class);
        int requestID = (int) System.currentTimeMillis(); //unique requestID to differentiate between various notification with same NotifId
        int flags = PendingIntent.FLAG_CANCEL_CURRENT; // cancel old intent and create new one
        PendingIntent pIntent = PendingIntent.getActivity(this, requestID, intent, flags);

//        Intent kiwiIntent = new Intent(this, ShareDopeActivity.class);
//        PendingIntent sIntent = PendingIntent.getActivity(this, requestID, intent, flags);

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.kiwi)
//                .setContentTitle(notification.getTitle())
                .setContentTitle("DopeApp")
                .setSound(uri)
                .setAutoCancel(true)
                .setContentIntent(pIntent)
//                .addAction(R.drawable.kiwi, "Kiwi", sIntent)
                .setContentText(notification.getBody());
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.cancel(MESSAGE_NOTIFICATION_ID);

        Notification noti = mBuilder.build();
//        noti.flags |= Notification.FLAG_AUTO_CANCEL;



        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, noti);
    }
}
