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
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Notes extends AppCompatActivity {

    // получаем доступ к базе донных:
    DatabaseReference ManualRP;
    String bsNotes = "List of BS";

    String ids;
    String indexBDB;

    private boolean isFABOpen;

    ListView lvData;

    BsAdapter bsAdapter;

    // в ArrayAdapter можно передать только List, поэтому создаем List для отображения БД
    // List типа String для хранения в себе строк дат из отображаемого месяца БД
    private List<String> listId;
    private List<DBForm> listBs;
    private List<Boolean> listFlags;
    private List<Boolean> listFlagsToAdapter;
    private List<String> listComments;

    DialogFragment dialog;
    DialogFragment dialogRedact;

    final int MENU = 1;
    boolean flag;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes);
        Log.d("myLogs", "--Class Notes start--");

        FirebaseApp.initializeApp(this);
        //Поскольку создал подключение Firebase не сразу в проект, а после прочих дествий, приходится прописывать адресс БД в скобках
        //ManualRP = FirebaseDatabase.getInstance("https://manual-rp-default-rtdb.europe-west1.firebasedatabase.app").getReference(bsNotes);

        setTitle("Список БС");

        listId = new ArrayList<>();
        listBs = new ArrayList<>();
        listFlags = new ArrayList<>();
        listFlagsToAdapter = new ArrayList<>();
        listComments = new ArrayList<>();

        lvData = findViewById(R.id.lvData);
        registerForContextMenu(lvData);

        bsAdapter = new BsAdapter(this, listId, listBs, listFlagsToAdapter);
        //bsAdapter = new BsAdapter(this, listId, listBs);

        lvData.setAdapter(bsAdapter);

        dialog = new Dialog();
        dialogRedact = new DialogRedactElement();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if (!isFABOpen) {
                Bundle args = new Bundle();
                args.putStringArrayList("names", (ArrayList<String>) listId);
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "dialog");
            } else {
                Log.d("myLog", "WRONG!");
            }
        });

        getDataFromDB();
        getSearchFromDB();
        setOnClickItem();
    }

    // ниже метод загружающий значения из БД в ListView через Adapter
    public void getDataFromDB() {
        Log.d("myLogs", "--Class Notes, getDataFromDB--");

        ManualRP = FirebaseDatabase.getInstance("https://manual-rp-default-rtdb.europe-west1.firebasedatabase.app").getReference(bsNotes);
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

                if (listId.size() > 0) listId.clear();
                if (listBs.size() > 0) listBs.clear();
                if (listFlags.size() > 0) listFlags.clear();
                if (listComments.size() > 0) listComments.clear();
                if (listFlagsToAdapter.size() > 0) listFlagsToAdapter.clear();

                // с помощью цикла из БД Cars достаются объекты - Children, последовательно
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // сохраняем в list значения заголовков таблиц
                    listId.add(ds.getKey());
                    // цикл для погружение в БД на один разряд и получения олгической переменной
                    // в адаптер
                    for (DataSnapshot child : ds.getChildren()) {
                        // объекту day присваивается объект child
                        // которому присваиваются значения из класса DBForm
                        DBForm day = child.getValue(DBForm.class);
                        // проверка на пустоту "если day не равен 0"
                        assert day != null;
                        listBs.add(day);
                        listComments.add(day.comment);
                        listFlags.add(day.checkText);
                    }

                    Log.d("myLogs", "listComments = " + listComments);
                    Log.d("myLogs", "listFlags = " + listFlags);

                    flag = listFlags.contains(true);
                    listFlagsToAdapter.add(flag);

                    Log.d("myLogs", "flag = " + flag);

                    listFlags.clear();
                    listComments.clear();
                }
                Log.d("myLogs", "listId = " + listId);
                Log.d("myLogs", "listBs = " + listBs);

                // оповещаем адаптер что данные в ListData изменились
                bsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        // добавляем слушаетль в объект Query для упорядоченной выгрузки БД
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    //Создание методов контекстного меню
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lvData) {
            //menu.add(0, MENU, 0, "Удалить");
            menu.add(0, MENU, 0, "Редактировать");
        }
    }

    //метод реализующий появление контекстного меню и выбора удаления элемента из БД
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d("myLogs", "--Class Notes, onContextItemSelected--");

        // получаем информацию о пункте списка
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == MENU) {
            //info.id - переменная типа long. Это id элемента в БД
            indexBDB = listId.get((int) info.id);
            Log.d("myLogs", "index = " + indexBDB);

            redactElement();
            //moveBetweenDB();
            //getDataFromDB();
        }
        return super.onContextItemSelected(item);
    }

    protected void onResume() {
        super.onResume();
        Log.d("myLog", "onResume");
        getDataFromDB();
    }

    // Метод котрый открывает позицию списка в отдельном окне
    private void setOnClickItem() {
        Log.d("myLogs", "--Class Notes, setOnClickItem--");

        lvData.setOnItemClickListener((parent, view, position, id) -> {
            //Получаем позицию выбранного элемента из списка
            //для отображения элемента с учетом фильтрации используется getItem
            ids = listId.get(position);

            Log.d("myLog", "ids = " + ids);

            //переходим в экран конкретного дня
            Intent intent = new Intent(this, Bs.class);
            intent.putExtra("bs", ids);
            startActivity(intent);
        });
    }

    //Метод поиска (фильтровки) элемента в textview
    public void getSearchFromDB() {
        Log.d("myLogs", "--Class Notes, getSearchFromDB--");

        EditText editText = findViewById(R.id.search);

        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bsAdapter.getFilter().filter(s);
            }
        });
    }

    public void redactElement() {
        Bundle args = new Bundle();
        args.putString("name", indexBDB);
        dialogRedact.setArguments(args);
        dialogRedact.show(getSupportFragmentManager(), "dialog");
    }

    //Метод перемещающий элемент из одной ветки БД в другую (удаление)
    public void moveBetweenDB() {
        DatabaseReference fromDB = FirebaseDatabase.getInstance("https://manual-rp-default-rtdb.europe-west1.firebasedatabase.app").getReference("List of BS").child(indexBDB);
        DatabaseReference toDB = FirebaseDatabase.getInstance("https://manual-rp-default-rtdb.europe-west1.firebasedatabase.app").getReference("Deleted notes").child(indexBDB);

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

    //Вариант кода для перемещения элемента из одной ветки БД в другую
    /*for (DataSnapshot ds : dataSnapshot.getChildren()) {
    DBForm day = ds.getValue(DBForm.class);
    assert day != null;
    FirebaseDatabase.getInstance("https://manual-rp-default-rtdb.europe-west1.firebasedatabase.app").getReference("Deleted notes").child(indexBDB).push().setValue(day);
    }*/
}
