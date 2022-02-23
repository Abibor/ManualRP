package com.example.manualrp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class DialogBS extends DialogFragment implements View.OnClickListener{

    EditText editText;
    String note;
    String bs;
    String name;
    DatabaseReference ManualRP;
    SharedPreferences sPref;
    public static final int MODE_PRIVATE = 0x0000;

    Locale loc = new Locale("ru", "RU");
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);
    String date = sdf.format(new Date(System.currentTimeMillis()));
    boolean checkCrossOutText = true;

    //Объект для аутентификации
    private FirebaseAuth mAuth;
    FirebaseUser user;

    Spinner spinner;
    ArrayAdapter<String> adapter;
    String [] category = {"СКВ", "БС", "РРЛ", "ИБП", "ПРОЧЕЕ"};
    String dropList;

    @SuppressLint("WrongConstant")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(getDialog()).setTitle("Введите замечание");
        Log.d("myLogs", "--Class DialogBS start--");

        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.dialog_bs, null);

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        v.findViewById(R.id.editNote).setOnClickListener(this);
        v.findViewById(R.id.saveNote).setOnClickListener(this);
        editText = v.findViewById(R.id.editNote);

        sPref = this.requireContext().getSharedPreferences("settings", MODE_PRIVATE);
        bs = sPref.getString("bs", bs);
        Log.d("myLogs", "bs - " + bs);
        ManualRP = FirebaseDatabase.getInstance("https://manual-rp-default-rtdb.europe-west1.firebasedatabase.app").getReference("List of BS").child(bs);

        spinner = v.findViewById(R.id.spinner);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("myLogs", "position = " + position);
                if (position == 0) {
                    dropList = category[0];
                }
                if (position == 1) {
                    dropList = category[1];
                }
                if (position == 2) {
                    dropList = category[2];
                }
                if (position == 3) {
                    dropList = category[3];
                }
                if (position == 4) {
                    dropList = category[4];
                }
                Log.d("myLogs", "dropList = " + dropList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }
    @Override
    public void onClick(View v) {
        note = editText.getText().toString();
        Log.d("myLogs", "Dialog bs - " + note);
        if (note.equals("")){
            Log.d("myLogs", "Empty bs - " + note);
            dismiss();
        } else {
            Log.d("myLogs", "to do - " + note);
            onClickAddBs();
            ((Bs) requireActivity()).getDataFromDB();
            editText.setText("");
        }
        dismiss();
    }

    public void onClickAddBs() {
        Log.d("myLogs", "--Class DialogBS onClickAddBs--");
        name = user.getEmail();
        Log.d("myLogs", "userName - " + name);

        //DBForm newDBForm = new DBForm(date, note, checkCrossOutText);
        DBForm newDBForm = new DBForm(date, note, checkCrossOutText, name, dropList);
        //DBForm newDBForm = new DBForm(date, note, name, dropList);
        ManualRP.push().setValue(newDBForm);
        /*
        DBForm newDBForm1 = new DBForm("", "", "", "");
        ManualRP.push().setValue(newDBForm1);

         */
    }

    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

}
