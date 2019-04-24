package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ClassDetailsActivity extends AppCompatActivity {

    Routines[] routines;
    private String className;
    private Student[] students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);

        getSupportActionBar().setTitle("Class Details");

        Bundle bundle=getIntent().getExtras();
        className=bundle.getString("className");

        CustomDatabase database=new CustomDatabase(this);
        String routineString=database.getRoutine(className);
        String[] routineSplitted=routineString.split(" ");

        routines=new Routines[routineSplitted.length/3];

        for(int i=0;i<routineSplitted.length;i+=3){
            routines[i/3]=new Routines();

            routines[i/3].day=routineSplitted[i];
            routines[i/3].start=routineSplitted[i+1];
            routines[i/3].end=routineSplitted[i+2];
        }

        ListView routineListView=findViewById(R.id.class_routine_listview);
        CustomClassRoutineShowAdapter adapter=new CustomClassRoutineShowAdapter(ClassDetailsActivity.this,R.layout.class_routine_list_sample,routines);
        routineListView.setAdapter(adapter);

        students=database.getStudents(className);

        ListView studentsListview=findViewById(R.id.class_student_listview);
        CustomClassStudentsEditAdapter stAdapter=new CustomClassStudentsEditAdapter(ClassDetailsActivity.this,R.layout.class_student_edit_list_sample,students,className,false);
        studentsListview.setAdapter(stAdapter);

        Button editButton=findViewById(R.id.edit_students_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView studentsListview=findViewById(R.id.class_student_listview);
                CustomClassStudentsEditAdapter adapter=new CustomClassStudentsEditAdapter(ClassDetailsActivity.this,R.layout.class_student_edit_list_sample,students,className,true);
                studentsListview.setAdapter(adapter);
            }
        });

        TextView name,number,nothing;

        name=findViewById(R.id.class_name_text);
        number=findViewById(R.id.class_number_text);
        nothing=findViewById(R.id.class_nothing_text);

        name.setText(className);
        number.setText(String.valueOf(students.length));

        if(students.length==0) {
            editButton.setVisibility(View.GONE);
            nothing.setVisibility(View.VISIBLE);
        }

        String[] attendanceDates=database.getAttendanceDates(className);
        if (attendanceDates!=null){
            ListView listView=findViewById(R.id.class_attendance_sheet_listview);
            final CustomSingleTextAdapter adapter2=new CustomSingleTextAdapter(ClassDetailsActivity.this,R.layout.admission_list_sample,R.id.admission_list_sample_text,attendanceDates);
            listView.setAdapter(adapter2);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(ClassDetailsActivity.this,AttendanceSheetActivity.class);
                    intent.putExtra("className",className);
                    intent.putExtra("date",adapter2.getItem(position));
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.class_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.delete_class_menu_item){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Warning");
            builder.setIcon(R.drawable.ic_warning_black_24dp);
            builder.setMessage("Are you sure you want to delete this class? This process cannot be undone!");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CustomDatabase database=new CustomDatabase(ClassDetailsActivity.this);
                    database.deleteClass(className);

                    Toast.makeText(ClassDetailsActivity.this, "Deleted class : "+className, Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(ClassDetailsActivity.this,ViewStudentsActivity.class);
                    setResult(1,intent);
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            });

            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }

        return true;
    }
}

