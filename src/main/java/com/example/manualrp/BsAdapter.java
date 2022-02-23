package com.example.manualrp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BsAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    List<String> label;
    List<DBForm> objects;
    List<String> notes;
    List<Boolean> c;

    TextView comm;
    Boolean a;
    String name;

    BsAdapter(Context context, List<String> names, List<DBForm> listBs, List<Boolean> flag) {
        ctx = context;
        label = names;
        objects = listBs;
        c = flag;

        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public int getCount() {
        return label.size();
    }

    public Object getItem(int position) {
        return label.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("myLogs", "--Class BsAdapter start--");

        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_list_bs, parent, false);
        }

        name = label.get(position);

        // заполняем View в пункте списка данными из DBForm
        comm = (TextView) view.findViewById(R.id.comment);
        comm.setText(name);

        a = c.get(position);

        Log.d("myLogs", "a - " + a);


        if(a){
            view.setBackgroundColor(Color.argb(32, 106, 179, 188));
        } else {
            view.setBackgroundColor(Color.argb(100, 76, 175, 80));
        }

        return view;
    }

    public Filter getFilter() {
        Filter filter = new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                label = (List<String>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<String> FilteredArrList = new ArrayList<String>();

                if (notes == null) {
                    notes = new ArrayList<String>(label); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = notes.size();
                    results.values = notes;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < notes.size(); i++) {
                        String data = notes.get(i);
                        if (data.toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(data);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;    }

}
