package com.example.manualrp;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    List<DBForm> objects;

    TextView comment;
    TextView date;
    TextView userName;
    TextView category;
    //boolean c;

    MyAdapter(Context context, List<DBForm> comments) {

        ctx = context;
        objects = comments;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("myLogs", "--Class MyAdapter start--");

        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        DBForm db = getDBForm(position);

        // заполняем View в пункте списка данными из DBForm
        comment = (TextView) view.findViewById(R.id.comment);
        date = (TextView) view.findViewById(R.id.date);
        userName = (TextView) view.findViewById(R.id.userName);
        category = (TextView) view.findViewById(R.id.category);

        //c = db.checkText;

        date.setText(db.date);
        userName.setText(db.userName);
        category.setText(db.category);
        comment.setText(db.comment);

        /*
        if (!c) {
            comment.setText(db.comment);
        } else {
            comment.setText(db.comment);
            comment.setPaintFlags(comment.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }*/

        return view;
    }

    DBForm getDBForm(int position) {
        return ((DBForm) getItem(position));
    }
}
