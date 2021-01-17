package com.example.readapplication;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AppService extends Service {
    public static final String START_FOREGROUND_SERVICE = "START_FOREGROUND_SERVICE";
    public static final String STOP_FOREGROUND_SERVICE = "STOP_FOREGROUND_SERVICE";

    public static int NOTIFICATION_ID = 153;
    private int lastShownNotificationId = -1;
    public static String CHANNEL_ID = "com.example.readapplication.CHANNEL_ID_FOREGROUND";
    public static String MAIN_ACTION = "com.example.readapplication.appService.action.main";

    public static final String BROADCAST_NEW_MASSAGE_DETECTED = "android.provider.Telephony.SMS_RECEIVED";
    public static final String BROADCAST_NEW_CALL_DETECTED = "android.intent.action.PHONE_STATE";

    private NotificationCompat.Builder notificationBuilder;
    private ReceiveSms receiveSms;

    private boolean isServiceRunningRightNow = false;


    public AppService() {
    }

    @Override
    public void onCreate() {
        receiveSms = new ReceiveSms();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_NEW_MASSAGE_DETECTED);
        intentFilter.addAction(BROADCAST_NEW_CALL_DETECTED);
        registerReceiver(receiveSms,intentFilter);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();

        if (intent.getAction().equals(START_FOREGROUND_SERVICE)) {
            if (isServiceRunningRightNow){
                return START_STICKY;
            }

            isServiceRunningRightNow = true;
            notifyToUserForForegroundService();

            return START_STICKY;
        } else if (intent.getAction().equals(STOP_FOREGROUND_SERVICE)) {
            stopForeground(true);
            stopSelf();
            isServiceRunningRightNow = false;
            return START_NOT_STICKY;
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiveSms);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void notifyToUserForForegroundService() {
        // On notification click
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder = getNotificationBuilder(this,
                CHANNEL_ID,
                NotificationManagerCompat.IMPORTANCE_LOW); //Low importance prevent visual appearance for this notification channel on top

        notificationBuilder.setContentIntent(pendingIntent) // Open activity
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_spies)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                .setContentTitle("We see you")
                .setContentText("App in progress")
        ;

        Notification notification = notificationBuilder.build();

        startForeground(NOTIFICATION_ID, notification);

        if (NOTIFICATION_ID != lastShownNotificationId) {
            // Cancel previous notification
            final NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
            notificationManager.cancel(lastShownNotificationId);
        }
        lastShownNotificationId = NOTIFICATION_ID;
    }

    public static NotificationCompat.Builder getNotificationBuilder(Context context, String channelId, int importance) {
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            prepareChannel(context, channelId, importance);
            builder = new NotificationCompat.Builder(context, channelId);
        } else {
            builder = new NotificationCompat.Builder(context);
        }
        return builder;
    }

    @TargetApi(26)
    private static void prepareChannel(Context context, String id, int importance) {
        final String appName = context.getString(R.string.app_name);
        String notifications_channel_description = "Cycling map channel";
        String description = notifications_channel_description;
        final NotificationManager nm = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);

        if(nm != null) {
            NotificationChannel nChannel = nm.getNotificationChannel(id);

            if (nChannel == null) {
                nChannel = new NotificationChannel(id, appName, importance);
                nChannel.setDescription(description);

                // from another answer
                nChannel.enableLights(true);
                nChannel.setLightColor(Color.BLUE);

                nm.createNotificationChannel(nChannel);
            }
        }
    }
}