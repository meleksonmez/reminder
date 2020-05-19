package com.example.reminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewReminderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DBHelper dbHelper;
    String item = "";
    SharedPreferences settings;
    final String appModeKey = "appModeKey";
    final static String vibrationKey = "vibrationKey";
    final static String ringToneKey = "ringToneKey";
    final static String repeatKey = "repeatKey";
    final static String timeKey = "timeKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);

        dbHelper = new DBHelper(NewReminderActivity.this);

        settings = getSharedPreferences("SQL", 0);

        init();

        final EditText newTitle = (EditText) findViewById(R.id.newTitle_editText);
        final EditText newDetail = (EditText) findViewById(R.id.addNewDetail_editText);
        final TextView newTimeText = (TextView) findViewById(R.id.addNewTime_editText);

        final Button addNewButton = (Button) findViewById(R.id.save_button);
        final Button shareButton = (Button) findViewById(R.id.share_button);

        final Spinner spinner = (Spinner) findViewById(R.id.addCategory_spinner);

        spinner.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("Kategori Seçiniz");
        categories.add(NoteCategory.getBirthday());
        categories.add(NoteCategory.getFamily());
        categories.add(NoteCategory.getMeeting());
        categories.add(NoteCategory.getNote());
        categories.add(NoteCategory.getTask());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        final Calendar newCalendar = Calendar.getInstance();
        final Calendar newDate = Calendar.getInstance();
        newTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(NewReminderActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                        Calendar newTime = Calendar.getInstance();
                        TimePickerDialog time = new TimePickerDialog(NewReminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                newDate.set(year,month,dayOfMonth,hourOfDay,minute,0);
                                Calendar tem = Calendar.getInstance();
                                Log.w("TIME",System.currentTimeMillis()+"");
                                if(newDate.getTimeInMillis()-tem.getTimeInMillis()>0)
                                    newTimeText.setText(ReminderNotes.convertGregorianToDate(newDate));
                                else
                                    Toast.makeText(NewReminderActivity.this,"Invalid time",Toast.LENGTH_SHORT).show();
                            }
                        },newTime.get(Calendar.HOUR_OF_DAY),newTime.get(Calendar.MINUTE),true);
                        time.show();
                    }
                },newCalendar.get(Calendar.YEAR),newCalendar.get(Calendar.MONTH),newCalendar.get(Calendar.DAY_OF_MONTH));

                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();
            }
        });

        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!newTitle.getText().toString().equalsIgnoreCase("") &&
                !newDetail.getText().toString().equalsIgnoreCase("") &&
                !newTimeText.getText().toString().equalsIgnoreCase("") &&
                !(item.equalsIgnoreCase("") || item.equalsIgnoreCase("Kategori Seçiniz"))){
                    ReminderNotes reminderNotes = new ReminderNotes();

                    reminderNotes.setReminderTitle(newTitle.getText().toString());
                    reminderNotes.setReminderDetail(newDetail.getText().toString());
                    reminderNotes.setReminderCategory(item);
                    reminderNotes.setReminderTime(newDate);
                    reminderNotes.setChecked(false);

                    dbHelper.insertReminderNotes(reminderNotes);

                    long[] vibration = {settings.getLong(vibrationKey, 0), 0};
                    String ringTone = settings.getString(ringToneKey, "");

                    String alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();
                    if(ringTone.equalsIgnoreCase("ALARM")){
                        alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();
                    } else if (ringTone.equalsIgnoreCase("NOTIFICATION")){
                        alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString();
                    } else if (ringTone.equalsIgnoreCase("RINGTONE")) {
                        alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE).toString();
                    }

                    Intent intent = new Intent(NewReminderActivity.this,NotifierAlarm.class);
                    intent.putExtra("Title", reminderNotes.getReminderTitle());
                    intent.putExtra("Message", reminderNotes.getReminderDetail());
                    intent.putExtra("RemindDate", reminderNotes.getReminderTime().getTimeInMillis());
                    intent.putExtra("id", reminderNotes.getID());
                    intent.putExtra("Vibration", vibration);
                    intent.putExtra("RingTone", alarmsound);

                    PendingIntent alarmIntent = PendingIntent.getBroadcast(NewReminderActivity.this, reminderNotes.getID(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    int repeatSetting = settings.getInt(repeatKey, 0);
                    long repeat = 15;
                    if(repeatSetting == 15){
                        repeat = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
                    } else if (repeatSetting == 30) {
                        repeat = AlarmManager.INTERVAL_HALF_HOUR;
                    } else if (repeatSetting == 60){
                        repeat = AlarmManager.INTERVAL_HOUR;
                    }

                    int timeSetting = settings.getInt(timeKey, 0);
                    long time = reminderNotes.getReminderTime().getTimeInMillis();
                    if(timeSetting == 5){
                        time = reminderNotes.getReminderTime().getTimeInMillis() - (timeSetting * 60 * 1000);
                    } else if (timeSetting == 10) {
                        time = reminderNotes.getReminderTime().getTimeInMillis() - (timeSetting * 60 * 1000);
                    } else if (timeSetting == 15){
                        time = reminderNotes.getReminderTime().getTimeInMillis() - (timeSetting * 60 * 1000);
                    } else if (timeSetting == 30){
                        time = reminderNotes.getReminderTime().getTimeInMillis() - (timeSetting * 60 * 1000);
                    }

                    AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                    //alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderNotes.getReminderTime().getTimeInMillis(), intent1);
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, time, repeat, alarmIntent);

                    newTitle.setText("");
                    newDetail.setText("");
                    newTimeText.setText("");
                    spinner.setSelection(0);

                    Toast.makeText(NewReminderActivity.this, "Not eklendi.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(NewReminderActivity.this, "Tüm alanları doldurunuz.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!newTitle.getText().toString().equalsIgnoreCase("") &&
                        !newDetail.getText().toString().equalsIgnoreCase("") &&
                        !newTimeText.getText().toString().equalsIgnoreCase("") &&
                        !(item.equalsIgnoreCase("") || item.equalsIgnoreCase("Kategori Seçiniz"))){
                    ReminderNotes reminderNotes = new ReminderNotes();

                    reminderNotes.setReminderTitle(newTitle.getText().toString());
                    reminderNotes.setReminderDetail(newDetail.getText().toString());
                    reminderNotes.setReminderCategory(item);
                    reminderNotes.setReminderTime(newDate);

                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, ReminderNotes.toString(reminderNotes));
                    startActivity(Intent.createChooser(emailIntent,"Sending email..."));
                }
                else{
                    Toast.makeText(NewReminderActivity.this, "Tüm alanları doldurunuz.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void init(){
        final ConstraintLayout newReminderLayout = (ConstraintLayout) findViewById(R.id.newReminder_layout);
        String appMode = settings.getString(appModeKey, "");
        if(appMode.equalsIgnoreCase("ON")) {
            newReminderLayout.setBackgroundColor(Color.DKGRAY);
        }
        else {
            newReminderLayout.setBackgroundColor(Color.rgb(255, 152, 0));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }
}
