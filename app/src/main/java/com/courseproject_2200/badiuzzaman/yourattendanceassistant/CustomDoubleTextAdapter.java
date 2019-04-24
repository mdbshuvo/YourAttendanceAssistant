package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

class CustomDoubleTextAdapter extends BaseAdapter {

    private Context context;
    int resource;
    private Student[] students;
    private ArrayList<Student> mOriginalValues;
    private List<Student> mObjects;

    public CustomDoubleTextAdapter(Context context, int resources, Student[] students) {
        this.context=context;
        this.resource=resources;
        this.students= (Student[]) students;
        mObjects= Arrays.asList(this.students);
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    public Student getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(resource,parent,false);
        }

        TextView nameText,rollText;

        nameText=convertView.findViewById(R.id.student_list_name);
        rollText=convertView.findViewById(R.id.student_list_roll);

        nameText.setText(mObjects.get(position).name);
        rollText.setText(String.valueOf(mObjects.get(position).roll));

        return convertView;
    }


    public void performFiltering(String prefix) {
        //Toast.makeText(context, "Performing filtering", Toast.LENGTH_SHORT).show();

        if (mOriginalValues == null) {
            mOriginalValues = new ArrayList<Student>(mObjects);
        }

        if (prefix == null || prefix.length() == 0) {
            final ArrayList<Student> list;
            list = new ArrayList<Student>(mOriginalValues);
            mObjects = list;
        } else {
            final String prefixString = prefix.toLowerCase();

            final ArrayList<Student> values;
            values = new ArrayList<Student>(mOriginalValues);

            final int count = values.size();
            final ArrayList<Student> newValues = new ArrayList<Student>();

            for (int i = 0; i < count; i++) {
                final String value = values.get(i).name;
                final int valueRoll = values.get(i).roll;
                final String valueText = value.toLowerCase();
                final String valueRollText = String.valueOf(valueRoll);

                // First match against the whole, non-splitted value
                if (valueText.startsWith(prefixString)) {
                    newValues.add(values.get(i));
                } else if (valueRollText.startsWith(prefixString)){
                    newValues.add(values.get(i));
                } else {
                    final String[] words = valueText.split(" ");
                    for (String word : words) {
                        if (word.startsWith(prefixString)) {
                            newValues.add(values.get(i));
                            break;
                        }
                    }
                }
            }

            mObjects=newValues;

            /*for(int i=0;i<newValues.size();i++)
                Toast.makeText(context, mObjects.get(i).name, Toast.LENGTH_SHORT).show();*/
        }

        notifyDataSetChanged();
    }

}

