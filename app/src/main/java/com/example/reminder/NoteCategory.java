package com.example.reminder;

import android.graphics.Color;

public class NoteCategory {

    private static final String birthday = "Doğum Günü";
    private static final String task = "Görev";
    private static final String meeting = "Toplantı";
    private static final String family = "Aile";
    private static final String note = "Not";

    public static String getBirthday() {
        return birthday;
    }

    public static String getFamily() {
        return family;
    }

    public static String getMeeting() {
        return meeting;
    }

    public static String getNote() {
        return note;
    }

    public static String getTask() {
        return task;
    }

    public static int getColour(String category){
        switch (category){
            case birthday : return Color.rgb(145, 100, 100);
            case task : return Color.rgb(120,120,200);
            case meeting : return Color.YELLOW;
            case family : return Color.MAGENTA;
            case note : return Color.LTGRAY;
            default: return Color.WHITE;
        }
    }
}
