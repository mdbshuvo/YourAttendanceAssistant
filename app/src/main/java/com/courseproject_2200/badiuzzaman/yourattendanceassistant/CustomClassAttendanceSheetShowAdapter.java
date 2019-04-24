package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class CustomClassAttendanceSheetShowAdapter extends BaseAdapter{private Context context;
    private int parent;
    private StudentStatuses[] status;

    public CustomClassAttendanceSheetShowAdapter(Context context, int parent, StudentStatuses[] status) {
        this.context=context;
        this.parent=parent;
        this.status =status;
    }

    @Override
    public int getCount() {
        return status.length;
    }

    @Override
    public Object getItem(int position) {
        return status[position];
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

        TextView roll,name,status;
        roll=convertView.findViewById(R.id.routine_day);
        name=convertView.findViewById(R.id.routine_class_start);
        status=convertView.findViewById(R.id.routine_class_end);

        roll.setText(String.valueOf(this.status[position].roll));
        name.setText(this.status[position].name);
        status.setText(this.status[position].status);

        return convertView;
    }

}

class StudentStatuses extends Student{
    String status;
}
