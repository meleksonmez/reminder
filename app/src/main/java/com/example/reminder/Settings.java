package com.example.reminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Settings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    SharedPreferences settings;
    String itemRepeat = "";
    String itemTime = "";
    String itemRingTone = "";
    int itemRingTonePos = 0;
    int itemTimePos = 0;
    int itemRepeatPos = 0;

    final static String appModeKey = "appModeKey";
    final static String vibrationKey = "vibrationKey";
    final static String repeatKey = "repeatKey";
    final static String ringToneKey = "ringToneKey";
    final static String timeKey = "timeKey";

    Spinner spinnerRingTone;
    Spinner spinnerRepeat;
    Spinner spinnerAlarmTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settings = getSharedPreferences("SQL", 0);

        ////////// Zil sesi /////////
        spinnerRingTone = (Spinner) findViewById(R.id.zilSesi_spinner);
        spinnerRingTone.setOnItemSelectedListener(this);

        List<String> ringtone = new ArrayList<String>();
        ringtone.add("Zil Sesi Seçiniz");
        ringtone.add(NoteCategory.getBirthday());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ringtone);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRingTone.setAdapter(dataAdapter);

        ///////// Hatirlatma Sıklığı //////////
        spinnerRepeat = (Spinner) findViewById(R.id.hatirlatmaTekrari_spinner);
        spinnerRepeat.setOnItemSelectedListener(this);

        List<String> repeat = new ArrayList<String>();
        repeat.add("Hatırlatma Sıklığı Seçiniz");
        repeat.add("5");
        repeat.add("10");
        repeat.add("15");
        repeat.add("30");

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, repeat);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRepeat.setAdapter(dataAdapter);

        ///////// Hatirlatma Zamani //////////
        spinnerAlarmTime = (Spinner) findViewById(R.id.hatirlatmaZamani_spinner);
        spinnerAlarmTime.setOnItemSelectedListener(this);

        List<String> alarmTime = new ArrayList<String>();
        alarmTime.add("Hatırlatma Sıklığı Seçiniz");
        alarmTime.add("5");
        alarmTime.add("10");
        alarmTime.add("15");
        alarmTime.add("30");

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, alarmTime);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlarmTime.setAdapter(dataAdapter);

        ////////////////// APP Mode ////////////////////////////
        final ConstraintLayout settingsLayout = (ConstraintLayout) findViewById(R.id.settings_layout);

        final Switch appMode = (Switch) findViewById(R.id.appMode_switch);
        final Button save = (Button) findViewById(R.id.settingsSave_button);
        final CheckBox vibration = (CheckBox) findViewById(R.id.titresim_checkBox);

        if(settings.getString(appModeKey,"").equalsIgnoreCase("OFF")) {
            settingsLayout.setBackgroundColor(Color.rgb(255, 152, 0));
            appMode.setChecked(false);
        }
        else {
            settingsLayout.setBackgroundColor(Color.DKGRAY);
            appMode.setChecked(true);
        }

        appMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appMode.isChecked()) {
                    settingsLayout.setBackgroundColor(Color.DKGRAY);
                }
                else {
                    settingsLayout.setBackgroundColor(Color.rgb(255, 152, 0));
                }
            }
        });

        if(settings.getString(vibrationKey,"").equalsIgnoreCase("True")) {
            vibration.setChecked(true);
        }
        else {
            vibration.setChecked(false);
        }

        if(settings.getInt(ringToneKey, 0) != 0){
            spinnerRingTone.setSelection(settings.getInt(ringToneKey, 0));
        }

        if(settings.getInt(timeKey, 0) != 0){
            spinnerAlarmTime.setSelection(settings.getInt(timeKey, 0));
        }

        if(settings.getInt(repeatKey, 0) != 0){
            spinnerRepeat.setSelection(settings.getInt(repeatKey, 0));
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = settings.edit();

                String switchValue = appMode.isChecked() ? "ON" : "OFF";
                String vib = vibration.isChecked() ? "true" : "false";

                editor.putInt(ringToneKey, itemRingTonePos);
                editor.putInt(repeatKey, itemRepeatPos);
                editor.putInt(timeKey, itemTimePos);
                editor.putString(vibrationKey, vib);
                editor.putString(appModeKey, switchValue);

                editor.commit();

                Toast.makeText(Settings.this, "Saved", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(view == spinnerRingTone){
            itemRingTone = parent.getItemAtPosition(position).toString();
            itemRingTonePos = position;
        }

        if(view == spinnerAlarmTime){
            itemTime = parent.getItemAtPosition(position).toString();
            itemTimePos = position;
        }

        if(view == spinnerRepeat){
            itemRepeat = parent.getItemAtPosition(position).toString();
            itemRepeatPos = position;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
