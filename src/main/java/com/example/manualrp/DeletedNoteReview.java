package com.example.manualrp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DeletedNoteReview extends AppCompatActivity {

    TextView note;
    String bs, ids;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_review);
        Log.d("myLogs", "--Class DeletedNoteReview start--");

        note = (TextView) findViewById(R.id.note);

        Intent i = getIntent();
        if(i != null) {
            bs = getIntent().getStringExtra("bs");
            Log.d("myLogs", "get bs = " + bs);

            ids = getIntent().getStringExtra("id");
            Log.d("myLogs", "get id = " + ids);

            note.setText(getIntent().getStringExtra("comment"));
            Log.d("myLogs", "get comment = " + note);
        }
    }
}
