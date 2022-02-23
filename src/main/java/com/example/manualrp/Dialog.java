package com.example.manualrp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Dialog extends DialogFragment implements View.OnClickListener {

    EditText editText;
    EditText editTextName;
    String bs;
    String name;
    boolean checkName;
    List<String> listNames = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(getDialog()).setTitle("Введите номер базовой станции");
        Log.d("myLogs", "--Class Dialog start--");

        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.dialog_notes, null);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        v.findViewById(R.id.editTextNumber).setOnClickListener(this);
        v.findViewById(R.id.save).setOnClickListener(this);

        editText = v.findViewById(R.id.editTextNumber);
        editTextName = v.findViewById(R.id.editTextName);

        assert getArguments() != null;
        listNames = getArguments().getStringArrayList("names");
        Log.d("myLogs", "listNames - " + listNames);

        return v;
    }

    @Override
    public void onClick(View v) {
        Log.d("myLogs", "--Class Dialog, onClick--");

        bs = editText.getText().toString();
        name = editTextName.getText().toString();

        checkElement();

        if (bs.equals("") || checkName) {
            Toast.makeText(getActivity(),"Элемент уже существует", Toast.LENGTH_SHORT).show();
            dismiss();
        } else {
            Log.d("myLogs", "to do - " + bs);
            Log.d("myLogs", "to name - " + name);

            onClickAddBs();
            ((Notes) requireActivity()).getDataFromDB();
        }
        dismiss();
    }

    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }

    //Ниже метод добавляющий элемент в БД
    public void onClickAddBs() {
        Log.d("myLogs", "--Class Dialog, onClickAddBs--");

        DBForm newDBForm = new DBForm("", "", false, "", "");
        String bsName = bs + " " + name;
        DatabaseReference ManualRP = FirebaseDatabase.getInstance("https://manual-rp-default-rtdb.europe-west1.firebasedatabase.app").getReference("List of BS").child(bsName);
        ManualRP.push().setValue(newDBForm);

        Intent intent = new Intent(getActivity(), Bs.class);
        intent.putExtra("bs", bs + " " + name);
        Log.d("myLogs", "Intent to Class Bs - " + bs + " " + name);
        startActivity(intent);
    }

    public void checkElement() {
        Log.d("myLogs", "--Class Dialog, checkElement--");

        Log.d("myLogs", "bs - " + bs);

        for (String string : listNames) {
            if (string.contains(bs)) {
                Log.d("myLogs", "Нашел подобный - " + string);
                checkName = true;
            }
        }
    }
}
