package com.example.manualrp;

import android.util.Log;

public class DBForm {
    public String comment;
    public String date;
    public String category;
    public boolean checkText;
    public String userName;

    // для Firebase нужно создавать два конструктора!?
    public DBForm() {

    }

    // конструктор класса

    public DBForm(String date, String comment, Boolean checkCrossOutText, String name, String dropList) {
    //public DBForm(String date, String comment, String name, String dropList) {
        this.comment = comment;
        this.date = date;
        this.checkText = checkCrossOutText;
        this.userName = name;
        this.category = dropList;

        Log.d("myLogs", "--Class DBForm start--");
        Log.d("myLogs", "comment = " + comment);
        Log.d("myLogs", "checkCrossOutText = " + checkCrossOutText);
        Log.d("myLogs", "userName = " + name);
        Log.d("myLogs", "category = " + dropList);
    }
}


