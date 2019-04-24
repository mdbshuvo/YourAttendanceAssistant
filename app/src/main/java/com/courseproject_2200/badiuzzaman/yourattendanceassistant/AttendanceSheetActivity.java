package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class AttendanceSheetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_sheet);

        getSupportActionBar().setTitle("Attendance Sheet");

        Bundle bundle=getIntent().getExtras();
        String className=bundle.getString("className");
        String date=bundle.getString("date");

        TextView name=findViewById(R.id.attendance_sheet_class_name);
        name.setText(className);
        TextView intro=findViewById(R.id.class_intro);
        intro.setText(intro.getText()+date);

        CustomDatabase database=new CustomDatabase(AttendanceSheetActivity.this);

        StudentStatuses[] studentsStatuses=database.getToadaysAttendance(className,date);

        ListView routineListView=findViewById(R.id.class_routine_listview);
        CustomClassAttendanceSheetShowAdapter adapter=new CustomClassAttendanceSheetShowAdapter(AttendanceSheetActivity.this,R.layout.class_routine_list_sample,studentsStatuses);
        routineListView.setAdapter(adapter);
    }
}


