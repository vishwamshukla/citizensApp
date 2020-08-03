package com.example.citizensapp;

import android.content.Intent;
import android.net.Uri;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;



public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private static final String UPDATE_PAYLOAD = "update_request";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String payload = remoteMessage.getData().get("action");
        // Log.d(TAG, "Notification Message Body: " + payload);
        if(payload.equals(UPDATE_PAYLOAD)){
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
    }
}