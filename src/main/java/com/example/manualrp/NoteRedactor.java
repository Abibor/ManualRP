package com.example.manualrp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NoteRedactor extends AppCompatActivity {

    EditText noteRedactor;
    TextView date;
    String bs, ids, day, comment;

    Locale loc = new Locale("ru", "RU");
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);

    DatabaseReference ManualRP;

    //Объект для аутентификации
    FirebaseAuth mAuth;
    FirebaseUser user;
    String name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_redactor);
        Log.d("myLogs", "--Class NoteRedactor start--");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        date = (TextView) findViewById(R.id.date);
        date.setText(sdf.format(new Date(System.currentTimeMillis())));

        noteRedactor = (EditText) findViewById(R.id.noteRedactor);

        Intent i = getIntent();
        if(i != null) {
            bs = getIntent().getStringExtra("bs");
            Log.d("myLogs", "get bs = " + bs);

            ids = getIntent().getStringExtra("id");
            Log.d("myLogs", "get id = " + ids);

            noteRedactor.setText(getIntent().getStringExtra("comment"));
            Log.d("myLogs", "get comment = " + comment);
        }

    }

    public void onClickNoteRedactor(View view){
        Log.d("myLogs", "--Class NoteRedactor, onClickNoteRedactor--");

        ManualRP = FirebaseDatabase.getInstance("https://manual-rp-default-rtdb.europe-west1.firebasedatabase.app").getReference("List of BS").child(bs).child(ids);
        day = date.getText().toString();
        name = user.getEmail();
        comment = noteRedactor.getText().toString();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("date", day);
        Log.d("myLogs", "date = " + day);

        Map<String, Object> childUpdates1 = new HashMap<>();
        childUpdates1.put("comment", comment);
        Log.d("myLogs", "comment = " + comment);

        Map<String, Object> childUpdates2 = new HashMap<>();
        childUpdates2.put("userName", name);
        Log.d("myLogs", "userName = " + name);

        ManualRP.updateChildren(childUpdates);
        ManualRP.updateChildren(childUpdates1);
        ManualRP.updateChildren(childUpdates2);

        Toast.makeText(getApplicationContext(),"Сохранено", Toast.LENGTH_LONG).show();
        super.onBackPressed();
    }

}
