package com.example.group3_sker.Helper;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.group3_sker.Activity.MainActivity;
import com.example.group3_sker.R;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {
    private static final int NOTIFICATION_ID = 1;

    private Timer timer;
    private TimerTask timerTask;
    private final String TAG = "Timers";
    private final int Your_X_SECS = 10;
    private final String CHANNEL_ID = "NotificationChannel";
    private String userId;
    String notificationText;

    @Nullable
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        createNotificationChannel();
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Retrieve userId from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("USER_ID", "User");

        createNotificationChannel();
        startForeground(NOTIFICATION_ID, buildNotification("Service is running", "Your cart notifications are active"));
    }

    private Notification buildNotification(String title, String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.sker_logo_new)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        return builder.build();
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        stoptimertask();
        super.onDestroy();
    }

    final Handler handler = new Handler();

    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 5000, Your_X_SECS * 10000);
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        Log.e(TAG, "Timer task executed");
                        sendNotification();
                    }
                });
            }
        };
    }

    @SuppressLint("NotificationPermission")
    private void sendNotification() {
        ManagementCart managementCart = new ManagementCart(this, userId);
        int totalItemCount = managementCart.getTotalItemCount();

        if(totalItemCount > 0){
             notificationText = "Your cart has " + totalItemCount + " item" + (totalItemCount != 1 ? "s" : "") + " not paid yet!";
        }
        else{
            notificationText = "You are all done!";
        }

        Log.e(TAG, "Sending notification with text: " + notificationText);
        Notification notification = buildNotification("Cart Reminder", notificationText);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, notification);
        } else {
            Log.e(TAG, "NotificationManager is null, cannot send notification");
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for cart notifications");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            } else {
                Log.e(TAG, "NotificationManager is null");
            }
        }
    }
}
