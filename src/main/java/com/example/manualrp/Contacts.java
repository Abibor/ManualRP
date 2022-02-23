package com.example.manualrp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Contacts extends AppCompatActivity {

    private boolean isFABOpen;
    DatabaseReference ManualRP;
    private List<String> listPhone;
    ListView lvPhone;
    //ArrayAdapter<String> adapter;
    EditText editText;
    String bs;
    final int MENU = 1;

    String ids;

    ContactAdapter contactAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);
        Log.d("myLogs", "--Class Contacts start--");

        setTitle("Контакты");
        listPhone = new ArrayList<>();
        lvPhone = findViewById(R.id.lvPhone);
        registerForContextMenu(lvPhone);
        //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listPhone);
        contactAdapter = new ContactAdapter(this, listPhone);
        lvPhone.setAdapter(contactAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if(!isFABOpen){
                Intent intent = new Intent(this, BsPhone.class);
                startActivity(intent);
                Log.d("myLog", "Click");
            } else{
                Log.d("myLog", "onStart");
            }
        });
        getDataFromDB();
        getSearchFromDB();
        setOnClickItem();
    }

    protected void onResume() {
        super.onResume();
        Log.d("myLog", "onResume");
        getDataFromDB();
        Log.d("myLog", "getDataFromDB");
    }

    // ниже метод загружающий значения из БД в ListView через Adapter
    public void getDataFromDB() {
        ManualRP = FirebaseDatabase.getInstance("https://manual-rp-default-rtdb.europe-west1.firebasedatabase.app").getReference("Contacts");
        // с помощью метода Query упорядочиваем элементы в БД по полю "bs"
        Query query = ManualRP.orderByKey();
        // считываем базу данных через слушатель vListener и его методы
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            // в методе ниже меняется информация о БД, о том что в ней внутри
            // и метод будет выдавать через объект dataSnapshot данные
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // для корректной работы спика приходится обнулять его каждый раз, иначе
                // после каждого удаления элемента будет происходить факториал дублирование элементов bs
                // убеждаемся что список чистый или очищаем его

                if (listPhone.size() > 0) listPhone.clear();

                // с помощью цикла из БД Cars достаются объекты - Children, последовательно
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // далее объекту day присваивается объект ds
                    // которому присваиваются значения из класса DBForm

                    DBPhones day = ds.getValue(DBPhones.class);

                    // проверка на пустоту "если month не равен 0"
                    assert day != null;

                    // для того чтобы сохранить полученное из цикла занчение и отобразить на экран
                    // добавляем имя date объекта month (либо другое значение)
                    // сохраняем в list значения заголовков таблиц
                    // добавляю в список id всех элементов БД
                    listPhone.add(ds.getKey());
                }
                Log.d("myLogs", "listPhone = " + listPhone);

                // оповещаем адаптер что данные в ListData изменились
                contactAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        // добавляем слушаетль в объект Query для упорядоченной выгрузки БД
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    // ниже метод загружающий значения из БД в ListView через Adapter
    public void getSearchFromDB() {
        Log.d("myLog", "getSearchFromDB");

        editText = findViewById(R.id.searchPhone);

        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contactAdapter.getFilter().filter(s);
            }
        });
    }

    // Метод котрый открывает позицию списка в отдельном окне
    private void setOnClickItem() {
        lvPhone.setOnItemClickListener((parent, view, position, id) -> {
            //для отображения элемента с учетом фильтрации используется getItem
            bs = listPhone.get(position);
            Log.d("myLog", "bs = " + bs);

            //переходим в экран конкретного дня
            Intent intent = new Intent(this, BsPhone.class);
            intent.putExtra("contact", bs);
            startActivity(intent);
        });
    }

    //Создание методов контекстного меню
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lvPhone) {
            menu.add(0,MENU,0,"Удалить");
        }
    }

    //метод реализующий удаление элемента из БД при нажатии на строку удалить в контекстном меню
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // получаем информацию о пункте списка
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == MENU) {
            // info.id - переменная типа long. Это id элемента в БД
            String index = listPhone.get((int) info.id);
            Log.d("myLogs", "index = " + index);
            ManualRP = FirebaseDatabase.getInstance("https://manual-rp-default-rtdb.europe-west1.firebasedatabase.app").getReference("Contacts").child(index);
            ManualRP.removeValue();
            getDataFromDB();
        }

        return super.onContextItemSelected(item);
    }

}
