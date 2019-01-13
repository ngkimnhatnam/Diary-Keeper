package com.meomoc.toolbar_optionmenu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final ArrayList<String> business;
    private final ArrayList<String> time;
    private final ArrayList<String> content;

    public MyAdapter(MainActivity _context, ArrayList<String> _business, ArrayList<String> _time, ArrayList<String> _content){
        super(_context, R.layout.custom_list, _business);
        this.context = _context;
        this.business = _business;
        this.time = _time;
        this.content = _content;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.custom_list, parent, false);
        TextView businessText = (TextView) rowView.findViewById(R.id.business);
        TextView timeText = (TextView) rowView.findViewById(R.id.time);

        businessText.setText(business.get(position));
        timeText.setText(time.get(position));

        return rowView;
    }
}
