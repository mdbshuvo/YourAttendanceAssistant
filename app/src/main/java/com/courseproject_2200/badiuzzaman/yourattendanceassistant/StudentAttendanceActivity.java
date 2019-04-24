package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class StudentAttendanceActivity extends AppCompatActivity {

    CustomDatabase database;
    Student[] students;
    String className;
    ListView listView;
    CustomDoubleAttendanceTextAdapter adapter;
    SearchView searchView;
    Button saveAttendanceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);

        getSupportActionBar().setTitle("Take Attendance");

        Bundle bundle=getIntent().getExtras();
        className=bundle.getString("className");

        database=new CustomDatabase(StudentAttendanceActivity.this);
        students=database.getStudents(className);
        //Toast.makeText(this, "I am here", Toast.LENGTH_SHORT).show();
        listView=findViewById(R.id.student_attendance_list_listview);
        adapter=new CustomDoubleAttendanceTextAdapter(this,R.layout.student_attendance_list_sample,students);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(StudentAttendanceActivity.this, adapter.getItem(position).name, Toast.LENGTH_SHORT).show();
            }
        });

        if(students.length==0){
            Toast.makeText(this, "Please admit some students first", Toast.LENGTH_SHORT).show();
            finish();
        }

        searchView=findViewById(R.id.search_student_attendance_list);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.performFiltering(newText);
                return false;
            }
        });

        saveAttendanceButton=findViewById(R.id.save_attendance_button);
        saveAttendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] attendanceSheet=adapter.getAttendanceSheet();

                //Toast.makeText(StudentAttendanceActivity.this, TimeGenerator.getTimeString12(), Toast.LENGTH_SHORT).show();
                if(attendanceSheet!=null){
                    CustomDatabase database=new CustomDatabase(StudentAttendanceActivity.this);
                    database.setToadaysAttendance(className,attendanceSheet);

                    Toast.makeText(StudentAttendanceActivity.this, "Attendance Sheet saved", Toast.LENGTH_SHORT).show();
                    finish();
                }

                return;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.search_classes){
            TextView textView=findViewById(R.id.title_student_attendance_list);
            textView.setVisibility(View.GONE);
            saveAttendanceButton.setVisibility(View.GONE);
            searchView.setVisibility(View.VISIBLE);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if(searchView.getVisibility()==View.VISIBLE){
            searchView.setVisibility(View.GONE);
            TextView textView=findViewById(R.id.title_student_attendance_list);
            textView.setVisibility(View.VISIBLE);
            saveAttendanceButton.setVisibility(View.VISIBLE);
        }
        else super.onBackPressed();
    }
}
