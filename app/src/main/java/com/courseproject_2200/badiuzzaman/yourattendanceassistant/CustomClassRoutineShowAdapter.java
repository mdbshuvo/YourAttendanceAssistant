package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomClassRoutineShowAdapter extends BaseAdapter {

    private Context context;
    private int parent;
    private Routines[] routines;

    public CustomClassRoutineShowAdapter(Context context, int parent, Routines[] routines) {
        this.context=context;
        this.parent=parent;
        this.routines=routines;
    }

    @Override
    public int getCount() {
        return routines.length;
    }

    @Override
    public Object getItem(int position) {
        return routines[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView==null){
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(parent,viewGroup,false);
        }

        TextView day,start,end;
        day=convertView.findViewById(R.id.routine_day);
        start=convertView.findViewById(R.id.routine_class_start);
        end=convertView.findViewById(R.id.routine_class_end);

        day.setText(routines[position].day);
        start.setText(routines[position].start);
        end.setText(routines[position].end);

        return convertView;
    }
}

class Routines{
    String day,start,end;
}