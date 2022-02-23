package com.example.manualrp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeletedNotes extends AppCompatActivity {

    TextView name;
    String bs;
    String ids;
    ListView lvNotes;
    DatabaseReference ManualRP;
    MyAdapter myAdapter;

    private List<String> listComment;
    private List<String> listId;
    private List<DBForm> listDB;
    //private List<Boolean> listFlag;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deleted_notes);
        Log.d("myLogs", "--Class DeletedNotes start--");

        setTitle("Архив замечаний по БС");

        name = (TextView) findViewById(R.id.nameBs);
        lvNotes = findViewById(R.id.lvNotes);

        listComment = new ArrayList<>();
        listId = new ArrayList<>();
        listDB = new ArrayList<>();
        //listFlag = new ArrayList<>();

        myAdapter = new MyAdapter(this, listDB);
        lvNotes.setAdapter(myAdapter);

        Intent i = getIntent();
        if (i != null) {
            bs = getIntent().getStringExtra("bs");
            Log.d("myLogs", "bs - " + bs);

            name.setText(bs);
        }

        getDataFromDB();
        setOnClickItem();
    }

    private void getDataFromDB() {
        Log.d("myLogs", "--Class Bs, getDataFromDB--");

        ManualRP = FirebaseDatabase.getInstance("https://manual-rp-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Deleted notes").child(bs);
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

                    // для того чтобы сохранить полученное из цикла занчение и отобразить на экран
                    // добавляем имя date объекта month (либо другое значение)
                    listComment.add(day.comment);

                    //listFlag.add(day.checkText);

                    // добавляю в список id всех элементов БД
                    listId.add(ds.getKey());

                    // добавляю в список ссылки на объекты из БД
                    listDB.add(day);
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
        query.addListenerForSingleValueEvent(valueEventListener);
    }

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
            Intent intent = new Intent(this, DeletedNoteReview.class);
            intent.putExtra("id", ids);
            intent.putExtra("bs", bs);
            intent.putExtra("comment", DBForm.comment);
            Log.d("myLogs", "comment = " + DBForm.comment);
            startActivity(intent);

        });
    }
}
