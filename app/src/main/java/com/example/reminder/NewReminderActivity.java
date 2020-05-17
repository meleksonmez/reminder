package com.example.reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewReminderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DBHelper dbHelper;
    String item = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);

        dbHelper = new DBHelper(NewReminderActivity.this);

        final EditText newTitle = (EditText) findViewById(R.id.newTitle_editText);
        final EditText newDetail = (EditText) findViewById(R.id.addNewDetail_editText);
        final EditText newTime = (EditText) findViewById(R.id.addNewTime_editText);

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

        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!newTitle.getText().toString().equalsIgnoreCase("") &&
                !newDetail.getText().toString().equalsIgnoreCase("") &&
                !newTime.getText().toString().equalsIgnoreCase("") &&
                !(item.equalsIgnoreCase("") || item.equalsIgnoreCase("Kategori Seçiniz"))){
                    ReminderNotes reminderNotes = new ReminderNotes();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(newTime.getDrawingTime());

                    reminderNotes.setReminderTitle(newTitle.getText().toString());
                    reminderNotes.setReminderDetail(newDetail.getText().toString());
                    reminderNotes.setReminderCategory(item);
                    reminderNotes.setReminderTime(calendar);
                    reminderNotes.setChecked(false);

                    dbHelper.insertReminderNotes(reminderNotes);

                    Toast.makeText(NewReminderActivity.this, "Not eklendi.", Toast.LENGTH_SHORT).show();
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

}
