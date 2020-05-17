package com.example.reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

public class Settings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    SharedPreferences settings;
    String item = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settings = getSharedPreferences("SQL", 0);

        ////////// Zil sesi /////////
        final Spinner spinnerRingTone = (Spinner) findViewById(R.id.zilSesi_spinner);
        spinnerRingTone.setOnItemSelectedListener(this);

        List<String> ringtone = new ArrayList<String>();
        ringtone.add("Zil Sesi Seçiniz");
        ringtone.add(NoteCategory.getBirthday());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ringtone);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRingTone.setAdapter(dataAdapter);

        ///////// Hatirlatma Sıklığı //////////
        final Spinner spinnerRepeat = (Spinner) findViewById(R.id.hatirlatmaTekrari_spinner);
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
        final Spinner spinnerAlarmTime = (Spinner) findViewById(R.id.hatirlatmaZamani_spinner);
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

        //////// AppMode //////////
        final Switch appMode = (Switch) findViewById(R.id.appMode_switch);




        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("firstTime", false);
        editor.apply();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
