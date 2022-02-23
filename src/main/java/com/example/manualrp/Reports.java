package com.example.manualrp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Reports extends AppCompatActivity {

    Spinner spinner;
    ArrayAdapter<String> adapter;
    String[] category = {"ВСЕ БС", "СКВ", "БС", "РРЛ", "ИБП", "ПРОЧЕЕ"};
    String[] titles = {"NAME", "СКВ", "БС", "РРЛ", "ИБП", "ПРОЧЕЕ"};
    String[] table;
    String[] toCsvNames;
    String[] toCsvComments;
    String[] toCsvlistSKV;
    String[] toCsvlistBSS;
    String[] toCsvlistRRL;
    String[] toCsvlistIBP;
    String[] toCsvlistOTHER;
    String dropList;

    // получаем доступ к базе донных:
    DatabaseReference ManualRP;
    String bsNotes = "List of BS";

    ArrayAdapter<String> adapter1;
    ListView lvData;
    List<String> listBs;
    List<String> listNames;
    List<String> listComments;

    List<String> listSKV;
    List<String> listBSS;
    List<String> listRRL;
    List<String> listIBP;
    List<String> listOTHER;

    String getChild;
    String getKey;
    Set<String> s;

    File file1;
    File file;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports);
        Log.d("myLogs", "--Class Reports start--");

        setTitle("Отчеты");

        listBs = new ArrayList<>();
        listNames = new ArrayList<>();
        listComments = new ArrayList<>();
        s = new LinkedHashSet<>();

        listSKV = new ArrayList<>();
        listBSS = new ArrayList<>();
        listRRL = new ArrayList<>();
        listIBP = new ArrayList<>();
        listOTHER = new ArrayList<>();

        lvData = findViewById(R.id.lvData);
        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listBs);
        lvData.setAdapter(adapter1);

        spinner = findViewById(R.id.spinner);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("myLogs", "position = " + position);
                if (position == 0) {
                    dropList = category[0];
                    getDataFromDB();
                }
                if (position == 1) {
                    dropList = category[1];
                    getDataFromDB();
                }
                if (position == 2) {
                    dropList = category[2];
                    getDataFromDB();
                }
                if (position == 3) {
                    dropList = category[3];
                    getDataFromDB();
                }
                if (position == 4) {
                    dropList = category[4];
                    getDataFromDB();
                }
                if (position == 5) {
                    dropList = category[5];
                    getDataFromDB();
                }
                Log.d("myLogs", "dropList = " + dropList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setOnClickItem();
    }

    public void getDataFromDB() {
        Log.d("myLogs", "--Class Reports, getDataFromDB--");

        ManualRP = FirebaseDatabase.getInstance("https://manual-rp-default-rtdb.europe-west1.firebasedatabase.app").getReference(bsNotes);
        // с помощью метода Query упорядочиваем элементы в БД по полю "bs"
        Query query = ManualRP.orderByKey();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // для корректной работы спика приходится обнулять его каждый раз, иначе
                    // после каждого удаления элемента будет происходить факториал дублирование элементов bs
                    // убеждаемся что список чистый или очищаем его

                    if (listBs.size() > 0) listBs.clear();
                    if (listNames.size() > 0) listNames.clear();
                    if (listComments.size() > 0) listComments.clear();
                    if (s.size() > 0) s.clear();
                    if (listSKV.size() > 0) listSKV.clear();
                    if (listBSS.size() > 0) listBSS.clear();
                    if (listRRL.size() > 0) listRRL.clear();
                    if (listIBP.size() > 0) listIBP.clear();
                    if (listOTHER.size() > 0) listOTHER.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Log.d("myLogs", "ds - " + ds.getKey());
                        getKey = ds.getKey();
                        for (DataSnapshot child : ds.getChildren()) {
                            Log.d("myLogs", "child - " + child.getKey());

                            DBForm day = child.getValue(DBForm.class);
                            assert day != null;
                            getChild = day.category;
                            Log.d("myLogs", "getChild - " + getChild);
                            Log.d("myLogs", " ---- ");
                            if (getChild.equals(dropList)) {
                                s.add(getKey);
                                listNames.add(getKey);
                                listComments.add(day.comment);
                            }
                            if (dropList.equals("ВСЕ БС")) {
                                s.add(getKey);
                                listNames.add(getKey);
                                if (getChild.equals(category[1])) {
                                    listSKV.add(day.comment);
                                } else {
                                    listSKV.add(" ");
                                }

                                if (getChild.equals(category[2])) {
                                    listBSS.add(day.comment);
                                } else {
                                    listBSS.add(" ");
                                }

                                if (getChild.equals(category[3])) {
                                    listRRL.add(day.comment);
                                } else {
                                    listRRL.add(" ");
                                }

                                if (getChild.equals(category[4])) {
                                    listIBP.add(day.comment);
                                } else {
                                    listIBP.add(" ");
                                }

                                if (getChild.equals(category[5])) {
                                    listOTHER.add(day.comment);
                                } else {
                                    listOTHER.add(" ");
                                }
                            }
                        }
                        Log.d("myLogs", "s = " + s);
                        Log.d("myLogs", "listNames = " + listNames);
                        Log.d("myLogs", "listComments = " + listComments);
                        Log.d("myLogs", "listSKV = " + listSKV);
                        Log.d("myLogs", "listBSS = " + listBSS);
                        Log.d("myLogs", "listRRL = " + listRRL);
                        Log.d("myLogs", "listIBP = " + listIBP);
                        Log.d("myLogs", "listOTHER = " + listOTHER);
                    }
                    listBs.addAll(s);
                    Log.d("myLogs", "listBs = " + listBs);
                    Log.d("myLogs", "listBs = " + listBs.size());
                    Log.d("myLogs", "listNames = " + listNames.size());
                }
                adapter1.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Метод котрый открывает позицию списка в отдельном окне
    private void setOnClickItem() {
        Log.d("myLogs", "--Class Reports, setOnClickItem--");

        lvData.setOnItemClickListener((parent, view, position, id) -> {
            //Получаем позицию выбранного элемента из списка
            //для отображения элемента с учетом фильтрации используется getItem
            String ids = listBs.get(position);

            Log.d("myLog", "ids = " + ids);

            //переходим в экран конкретного дня
            Intent intent = new Intent(this, Bs.class);
            intent.putExtra("bs", ids);
            startActivity(intent);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @SuppressLint("SdCardPath")
    public void onClickExcel(View view) {
        Log.d("myLogs", "--Class Reports, onClickExcel--");

        listToArray();

        //file1 = new File(Environment.getExternalStoragePublicDirectory (Environment.DIRECTORY_DOCUMENTS), "ManualRP");
        file1 = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "ManualRP");

        boolean wasSuccessful = file1.mkdirs();
        if (!wasSuccessful) {
            Log.d("myLogs", "was not successful - " + file1);
        } else {
            Log.d("myLogs", "was successful - " + file1);
        }

        file = new File(file1, "Report.csv");

        try {
            CSVWriter csvWrite;
            Log.d("myLogs", "Try");

            if (file.exists() && !file.isDirectory()) {
                boolean deleted = file.delete();
                if (!deleted) {
                    Log.d("myLogs", "file was not deleted");
                } else {
                    Log.d("myLogs", "file was deleted");
                }
            }
            csvWrite = new CSVWriter(new FileWriter(file));
            Log.d("myLogs", "Start csvWrite");

            if (dropList.equals(category[0])) {
                Log.d("myLogs", "ВСЕ БС");
                csvWrite.writeNext(titles);
                table = new String[titles.length];
                for (int i = 0; i < toCsvNames.length; i++) {
                    table[0] = toCsvNames[i];
                    table[1] = toCsvlistSKV[i];
                    table[2] = toCsvlistBSS[i];
                    table[3] = toCsvlistRRL[i];
                    table[4] = toCsvlistIBP[i];
                    table[5] = toCsvlistOTHER[i];
                    csvWrite.writeNext(table);
                }
            }
            if (dropList.equals(category[1])) {
                Log.d("myLogs", "СКВ");
                csvWrite.writeNext(titles);
                table = new String[titles.length];
                for (int i = 0; i < toCsvNames.length; i++) {
                    table[0] = toCsvNames[i];
                    table[1] = toCsvComments[i];
                    table[2] = " ";
                    table[3] = " ";
                    table[4] = " ";
                    table[5] = " ";
                    csvWrite.writeNext(table);
                }
            }
            if (dropList.equals(category[2])) {
                Log.d("myLogs", "БС");
                csvWrite.writeNext(titles);
                table = new String[titles.length];
                for (int i = 0; i < toCsvNames.length; i++) {
                    table[0] = toCsvNames[i];
                    table[1] = " ";
                    table[2] = toCsvComments[i];
                    table[3] = " ";
                    table[4] = " ";
                    table[5] = " ";
                    csvWrite.writeNext(table);
                }
            }
            if (dropList.equals(category[3])) {
                Log.d("myLogs", "РРЛ");
                csvWrite.writeNext(titles);
                table = new String[titles.length];
                for (int i = 0; i < toCsvNames.length; i++) {
                    table[0] = toCsvNames[i];
                    table[1] = " ";
                    table[2] = " ";
                    table[3] = toCsvComments[i];
                    table[4] = " ";
                    table[5] = " ";
                    csvWrite.writeNext(table);
                }
            }
            if (dropList.equals(category[4])) {
                Log.d("myLogs", "ИБП");
                csvWrite.writeNext(titles);
                table = new String[titles.length];
                for (int i = 0; i < toCsvNames.length; i++) {
                    table[0] = toCsvNames[i];
                    table[1] = " ";
                    table[2] = " ";
                    table[3] = " ";
                    table[4] = toCsvComments[i];
                    table[5] = " ";
                    csvWrite.writeNext(table);
                }
            }
            if (dropList.equals(category[5])) {
                Log.d("myLogs", "ПРОЧЕЕ");
                csvWrite.writeNext(titles);
                table = new String[titles.length];
                for (int i = 0; i < toCsvNames.length; i++) {
                    table[0] = toCsvNames[i];
                    table[1] = " ";
                    table[2] = " ";
                    table[3] = " ";
                    table[4] = " ";
                    table[5] = toCsvComments[i];
                    csvWrite.writeNext(table);
                }
            }

            Log.d("myLogs", "End csvWrite");
            csvWrite.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("myLogs", "Something wrong!!!");
        }

        sentFile();
    }

    @SuppressLint("QueryPermissionsNeeded")
    public void sentFile() {
        Log.d("myLogs", "--Class Reports, sentFile--");

        Uri imageUri = FileProvider.getUriForFile(
                this,
                "com.example.manualrp.fileprovider", //(use your app signature + ".provider" )
                file);

        Log.d("myLogs", "Uri.fromFile - " + imageUri);

        Intent sharingIntent = new Intent();
        sharingIntent.setAction(Intent.ACTION_SEND);
        sharingIntent.setType("application/csv");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(sharingIntent, "Share file with"));
    }

    public void listToArray() {
        Log.d("myLogs", "--Class Reports, listToArray--");

        toCsvComments = new String[listComments.size()];
        toCsvNames = new String[listNames.size()];
        toCsvlistSKV = new String[listSKV.size()];
        toCsvlistBSS = new String[listBSS.size()];
        toCsvlistRRL = new String[listRRL.size()];
        toCsvlistIBP = new String[listIBP.size()];
        toCsvlistOTHER = new String[listOTHER.size()];

        listComments.toArray(toCsvComments);
        listNames.toArray(toCsvNames);
        listSKV.toArray(toCsvlistSKV);
        listBSS.toArray(toCsvlistBSS);
        listRRL.toArray(toCsvlistRRL);
        listIBP.toArray(toCsvlistIBP);
        listOTHER.toArray(toCsvlistOTHER);

        Log.d("myLogs", "toCsvComments = " + Arrays.toString(toCsvComments));
        Log.d("myLogs", "toCsvNames = " + Arrays.toString(toCsvNames));
        Log.d("myLogs", "toCsvlistSKV = " + Arrays.toString(toCsvlistSKV));
        Log.d("myLogs", "toCsvlistBSS = " + Arrays.toString(toCsvlistBSS));
        Log.d("myLogs", "toCsvlistRRL = " + Arrays.toString(toCsvlistRRL));
        Log.d("myLogs", "toCsvlistIBP = " + Arrays.toString(toCsvlistIBP));
        Log.d("myLogs", "toCsvlistOTHER = " + Arrays.toString(toCsvlistOTHER));
    }

}

