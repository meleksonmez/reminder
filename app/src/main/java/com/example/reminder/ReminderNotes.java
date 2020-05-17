package com.example.reminder;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ReminderNotes {
    private int ID;
    private String reminderTitle;
    private String reminderDetail;
    private String reminderCategory;
    private Calendar reminderTime;
    private boolean checked;

    public ReminderNotes(String reminderTitle, String reminderDetail, String reminderCategory, Calendar reminderTime, boolean checked){
        this.reminderTitle = reminderTitle;
        this.reminderDetail = reminderDetail;
        this.reminderTime = reminderTime;
        this.reminderCategory = reminderCategory;
        this.checked = checked;
    }

    public ReminderNotes(){}

    public String getReminderCategory() {
        return reminderCategory;
    }

    public void setReminderCategory(String reminderCategory) {
        this.reminderCategory = reminderCategory;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public boolean isChecked() {
        return checked;
    }

    public Calendar getReminderTime() {
        return reminderTime;
    }

    public String getReminderDetail() {
        return reminderDetail;
    }

    public String getReminderTitle() {
        return reminderTitle;
    }

    public void setReminderDetail(String reminderDetail) {
        this.reminderDetail = reminderDetail;
    }

    public void setReminderTitle(String reminderTitle) {
        this.reminderTitle = reminderTitle;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setReminderTime(Calendar reminderTime) {
        this.reminderTime = reminderTime;
    }

    public static ArrayList<ReminderNotes> getRemindersWithType(ArrayList<ReminderNotes> reminderNotes, int type){
        ArrayList<ReminderNotes> reminderNotesNew = new ArrayList<>();
        Calendar today = Calendar.getInstance();
        double dayConverter = 24 * 60 * 60 * 1000;
        double diff;

        switch (type){
            case 1 : //today's reminders
                for(ReminderNotes reminderNote : reminderNotes){
                    diff = (reminderNote.getReminderTime().getTimeInMillis() - today.getTimeInMillis()) / dayConverter;
                    if(diff >= 0 && diff < 1){
                        reminderNotesNew.add(reminderNote);
                    }
                }
                return reminderNotesNew;
            case 2: //week's reminders
                for(ReminderNotes reminderNote : reminderNotes){
                    diff = (reminderNote.getReminderTime().getTimeInMillis() - today.getTimeInMillis()) / dayConverter;
                    if(diff >= 0 && diff < 7){
                        reminderNotesNew.add(reminderNote);
                    }
                }
                return reminderNotesNew;
            case 3: //month's reminders
                for(ReminderNotes reminderNote : reminderNotes){
                    diff = (reminderNote.getReminderTime().getTimeInMillis() - today.getTimeInMillis()) / dayConverter;
                    if(diff >= 0 && diff < 30){
                        reminderNotesNew.add(reminderNote);
                    }
                }
                return reminderNotesNew;
        }
        return reminderNotes;
    }

    public static int getRemindersCount(ArrayList<ReminderNotes> reminderNotes, int type){
        ArrayList<ReminderNotes> reminderNotesNew = new ArrayList<>();
        Calendar today = Calendar.getInstance();
        double dayConverter = 24 * 60 * 60 * 1000;
        double diff;

        switch (type){
            case 1 : //today's reminders
                for(ReminderNotes reminderNote : reminderNotes){
                    diff = (reminderNote.getReminderTime().getTimeInMillis() - today.getTimeInMillis()) / dayConverter;
                    if(diff >= 0 && diff < 1){
                        reminderNotesNew.add(reminderNote);
                    }
                }
                return reminderNotesNew.size();
            case 2: //week's reminders
                for(ReminderNotes reminderNote : reminderNotes){
                    diff = (reminderNote.getReminderTime().getTimeInMillis() - today.getTimeInMillis()) / dayConverter;
                    if(diff >= 0 && diff < 7){
                        reminderNotesNew.add(reminderNote);
                    }
                }
                return reminderNotesNew.size();
            case 3: //month's reminders
                for(ReminderNotes reminderNote : reminderNotes){
                    diff = (reminderNote.getReminderTime().getTimeInMillis() - today.getTimeInMillis()) / dayConverter;
                    if(diff >= 0 && diff < 30){
                        reminderNotesNew.add(reminderNote);
                    }
                }
                return reminderNotesNew.size();
            default: reminderNotes.size();
        }
        return reminderNotes.size();
    }

    public static String convertGregorianToDate(Calendar reminderTime){
        SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        fmt.setCalendar(reminderTime);

        String dateFormatted = fmt.format(reminderTime.getTime());

        return dateFormatted;
    }

    public static String toString(ReminderNotes reminderNotes) {
        String text = "";
        text += "Not Başlığı: " + reminderNotes.getReminderTitle() + "\n";
        text += "Not Detayı: " + reminderNotes.getReminderDetail() + "\n";
        text += "Not Kategorisi: " + reminderNotes.getReminderCategory() + "\n";
        text += "Not Gerçekleşme Zamanı: " + ReminderNotes.convertGregorianToDate(reminderNotes.getReminderTime()) + "\n";
        return text;
    }
}
