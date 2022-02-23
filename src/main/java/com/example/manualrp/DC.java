package com.example.manualrp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class DC extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schemas_lv_data);
        Log.d("myLogs", "--Class DC start--");
        setTitle("ИБП");
    }
}
