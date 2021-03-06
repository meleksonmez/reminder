package com.example.reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ArrayList<ReminderNotes> reminderNotesArrayList;
    DBHelper dbHelper;
    SharedPreferences settings;
    boolean firstTime;
    private final int dayMode = 1;
    private final int weekMode = 2;
    private final int monthMode = 3;
    private final int allMode = 4;
    private final String titleCode = "Title";
    private final String listModeCode = "ListMode";
    String title = "";
    final String appModeKey = "appModeKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reminderNotesArrayList = new ArrayList<>();
        dbHelper = new DBHelper(MainActivity.this);
        settings = getSharedPreferences("SQL", 0);
        firstTime = settings.getBoolean("firstTime", true);

        ImageView todayImageView = (ImageView) findViewById(R.id.today_imageView);
        ImageView weekImageView = (ImageView) findViewById(R.id.week_imageView);
        ImageView monthImageView = (ImageView) findViewById(R.id.month_imageView);
        ImageView allImageView = (ImageView) findViewById(R.id.all_imageView);

        Button addNew = (Button) findViewById(R.id.addNew_button);
        Button settingsButton = (Button) findViewById(R.id.settings_button);

        if (firstTime) {
            hasFirstTime();
        }

        init();

        todayImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = "Bugünün Notları";
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra(titleCode, title);
                intent.putExtra(listModeCode, dayMode);
                startActivity(intent);
            }
        });

        weekImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = "Haftalık Notlar";
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra(titleCode, title);
                intent.putExtra(listModeCode, weekMode);
                startActivity(intent);
            }
        });

        monthImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = "Aylık Notlar";
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra(titleCode, title);
                intent.putExtra(listModeCode, monthMode);
                startActivity(intent);
            }
        });

        allImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = "Tüm Notlar";
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra(titleCode, title);
                intent.putExtra(listModeCode, allMode);
                startActivity(intent);
            }
        });

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNew = new Intent(MainActivity.this, NewReminderActivity.class);
                startActivity(addNew);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(MainActivity.this, Settings.class);
                startActivity(settingsIntent);
            }
        });
    }

    private void hasFirstTime() {
        Calendar day1 = Calendar.getInstance();
        Calendar day2 = Calendar.getInstance();
        day1.set(2020, 4, 31, 10, 0);
        day2.set(2020, 5, 20, 10, 0);

        dbHelper.insertReminderNotes(new ReminderNotes("Mobil Proje", "Mobil Programlama Dersi Projesi Teslimi", NoteCategory.getTask(), day1, false));
        dbHelper.insertReminderNotes(new ReminderNotes("Web Proje", "Web Programlama Dersi Projesi Teslimi", NoteCategory.getTask(), day2, false));

        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("firstTime", false);
        editor.apply();
    }

    public void init(){
        try {
            reminderNotesArrayList = dbHelper.getAllReminderNotes();
        } catch (ParseException e) {
            Toast.makeText(MainActivity.this, "Anımsatıcı listesine ulaşılamadı.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        TextView todayCountTextView = (TextView) findViewById(R.id.todayCount_textView);
        TextView weekCountTextView = (TextView) findViewById(R.id.weekCount_textView);
        TextView monthCountTextView = (TextView) findViewById(R.id.monthCount_textView);
        TextView allCountTextView = (TextView) findViewById(R.id.allCount_textView);

        String todayText = "Tamamlanma oranı: " + ReminderNotes.getCheckedRemindersCount(reminderNotesArrayList, dayMode) + "/"
                + ReminderNotes.getRemindersCount(reminderNotesArrayList, dayMode);
        String weekText = "Tamamlanma oranı: " + ReminderNotes.getCheckedRemindersCount(reminderNotesArrayList, weekMode) + "/"
                + ReminderNotes.getRemindersCount(reminderNotesArrayList, weekMode);
        String monthText = "Tamamlanma oranı: " + ReminderNotes.getCheckedRemindersCount(reminderNotesArrayList, monthMode) + "/"
                + ReminderNotes.getRemindersCount(reminderNotesArrayList, monthMode);
        String allText = "Tamamlanma oranı: " + ReminderNotes.getCheckedRemindersCount(reminderNotesArrayList, allMode) + "/"
                + ReminderNotes.getRemindersCount(reminderNotesArrayList, allMode);

        todayCountTextView.setText(todayText);
        weekCountTextView.setText(weekText);
        monthCountTextView.setText(monthText);
        allCountTextView.setText(allText);

        final ConstraintLayout mainLayout = (ConstraintLayout) findViewById(R.id.main_layout);
        String appMode = settings.getString(appModeKey, "");

        if(appMode.equalsIgnoreCase("ON")) {
            mainLayout.setBackgroundColor(Color.DKGRAY);
        }
        else {
            mainLayout.setBackgroundColor(Color.rgb(255, 152, 0));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        init();
    }
}
