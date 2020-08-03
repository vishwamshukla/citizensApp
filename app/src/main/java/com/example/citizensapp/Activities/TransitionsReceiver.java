package com.example.citizensapp.Activities;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.citizensapp.Core.ApplicationClass;
import com.example.citizensapp.R;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.gson.Gson;


import java.util.Calendar;



public class TransitionsReceiver extends IntentService {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Intent mainIntent;
    private PendingIntent pendingIntent;
    private NotificationCompat.Builder builder;
    private NotificationManagerCompat notificationManagerCompat;
    private long timer;
    private long minutesWasted;
    private final long NOTIFICATION_TIME_THRESHOLD = 60 * 1000 * 60;

    public TransitionsReceiver() {
        super("TransitionsReceiver");
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onHandleIntent(Intent intent) {
        //setting up notification system
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationClass.getInstance());
        editor = sharedPreferences.edit();
        timer = sharedPreferences.getLong("timer", Calendar.getInstance().getTimeInMillis());
        editor.putLong("timer", timer).commit();
        createNotificationChannel();
        mainIntent = new Intent(this, MainActivity.class);
        mainIntent.putExtra("inCar", true);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0,
                mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder = new NotificationCompat.Builder(this,
                getString(R.string.notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Are you in a car?")
                .setContentText("Tap to start logging potholes.")
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        notificationManagerCompat = NotificationManagerCompat.from(this);
        long currentTime = Calendar.getInstance().getTimeInMillis();
        timer = currentTime - timer;
        // Log.d("timer", timer + "");
        //check if intent contains data about an activity
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            DetectedActivity detectedActivity = result.getMostProbableActivity();
            // Log.d(getClass().getSimpleName(), detectedActivity.toString());
            //committing current activity to shared prefs
            if (detectedActivity != null) {
                editor.putString("currentActivity", new Gson().toJson(detectedActivity).toString()).commit();
            }
            if (detectedActivity.toString().contains("VEHICLE") && !ApplicationClass.getInstance().isTripInProgress()
                    && timer >= NOTIFICATION_TIME_THRESHOLD && detectedActivity.getConfidence() >= 30) {
                //sending notification to user
                notificationManagerCompat.notify(0, builder.build());
                editor.remove("timer").commit();
            } else {
                if (notificationManagerCompat != null) {
                    notificationManagerCompat.cancel(0);
                }
            }
        }
    }

    private void createNotificationChannel() {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(getString(R.string.notification_channel_id),
                    "TransitionsReceiver", importance);
            channel.setDescription("TransitionsReceiver");
            //registering channel with system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
