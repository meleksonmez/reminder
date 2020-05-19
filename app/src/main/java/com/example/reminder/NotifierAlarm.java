package com.example.reminder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Objects;

public class NotifierAlarm extends BroadcastReceiver {

    NotificationManager manager;
    private final int allMode = 4;
    private final String titleCode = "Title";
    private final String listModeCode = "ListMode";

    @Override
    public void onReceive(Context context, Intent intent) {
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationActions(context, intent);
    }

    private void notificationActions(Context context, Intent intent) {
        Intent intent1 = new Intent(context, ListActivity.class);
        intent1.putExtra(titleCode, "Tüm Notlar");
        intent1.putExtra(listModeCode, allMode);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(ListActivity.class);
        taskStackBuilder.addNextIntent(intent1);

        PendingIntent intent2 = taskStackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("my_channel_01", "hello", NotificationManager.IMPORTANCE_HIGH);
        }

        Uri alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (intent.getStringExtra("RingTone") != null) {
            alarmsound = Uri.parse(intent.getStringExtra("RingTone"));
        }
        long[] vibration = intent.getLongArrayExtra("Vibration");

        Notification notification = builder.setContentTitle(intent.getStringExtra("Title"))
                .setContentText(intent.getStringExtra("Message")).setAutoCancel(true)
                .setSound(alarmsound).setSmallIcon(R.drawable.ic_notifications)
                .setContentIntent(intent2)
                .setChannelId("my_channel_01")
                .setVibrate(vibration)
                .addAction(R.drawable.ic_snooze, "Ertele", intent2)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(1, notification);
    }
}
