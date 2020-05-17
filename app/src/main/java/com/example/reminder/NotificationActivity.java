package com.example.reminder;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.view.View;

public class NotificationActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "reminder"; //Channel ID
    private static final String CHANNEL_NAME = "reminderChannel"; //Channel Adı
    private static final int NOTIFICATION_ID = 52; //Notification ID'si
    private NotificationManager manager;
    private String title = "";
    private String detail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void createNotificationChannel(View view) {
        // create android channel
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableVibration(true);
        manager.createNotificationChannel(channel);
    }

    public void sendNotification(View view) {
        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(detail)
                .setSmallIcon(R.drawable.ic_notifications)
                .setAutoCancel(false)
                .build();
        manager.notify(NOTIFICATION_ID, notification);
    }

    public void deleteChannel(View view) {
        manager.deleteNotificationChannel(CHANNEL_ID);
    }
}
