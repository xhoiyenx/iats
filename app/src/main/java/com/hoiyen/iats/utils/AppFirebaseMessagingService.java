package com.hoiyen.iats.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hoiyen.iats.R;
import com.hoiyen.iats.activities.ChatActivity;


public class AppFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "FCM";

    @Override
    public void onMessageReceived(RemoteMessage message) {

        String notificationSource = message.getData().get("source");
        if (notificationSource != null) {
            switch (notificationSource) {
                case "chat": {
                    Intent intent = new Intent("update-chat");
                    intent.putExtra("model", message.getData().get("model"));
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                }
            }
        }
    }

}
