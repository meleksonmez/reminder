package com.example.reminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "reminderDB";

    private static final String TABLE_REMINDERS = "reminders";

    private List<String> sql = new ArrayList<>();

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        sql.add("CREATE TABLE " + TABLE_REMINDERS + "(id INTEGER PRIMARY KEY AUTOINCREMENT, reminderTitle TEXT, reminderDetail TEXT, reminderCategory TEXT, reminderTime LONG, checked INTEGER" + ")");

        for (String sqlCommand : sql) {
            Log.d("DBHelper", "SQL : " + sqlCommand);
            db.execSQL(sqlCommand);
        }
    }

    public void insertReminderNotes(ReminderNotes reminderNotes) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("reminderTitle", reminderNotes.getReminderTitle());
        values.put("reminderDetail", reminderNotes.getReminderDetail());
        values.put("reminderCategory", reminderNotes.getReminderCategory());
        values.put("reminderTime", reminderNotes.getReminderTime().getTimeInMillis());
        values.put("checked", reminderNotes.isChecked() ? 1 : 0);

        db.insert(TABLE_REMINDERS, null, values);
        db.close();
    }

    public void updateReminderNote(ReminderNotes reminderNotes) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("reminderTitle", reminderNotes.getReminderTitle());
        values.put("reminderDetail", reminderNotes.getReminderDetail());
        values.put("reminderCategory", reminderNotes.getReminderCategory());
        values.put("reminderTime", reminderNotes.getReminderTime().getTimeInMillis());

        // updating row
        db.update(TABLE_REMINDERS, values," id = ?",
                new String[] { String.valueOf(reminderNotes.getID()) });

        db.close();
    }

    public void isCheckedReminderNote(ReminderNotes reminderNotes) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("checked", reminderNotes.isChecked() ? 1 : 0);

        // updating row
        db.update(TABLE_REMINDERS, values," id = ?",
                new String[] { String.valueOf(reminderNotes.getID()) });

        db.close();
    }

    public void deleteReminderNote(ReminderNotes reminderNotes) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_REMINDERS,"id = ?",
                new String[] { String.valueOf(reminderNotes.getID()) });

        db.close();
    }


    public ArrayList<ReminderNotes> getAllReminderNotes() throws ParseException {
        ArrayList<ReminderNotes> reminderNotes = new ArrayList<ReminderNotes>();
        SQLiteDatabase db = this.getWritableDatabase();

        // String sqlQuery = "SELECT  * FROM " + TABLE_REMINDERS;
        // Cursor cursor = db.rawQuery(sqlQuery, null);

        Cursor cursor = db.query(TABLE_REMINDERS, new String[]{"id", "reminderTitle", "reminderDetail", "reminderCategory", "reminderTime", "checked"},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            ReminderNotes reminderNote = new ReminderNotes();

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(cursor.getLong(4));

            boolean isChecked = false;
            if(cursor.getInt(5) == 1){
                isChecked = true;
            }

            reminderNote.setID(cursor.getInt(0));
            reminderNote.setReminderTitle(cursor.getString(1));
            reminderNote.setReminderDetail(cursor.getString(2));
            reminderNote.setReminderCategory(cursor.getString(3));
            reminderNote.setReminderTime(cal);
            reminderNote.setChecked(isChecked);
            reminderNotes.add(reminderNote);
        }

        return reminderNotes;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
        onCreate(db);
    }
}