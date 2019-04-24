package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class CustomDoubleAttendanceTextAdapter extends BaseAdapter {

    private Context context;
    int resource;
    private StudentAttendance[] students;
    private ArrayList<StudentAttendance> mOriginalValues;
    private List<StudentAttendance> mObjects;
    private String present="present",absent="absent";


    public CustomDoubleAttendanceTextAdapter(Context context, int resources, Student[] students) {
        this.context=context;
        this.resource=resources;
        this.students=new StudentAttendance[students.length];

        for(int i=0;i<students.length;i++){
            this.students[i]=new StudentAttendance(students[i]);
        }

        mObjects= Arrays.asList(this.students);
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    public StudentAttendance getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(resource,parent,false);
        }

        TextView nameText,rollText;

        nameText=convertView.findViewById(R.id.student_list_name);
        rollText=convertView.findViewById(R.id.student_list_roll);

        final Button presentButton,absentButton;

        presentButton=convertView.findViewById(R.id.present_button);
        absentButton=convertView.findViewById(R.id.absent_button);

        nameText.setText(mObjects.get(position).name);
        rollText.setText(String.valueOf(mObjects.get(position).roll));


        presentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentButton.setBackgroundColor(Color.GREEN);
                absentButton.setBackgroundColor(Color.GRAY);
                mObjects.get(position).status=present;
                mObjects.get(position).takenAttendance=true;

                Toast.makeText(context, "Roll number "+mObjects.get(position).roll+" is present", Toast.LENGTH_SHORT).show();
            }
        });

        absentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentButton.setBackgroundColor(Color.GRAY);
                absentButton.setBackgroundColor(Color.RED);
                mObjects.get(position).status=absent;
                mObjects.get(position).takenAttendance=true;

                Toast.makeText(context, "Roll number "+mObjects.get(position).roll+" is absent", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    public String[] getAttendanceSheet(){
        String[] attendanceSheet=new String[mObjects.size()];

        if(mOriginalValues!=null && mOriginalValues.size()!=mObjects.size()){
            Toast.makeText(context, "Please clear the search filter first", Toast.LENGTH_SHORT).show();
            return null;
        }

        for(StudentAttendance item : mObjects){
            if(!item.takenAttendance){
                Toast.makeText(context, "Please complete taking attendance of all students\n (attendance of roll no "+item.roll+" is not taken)", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        for(int i=0;i<mObjects.size();i++){
            attendanceSheet[i]=mObjects.get(i).status;
        }

        return attendanceSheet;
    }


    public void performFiltering(String prefix) {
        //Toast.makeText(context, "Performing filtering", Toast.LENGTH_SHORT).show();

        if (mOriginalValues == null) {
            mOriginalValues = new ArrayList<StudentAttendance>(mObjects);
        }

        if (prefix == null || prefix.length() == 0) {
            final ArrayList<StudentAttendance> list;
            list = new ArrayList<StudentAttendance>(mOriginalValues);
            mObjects = list;
        } else {
            final String prefixString = prefix.toLowerCase();

            final ArrayList<StudentAttendance> values;
            values = new ArrayList<StudentAttendance>(mOriginalValues);

            final int count = values.size();
            final ArrayList<StudentAttendance> newValues = new ArrayList<StudentAttendance>();

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

class StudentAttendance extends Student{
    String status;
    Button presentButton,absentButton,leaveButton;
    boolean takenAttendance;

    StudentAttendance(){}
    StudentAttendance(Student student){
        this.roll=student.roll;
        this.name=student.name;
    }
}