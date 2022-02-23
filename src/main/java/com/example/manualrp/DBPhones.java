package com.example.manualrp;

import android.util.Log;

public class DBPhones {
    //обязательно public!!!
    public String numberBs;
    public String nameBs;
    public String fio;
    public String fio1;
    public String fio2;
    public String fio3;
    public String phoneNumber;
    public String phoneNumber1;
    public String phoneNumber2;
    public String phoneNumber3;
    public String comment;

    // для Firebase нужно создавать два конструктора!?
    public DBPhones() {

    }

    // конструктор класса
    public DBPhones(String numberBs, String nameBs, String fio, String fio1,
                    String fio2, String fio3, String phoneNumber, String phoneNumber1,
                    String phoneNumber2, String phoneNumber3, String comment) {
        this.numberBs = numberBs;
        this.nameBs = nameBs;
        this.fio = fio;
        this.fio1 = fio1;
        this.fio2 = fio2;
        this.fio3 = fio3;
        this.phoneNumber = phoneNumber;
        this.phoneNumber1 = phoneNumber1;
        this.phoneNumber2 = phoneNumber2;
        this.phoneNumber3 = phoneNumber3;
        this.comment = comment;

        Log.d("myLogs", "numberBs = " + numberBs);
        Log.d("myLogs", "nameBs = " + nameBs);
        Log.d("myLogs", "fio = " + fio);
        Log.d("myLogs", "phoneNumber = " + phoneNumber);
        Log.d("myLogs", "comment = " + comment);
    }
}


