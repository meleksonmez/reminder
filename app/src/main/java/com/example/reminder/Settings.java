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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Settings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    SharedPreferences settings;
    String itemRingTone = "";
    int itemRingTonePos = 0;

    final static String appModeKey = "appModeKey";
    final static String vibrationKey = "vibrationKey";
    final static String repeatKey = "repeatKey";
    final static String ringToneKey = "ringToneKey";
    final static String timeKey = "timeKey";

    Spinner spinnerRingTone;

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

        final EditText repeatEditText = (EditText) findViewById(R.id.hatirlatmaTekrari_editText);
        final EditText alarmTimeEditText = (EditText) findViewById(R.id.hatirlatmaZamani_editText);
        final CheckBox vibration = (CheckBox) findViewById(R.id.titresim_checkBox);

        ////////////////// APP Mode ////////////////////////////
        final ConstraintLayout settingsLayout = (ConstraintLayout) findViewById(R.id.settings_layout);

        final Switch appMode = (Switch) findViewById(R.id.appMode_switch);
        final Button save = (Button) findViewById(R.id.settingsSave_button);

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

        if(!settings.getString(timeKey, "").equalsIgnoreCase("")){
            repeatEditText.setText(settings.getString(timeKey, ""));
        }

        if(!settings.getString(repeatKey, "").equalsIgnoreCase("")){
            alarmTimeEditText.setText(settings.getString(repeatKey, ""));
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = settings.edit();

                String switchValue = appMode.isChecked() ? "ON" : "OFF";
                String vib = vibration.isChecked() ? "true" : "false";

                editor.putInt(ringToneKey, itemRingTonePos);
                editor.putString(repeatKey, repeatEditText.getText().toString());
                editor.putString(timeKey, alarmTimeEditText.getText().toString());
                editor.putString(vibrationKey, vib);
                editor.putString(appModeKey, switchValue);

                editor.commit();

                Toast.makeText(Settings.this, "Saved", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        itemRingTone = parent.getItemAtPosition(position).toString();
        itemRingTonePos = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
