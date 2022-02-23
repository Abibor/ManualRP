package com.example.manualrp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DialogRedactElement extends DialogFragment implements View.OnClickListener{

    EditText editTextName;
    String name;
    String changeName;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(getDialog()).setTitle("Редактировать имя БС");
        Log.d("myLogs", "--Class DialogRedactElement start--");

        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.dialog_redact_element, null);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        v.findViewById(R.id.editTextName).setOnClickListener(this);
        v.findViewById(R.id.save).setOnClickListener(this);

        editTextName = v.findViewById(R.id.editTextName);

        assert getArguments() != null;
        name = getArguments().getString("name");
        Log.d("myLogs", "name - " + name);

        editTextName.setText(name);
        return v;
    }


    @Override
    public void onClick(View v) {
        Log.d("myLogs", "--Class DialogRedactElement, onClick--");

        changeName = editTextName.getText().toString();
        Log.d("myLogs", "changeName - " + changeName);

        if(!name.equals(changeName)) {
            redactKeyBs();
        }
    }

    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d("myLogs", "--Class DialogRedactElement, onDismiss--");

    }

    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d("myLogs", "--Class DialogRedactElement, onCancel--");
        ((Notes) requireActivity()).getDataFromDB();
    }

    public void redactKeyBs() {
        Log.d("myLogs", "--Class DialogRedactElement, redactKeyBs--");

        DatabaseReference fromDB = FirebaseDatabase.getInstance("https://manual-rp-default-rtdb.europe-west1.firebasedatabase.app").getReference("List of BS").child(name);
        DatabaseReference toDB = FirebaseDatabase.getInstance("https://manual-rp-default-rtdb.europe-west1.firebasedatabase.app").getReference("List of BS").child(changeName);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toDB.setValue(dataSnapshot.getValue()).addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        Log.d("myLogs", "Success!");
                        fromDB.removeValue();
                    } else {
                        Log.d("myLogs", "Copy failed!");
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("myLogs", databaseError.getMessage());
            }
        };
        fromDB.addListenerForSingleValueEvent(valueEventListener);

    }

}
