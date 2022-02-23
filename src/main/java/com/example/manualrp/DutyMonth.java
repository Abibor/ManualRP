package com.example.manualrp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DutyMonth extends AppCompatActivity {
    // получаем доступ к базе донных:
    private TouchImageView imImage;
    private StorageReference mStorageRef;
    StorageReference photoReference;

    String month;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duty_month);
        Log.d("myLogs", "--Class DutyMonth start--");

        imImage = findViewById(R.id.imImage);
        mStorageRef = FirebaseStorage.getInstance().getReference("Duty");

        Intent i = getIntent();
        if (i != null) {
            month = getIntent().getStringExtra("month");
        }
        setTitle(month);
        getMonth();
    }

    private void getMonth() {
        if (month.equals("Январь")) {
            photoReference = mStorageRef.child("duty_01.22.png");
        }
        if (month.equals("Февраль")) {
            photoReference = mStorageRef.child("duty_02.22.png");
        }
        if (month.equals("Март")) {
            photoReference = mStorageRef.child("duty_03.22.png");
        }
        if (month.equals("Апрель")) {
            photoReference = mStorageRef.child("duty_04.22.png");
        }
        if (month.equals("Май")) {
            photoReference = mStorageRef.child("duty_05.22.png");
        }
        if (month.equals("Июнь")) {
            photoReference = mStorageRef.child("duty_06.22.png");
        }
        if (month.equals("Июль")) {
            photoReference = mStorageRef.child("duty_07.22.png");
        }
        if (month.equals("Август")) {
            photoReference = mStorageRef.child("duty_08.22.png");
        }
        if (month.equals("Сентябрь")) {
            photoReference = mStorageRef.child("duty_09.22.png");
        }
        if (month.equals("Октябрь")) {
            photoReference = mStorageRef.child("duty_10.22.png");
        }
        if (month.equals("Ноябрь")) {
            photoReference = mStorageRef.child("duty_11.22.png");
        }
        if (month.equals("Декабрь")) {
            photoReference = mStorageRef.child("duty_12.22.png");
        }

        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imImage.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
