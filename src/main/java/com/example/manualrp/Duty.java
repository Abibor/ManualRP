package com.example.manualrp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Duty extends AppCompatActivity {

    String january, february, march, april, may, june, july, august, september, october, november, december;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duty);
        Log.d("myLogs", "--Class Duty start--");

        setTitle("График дежурств");
        january = "Январь";
        february = "Февраль";
        march = "Март";
        april = "Апрель";
        may = "Май";
        june = "Июнь";
        july = "Июль";
        august = "Август";
        september = "Сентябрь";
        october = "Октябрь";
        november = "Ноябрь";
        december = "Декабрь";
    }

    public void onClickJanuary(View view) {
        Intent intent = new Intent(this, DutyMonth.class);
        intent.putExtra("month", january);
        startActivity(intent);
    }

    public void onClickFebruary(View view) {
        Intent intent = new Intent(this, DutyMonth.class);
        intent.putExtra("month", february);
        startActivity(intent);
    }

    public void onClickMarch(View view) {
        Intent intent = new Intent(this, DutyMonth.class);
        intent.putExtra("month", march);
        startActivity(intent);
    }

    public void onClickApril(View view) {
        Intent intent = new Intent(this, DutyMonth.class);
        intent.putExtra("month", april);
        startActivity(intent);

    }

    public void onClickMay(View view) {
        Intent intent = new Intent(this, DutyMonth.class);
        intent.putExtra("month", may);
        startActivity(intent);
    }

    public void onClickJune(View view) {
        Intent intent = new Intent(this, DutyMonth.class);
        intent.putExtra("month", june);
        startActivity(intent);
    }

    public void onClickJuly(View view) {
        Intent intent = new Intent(this, DutyMonth.class);
        intent.putExtra("month", july);
        startActivity(intent);
    }

    public void onClickAugust(View view) {
        Intent intent = new Intent(this, DutyMonth.class);
        intent.putExtra("month", august);
        startActivity(intent);
    }

    public void onClickSeptember(View view) {
        Intent intent = new Intent(this, DutyMonth.class);
        intent.putExtra("month", september);
        startActivity(intent);
    }

    public void onClickOctober(View view) {
        Intent intent = new Intent(this, DutyMonth.class);
        intent.putExtra("month", october);
        startActivity(intent);
    }

    public void onClickNovember(View view) {
        Intent intent = new Intent(this, DutyMonth.class);
        intent.putExtra("month", november);
        startActivity(intent);
    }

    public void onClickDecember(View view) {
        Intent intent = new Intent(this, DutyMonth.class);
        intent.putExtra("month", december);
        startActivity(intent);
    }

}
