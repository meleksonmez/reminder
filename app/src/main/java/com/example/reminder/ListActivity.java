package com.example.reminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    DBHelper dbHelper;
    private final String titleCode = "Title";
    private final String listModeCode = "ListMode";
    private String title = "";
    private int listMode = 4;
    SharedPreferences settings;
    final String appModeKey = "appModeKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        final ConstraintLayout listLayout = (ConstraintLayout) findViewById(R.id.list_layout);

        settings = getSharedPreferences("SQL", 0);

        String appMode = settings.getString(appModeKey, "");
        if(appMode.equalsIgnoreCase("ON")) {
            listLayout.setBackgroundColor(Color.DKGRAY);
        }
        else {
            listLayout.setBackgroundColor(Color.WHITE);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.reminderNotes_recyclerView);
        TextView titleTextView = (TextView) findViewById(R.id.listType_textView);

        ArrayList<ReminderNotes> reminderNotes = new ArrayList<>();

        dbHelper = new DBHelper(ListActivity.this);

        try {
            reminderNotes = dbHelper.getAllReminderNotes();
        } catch (ParseException e) {
            Toast.makeText(ListActivity.this, "Anımsatıcı listesine ulaşılamadı.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        if (getIntent().getStringExtra(titleCode) != null) {
            title = getIntent().getStringExtra(titleCode);
            titleTextView.setText(title);
        }

        listMode = getIntent().getIntExtra(listModeCode, 4);

        reminderNotes = ReminderNotes.getRemindersWithType(reminderNotes, listMode);

        ReminderListViewAdapter reminderListViewAdapter = new ReminderListViewAdapter(this, reminderNotes);
        recyclerView.setAdapter(reminderListViewAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}
