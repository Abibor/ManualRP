package com.example.manualrp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Bs extends AppCompatActivity {

    TextView name;
    ListView lvNotes;

    private boolean isFABOpen;

    String bs;
    String ids;
    String date;

    DatabaseReference ManualRP;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String userName;
    String index;

    private List<String> listComment;
    private List<String> listId;
    // List для хранения ссылки на объект из  конкретного дня БД
    private List<DBForm> listDB;
    //private List<Boolean> listFlag;

    MyAdapter myAdapter;

    DialogFragment dialog;

    SharedPreferences sPref;

    final int MENU = 1;

    //boolean checkCrossOutText;
    //boolean flag;

    Locale loc = new Locale("ru", "RU");
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bs);
        Log.d("myLogs", "--Class Bs start--");

        setTitle("Замечания по БС");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        dialog = new DialogBS();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if (!isFABOpen) {
                dialog.show(getSupportFragmentManager(), "dialog");
                getDataFromDB();
            }
        });

        name = (TextView) findViewById(R.id.nameBs);
        lvNotes = findViewById(R.id.lvNotes);
        registerForContextMenu(lvNotes);

        Intent i = getIntent();
        if (i != null) {
            bs = getIntent().getStringExtra("bs");
            Log.d("myLogs", "bs - " + bs);

            name.setText(bs);

            sPref = getSharedPreferences("settings", MODE_PRIVATE);
            SharedPreferences.Editor ed = sPref.edit();
            ed.putString("bs", bs);
            ed.apply();
        }

        listComment = new ArrayList<>();
        listId = new ArrayList<>();
        listDB = new ArrayList<>();
        //listFlag = new ArrayList<>();

        myAdapter = new MyAdapter(this, listDB);

        lvNotes.setAdapter(myAdapter);

        getDataFromDB();
        setOnClickItem();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.to_deleted_notes, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("myLogs", "--Class Bs onOptionsItemSelected--");

        int id = item.getItemId();
        if (id == R.id.delRec) {
            Intent intent = new Intent(this, DeletedNotes.class);
            intent.putExtra("bs", bs);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //ниже метод загружающий значения из БД в ListView через Adapter
    public void getDataFromDB() {
        Log.d("myLogs", "--Class Bs, getDataFromDB--");

        ManualRP = FirebaseDatabase.getInstance("https://manual-rp-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("List of BS").child(bs);
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

                if (listComment.size() > 0) listComment.clear();
                if (listId.size() > 0) listId.clear();
                if (listDB.size() > 0) listDB.clear();
                //if (listFlag.size() > 0) listFlag.clear();

                // с помощью цикла из БД Cars достаются объекты - Children, последовательно
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // далее объекту day присваивается объект ds
                    // которому присваиваются значения из класса DBForm

                    DBForm day = ds.getValue(DBForm.class);

                    // проверка на пустоту "если month не равен 0"
                    assert day != null;
                    String ad = day.comment;
                    // для того чтобы сохранить полученное из цикла занчение и отобразить на экран
                    // добавляем имя date объекта month (либо другое значение)
                    if(!ad.equals("")){
                        listComment.add(day.comment);

                        //listFlag.add(day.checkText);

                        // добавляю в список id всех элементов БД
                        listId.add(ds.getKey());

                        // добавляю в список ссылки на объекты из БД
                        listDB.add(day);
                    }

                }
                Log.d("myLogs", "listComment = " + listComment);
                Log.d("myLogs", "listDB = " + listDB);
                //Log.d("myLogs", "listFlag = " + listFlag);
                Log.d("myLogs", "listId = " + listId);

                // оповещаем адаптер что данные в ListData изменились
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        // добавляем слушаетль в объект Query для упорядоченной выгрузки БД
        //query.addListenerForSingleValueEvent(valueEventListener);
        //query.addValueEventListener(valueEventListener);
        query.addValueEventListener(valueEventListener);
    }

    //Создание методов контекстного меню
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lvNotes) {
            menu.add(0, MENU, 0, "Удалить");
        }
    }

    //метод реализующий удаление элемента из БД при нажатии на строку удалить в контекстном меню
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d("myLogs", "--Class Bs, onContextItemSelected--");

        // получаем информацию о пункте списка
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == MENU) {

            //checkCrossOutText = true;
            date = sdf.format(new Date(System.currentTimeMillis()));
            userName = user.getEmail();

            //info.id - переменная типа long. Это id элемента в БД
            index = listId.get((int) info.id);
            //flag = listFlag.get((int) info.id);

            Log.d("myLogs", "index = " + index);
            //Log.d("myLogs", "flag = " + flag);

            /*
            ManualRP = FirebaseDatabase.getInstance("https://manual-rp-default-rtdb.europe-west1.firebasedatabase.app").getReference("List of BS").child(bs).child(index);
            Map<String, Object> childUpdates1 = new HashMap<>();
            childUpdates1.put("date", date);
            Log.d("myLogs", "date = " + date);

            Map<String, Object> childUpdates2 = new HashMap<>();
            childUpdates2.put("userName", userName);
            Log.d("myLogs", "userName = " + name);

            ManualRP.updateChildren(childUpdates1);
            ManualRP.updateChildren(childUpdates2);

             */

            //метод перемещающий заметку в БД удаленных заметок
            moveBetweenDB();

            //метод перечеркивающий заметку
            //crossOutNote();

            //Обновляем отображение БД
            getDataFromDB();

        }
        return super.onContextItemSelected(item);
    }

    //Метод перемещающий элемент из одной ветки БД в другую (удаление)
    public void moveBetweenDB() {
        DatabaseReference fromDB = FirebaseDatabase.getInstance("https://manual-rp-default-rtdb.europe-west1.firebasedatabase.app").getReference("List of BS").child(bs).child(index);
        DatabaseReference toDB = FirebaseDatabase.getInstance("https://manual-rp-default-rtdb.europe-west1.firebasedatabase.app").getReference("Deleted notes").child(bs).child(index);

        //тут обновляется время и польхователь отредактировавший заметку
        Map<String, Object> childUpdates1 = new HashMap<>();
        childUpdates1.put("date", date);
        Log.d("myLogs", "date = " + date);

        Map<String, Object> childUpdates2 = new HashMap<>();
        childUpdates2.put("userName", userName);
        Log.d("myLogs", "userName = " + name);

        fromDB.updateChildren(childUpdates1);
        fromDB.updateChildren(childUpdates2);

        //далее перенос в таблицу удаленных элементов и удаление из таблицы заметок

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                toDB.setValue(dataSnapshot.getValue()).addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        Log.d("myLogs", "Success!");
                        fromDB.removeValue();
                        getDataFromDB();
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

    //метод перечеркивающий заметку не используется
    /*
    public void crossOutNote(){
        ManualRP = FirebaseDatabase.getInstance("https://manual-rp-default-rtdb.europe-west1.firebasedatabase.app").getReference("List of BS").child(bs).child(index);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("checkText", checkCrossOutText);
        Log.d("myLogs", "checkCrossOutText = " + checkCrossOutText);

        if (!flag) {
            Map<String, Object> childUpdates1 = new HashMap<>();
            childUpdates1.put("date", date);
            Log.d("myLogs", "date = " + date);

            Map<String, Object> childUpdates2 = new HashMap<>();
            childUpdates2.put("userName", userName);
            Log.d("myLogs", "userName = " + name);

            ManualRP.updateChildren(childUpdates1);
            ManualRP.updateChildren(childUpdates2);
        } else {
            Log.d("myLogs", "else flag = " + flag);
        }

        //Обновляем элемент в БД
        ManualRP.updateChildren(childUpdates);
    }

     */

    private void setOnClickItem() {
        Log.d("myLogs", "--Class Bs, setOnClickItem--");
        lvNotes.setOnItemClickListener((parent, view, position, id) -> {
            //Объект DBForm содержит в себе ссылку на объект в базе данных
            DBForm DBForm = listDB.get(position);
            Log.d("myLogs", "DBForm = " + DBForm);
            //Получаем ID выбранного элемента из списка

            //Объект DBForm содержит в себе ссылку на объект в базе данных
            ids = listId.get(position);
            Log.d("myLogs", "ids = " + ids);

            // переходим в экран конкретного дня
            Intent intent = new Intent(this, NoteRedactor.class);
            intent.putExtra("id", ids);
            intent.putExtra("bs", bs);
            intent.putExtra("comment", DBForm.comment);
            Log.d("myLogs", "comment = " + DBForm.comment);
            startActivity(intent);

        });
    }

}
