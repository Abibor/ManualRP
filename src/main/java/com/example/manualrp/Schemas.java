package com.example.manualrp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Schemas extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schemas);
        Log.d("myLogs", "--Class Schemas start--");
        setTitle("Схемы и подключения");
    }

    public void onClickBss(View view){
        Intent intent = new Intent(this, Bss.class);
        startActivity(intent);
    }

    public void onClickRll(View view){
        Intent intent = new Intent(this, Rrl.class);
        startActivity(intent);
    }

    public void onClickDc(View view){
        Intent intent = new Intent(this, DC.class);
        startActivity(intent);
    }

    public void onClickOther(View view){
        Intent intent = new Intent(this, Other.class);
        startActivity(intent);
    }

}
