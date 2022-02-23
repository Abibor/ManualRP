package com.example.manualrp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class BsPhone extends AppCompatActivity {

    DatabaseReference ManualRP;
    EditText number;
    EditText name;
    EditText contact;
    EditText contact1;
    EditText contact2;
    EditText contact3;
    EditText phone;
    EditText phone1;
    EditText phone2;
    EditText phone3;
    EditText comm;

    String bsNumber;
    String nameBs;
    String fio;
    String fio1;
    String fio2;
    String fio3;
    String editPhone;
    String editPhone1;
    String editPhone2;
    String editPhone3;
    String comment;
    String bs;

    String ids;
    String text;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bs_phone);
        Log.d("myLogs", "--Class BsPhone start--");

        setTitle("Карточка контакта");

        number = findViewById(R.id.bsNumber);
        name = findViewById(R.id.nameBs);
        contact = findViewById(R.id.fio);
        contact1 = findViewById(R.id.fio1);
        contact2 = findViewById(R.id.fio2);
        contact3 = findViewById(R.id.fio3);
        phone = findViewById(R.id.editPhone);
        phone1 = findViewById(R.id.editPhone1);
        phone2 = findViewById(R.id.editPhone2);
        phone3 = findViewById(R.id.editPhone3);
        comm = findViewById(R.id.comment);

        Intent i = getIntent();
        if (i != null) {
            bs = getIntent().getStringExtra("contact");
            Log.d("myLogs", "bs - " + bs);
            if (bs != null) {
                getDataFromDB();
            }
        }
    }

    public void onClickSaveContact(View view) {
        Log.d("myLogs", "--Class BsPhone onClickSaveContact--");

        bsNumber = number.getText().toString();
        nameBs = name.getText().toString();
        fio = contact.getText().toString();
        fio1 = contact1.getText().toString();
        fio2 = contact2.getText().toString();
        fio3 = contact3.getText().toString();
        editPhone = phone.getText().toString();
        editPhone1 = phone1.getText().toString();
        editPhone2 = phone2.getText().toString();
        editPhone3 = phone3.getText().toString();
        comment = comm.getText().toString();

        Log.d("myLog", "bsNumber - " + bsNumber);
        Log.d("myLog", "nameBs - " + nameBs);
        Log.d("myLog", "fio - " + fio);
        Log.d("myLog", "fio1 - " + fio1);
        Log.d("myLog", "fio2 - " + fio2);
        Log.d("myLog", "fio3 - " + fio3);
        Log.d("myLog", "editPhone - " + editPhone);
        Log.d("myLog", "editPhone1 - " + editPhone1);
        Log.d("myLog", "editPhone2 - " + editPhone2);
        Log.d("myLog", "editPhone3 - " + editPhone3);
        Log.d("myLog", "comment - " + comment);

        if (bs == null) {
            if (bsNumber.equals("")) {
                ManualRP = FirebaseDatabase.getInstance("https://manual-rp-default-rtdb.europe-west1.firebasedatabase.app").getReference("Contacts").child(nameBs);
                DBPhones newDBPhones = new DBPhones(bsNumber, nameBs, fio, fio1, fio2, fio3, editPhone,
                        editPhone1, editPhone2, editPhone3, comment);
                ManualRP.push().setValue(newDBPhones);
                super.onBackPressed();
            } else {
                ManualRP = FirebaseDatabase.getInstance("https://manual-rp-default-rtdb.europe-west1.firebasedatabase.app").getReference("Contacts").child(bsNumber);
                DBPhones newDBPhones = new DBPhones(bsNumber, nameBs, fio, fio1, fio2, fio3, editPhone,
                        editPhone1, editPhone2, editPhone3, comment);
                ManualRP.push().setValue(newDBPhones);
                super.onBackPressed();
            }
        } else {
            ManualRP = FirebaseDatabase.getInstance("https://manual-rp-default-rtdb.europe-west1.firebasedatabase.app").getReference("Contacts").child(bs).child(ids);

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("numberBs", bsNumber);
            Log.d("myLogs", "bsNumber = " + bsNumber);

            Map<String, Object> childUpdates1 = new HashMap<>();
            childUpdates1.put("nameBs", nameBs);
            Log.d("myLogs", "nameBs = " + nameBs);

            Map<String, Object> childUpdates2 = new HashMap<>();
            childUpdates2.put("fio", fio);
            Log.d("myLogs", "fio = " + fio);

            Map<String, Object> childUpdates3 = new HashMap<>();
            childUpdates3.put("fio1", fio1);
            Log.d("myLogs", "fio1 = " + fio1);

            Map<String, Object> childUpdates4 = new HashMap<>();
            childUpdates1.put("fio2", fio2);
            Log.d("myLogs", "fio2 = " + fio2);

            Map<String, Object> childUpdates5 = new HashMap<>();
            childUpdates1.put("fio3", fio3);
            Log.d("myLogs", "fio3 = " + fio3);

            Map<String, Object> childUpdates6 = new HashMap<>();
            childUpdates1.put("phoneNumber", editPhone);
            Log.d("myLogs", "editPhone = " + editPhone);

            Map<String, Object> childUpdates7 = new HashMap<>();
            childUpdates1.put("phoneNumber1", editPhone1);
            Log.d("myLogs", "editPhone = " + editPhone1);

            Map<String, Object> childUpdates8 = new HashMap<>();
            childUpdates1.put("phoneNumber2", editPhone2);
            Log.d("myLogs", "editPhone2 = " + editPhone2);

            Map<String, Object> childUpdates9 = new HashMap<>();
            childUpdates1.put("phoneNumber3", editPhone3);
            Log.d("myLogs", "editPhone3 = " + editPhone3);

            Map<String, Object> childUpdates10 = new HashMap<>();
            childUpdates1.put("comment", comment);
            Log.d("myLogs", "comment = " + comment);

            ManualRP.updateChildren(childUpdates);
            ManualRP.updateChildren(childUpdates1);
            ManualRP.updateChildren(childUpdates2);
            ManualRP.updateChildren(childUpdates3);
            ManualRP.updateChildren(childUpdates4);
            ManualRP.updateChildren(childUpdates5);
            ManualRP.updateChildren(childUpdates6);
            ManualRP.updateChildren(childUpdates7);
            ManualRP.updateChildren(childUpdates8);
            ManualRP.updateChildren(childUpdates9);
            ManualRP.updateChildren(childUpdates10);
        }
        Toast.makeText(getApplicationContext(),"Сохранено", Toast.LENGTH_LONG).show();
    }

    public void getDataFromDB() {
        Log.d("myLogs", "--Class BsPhone getDataFromDB--");

        ManualRP = FirebaseDatabase.getInstance("https://manual-rp-default-rtdb.europe-west1.firebasedatabase.app").getReference("Contacts").child(bs);
        // с помощью метода Query упорядочиваем элементы в БД по полю "bs"
        Query query = ManualRP.orderByKey();
        // считываем базу данных через слушатель vListener и его методы
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            // в методе ниже меняется информация о БД, о том что в ней внутри
            // и метод будет выдавать через объект dataSnapshot данные
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // с помощью цикла из БД Cars достаются объекты - Children, последовательно
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // далее объекту day присваивается объект ds
                    // которому присваиваются значения из класса DBForm

                    DBPhones card = ds.getValue(DBPhones.class);

                    // проверка на пустоту "если month не равен 0"
                    assert card != null;

                    // для того чтобы сохранить полученное из цикла занчение и отобразить на экран
                    // добавляем имя date объекта month (либо другое значение)
                    ids = ds.getKey();
                    number.setText(card.numberBs);
                    name.setText(card.nameBs);
                    contact.setText(card.fio);
                    contact1.setText(card.fio1);
                    contact2.setText(card.fio2);
                    contact3.setText(card.fio3);
                    phone.setText(card.phoneNumber);
                    phone1.setText(card.phoneNumber1);
                    phone2.setText(card.phoneNumber2);
                    phone3.setText(card.phoneNumber3);
                    comm.setText(card.comment);
                    Log.d("myLogs", "ids = " + ids);
                    Log.d("myLogs", "number = " + card.numberBs);
                    Log.d("myLogs", "name = " + card.nameBs);
                    Log.d("myLogs", "contact = " + card.fio);
                    Log.d("myLogs", "contact1 = " + card.fio1);
                    Log.d("myLogs", "contact2 = " + card.fio2);
                    Log.d("myLogs", "contact3 = " + card.fio3);
                    Log.d("myLogs", "phone = " + card.phoneNumber);
                    Log.d("myLogs", "phone1 = " + card.phoneNumber1);
                    Log.d("myLogs", "phone2 = " + card.phoneNumber2);
                    Log.d("myLogs", "phone3 = " + card.phoneNumber3);
                    Log.d("myLogs", "comm = " + card.comment);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        // добавляем слушаетль в объект Query для упорядоченной выгрузки БД
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.provider, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("myLogs", "--Class BsPhone onOptionsItemSelected--");

        int id = item.getItemId();
        if (id == R.id.share) {
            bsNumber = number.getText().toString();
            nameBs = name.getText().toString();
            fio = contact.getText().toString();
            fio1 = contact1.getText().toString();
            fio2 = contact2.getText().toString();
            fio3 = contact3.getText().toString();
            editPhone = phone.getText().toString();
            editPhone1 = phone1.getText().toString();
            editPhone2 = phone2.getText().toString();
            editPhone3 = phone3.getText().toString();
            comment = comm.getText().toString();

            Log.d("myLog", "bsNumber - " + bsNumber);
            Log.d("myLog", "nameBs - " + nameBs);
            Log.d("myLog", "fio - " + fio);
            Log.d("myLog", "fio1 - " + fio1);
            Log.d("myLog", "fio2 - " + fio2);
            Log.d("myLog", "fio3 - " + fio3);
            Log.d("myLog", "editPhone - " + editPhone);
            Log.d("myLog", "editPhone1 - " + editPhone1);
            Log.d("myLog", "editPhone2 - " + editPhone2);
            Log.d("myLog", "editPhone3 - " + editPhone3);
            Log.d("myLog", "editPhone3 - " + comment);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");

            if(!bsNumber.equals("")||!nameBs.equals("")){
                text = bsNumber + " " + nameBs;
            }
            if(!fio.equals("")||!editPhone.equals("")){
                text = text + "\n" + fio + " " + editPhone;
            }
            if(!fio1.equals("")||!editPhone1.equals("")){
                text = text + "\n" + fio1 + " " + editPhone1;
            }
            if(!fio2.equals("")||!editPhone2.equals("")){
                text = text + "\n" + fio2 + " " + editPhone2;
            }
            if(!comment.equals("")){
                text = text + "\n" + comment;
            }
            Log.d("myLog", "text - " + text);

            intent.putExtra(Intent.EXTRA_TEXT, text);

            try {
                startActivity(Intent.createChooser(intent, "Select an action"));
            } catch (android.content.ActivityNotFoundException ex) {
                // (handle error)
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
  }
