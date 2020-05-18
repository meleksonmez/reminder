package com.example.reminder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class NotifierAlarm extends BroadcastReceiver {

    private DBHelper dbHelper;

    @Override
    public void onReceive(Context context, Intent intent) {

        dbHelper = new DBHelper(context.getApplicationContext());
        ReminderNotes reminder = new ReminderNotes();
        /*Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(intent.getLongExtra("RemindDate", calendar.getTimeInMillis()));

        reminder.setReminderDetail(intent.getStringExtra("Title"));
        reminder.setReminderDetail(intent.getStringExtra("Message"));
        reminder.setReminderTime(calendar);*/
        reminder.setID(intent.getIntExtra("id",0));
        reminder.setChecked(true);

        dbHelper.isCheckedReminderNote(reminder);

        Intent intent1 = new Intent(context,MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(intent1);

        PendingIntent intent2 = taskStackBuilder.getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("my_channel_01","hello", NotificationManager.IMPORTANCE_HIGH);
        }

        Uri alarmsound = Uri.parse(intent.getStringExtra("RingTone"));
        long[] vibration = intent.getLongArrayExtra("Vibration");

        Notification notification = builder.setContentTitle(intent.getStringExtra("Title"))
                .setContentText(intent.getStringExtra("Message")).setAutoCancel(false)
                .setSound(alarmsound).setSmallIcon(R.drawable.ic_notifications)
                .setContentIntent(intent2)
                .setChannelId("my_channel_01")
                .setVibrate(vibration)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(1, notification);

    }
}