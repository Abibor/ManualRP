package com.example.manualrp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("myLogs", "--Class MainActivity start--");
    }

    public void onClickNotes(View view){
        Intent intent = new Intent(this, Notes.class);
        startActivity(intent);
    }

    public void onClickContacts(View view){
        Intent intent = new Intent(this, Contacts.class);
        startActivity(intent);
    }

    public void onClickDuty(View view){
        Intent intent = new Intent(this, Duty.class);
        startActivity(intent);
    }

    public void onClickSchemas(View view){
        Intent intent = new Intent(this, Schemas.class);
        startActivity(intent);
    }

    public void onClickReports(View view){
        Intent intent = new Intent(this, Reports.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.auth, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.auth) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}