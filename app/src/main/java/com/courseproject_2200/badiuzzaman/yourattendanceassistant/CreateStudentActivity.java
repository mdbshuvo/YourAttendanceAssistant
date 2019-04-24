package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateStudentActivity extends AppCompatActivity {

    String className=null;
    String[] classNames;
    Button addStudentButton;
    Spinner spinner;
    CustomDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_student);

        getSupportActionBar().setTitle("Create Student");

        /*Bundle bundle=getIntent().getExtras();
        className=bundle.getString("className");
        //Toast.makeText(this, className, Toast.LENGTH_SHORT).show();
        TextView intro=findViewById(R.id.class_intro);
        intro.setText(intro.getText()+className);*/

        database=new CustomDatabase(CreateStudentActivity.this);
        classNames=database.getClasses();

        spinner=findViewById(R.id.class_name_spinner);
        CustomSingleTextAdapter adapter=new CustomSingleTextAdapter(CreateStudentActivity.this,R.layout.admission_list_sample,R.id.admission_list_sample_text,classNames);
        spinner.setAdapter(adapter);

        addStudentButton=findViewById(R.id.add_student_button);
        addStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameText,rollText;
                nameText=findViewById(R.id.name_box);
                rollText=findViewById(R.id.roll_box);

                String name,rollString;

                name=nameText.getText().toString();
                rollString=rollText.getText().toString();

                if(name.matches("") || rollString.matches("")){
                    Toast.makeText(CreateStudentActivity.this, "Please enter something first", Toast.LENGTH_SHORT).show();
                    return;
                }

                className=spinner.getSelectedItem().toString();

                if(className==null){
                    Toast.makeText(CreateStudentActivity.this, "Please select a class", Toast.LENGTH_SHORT).show();
                    return;
                }

                int roll=Integer.parseInt(rollString);

                CustomDatabase database=new CustomDatabase(CreateStudentActivity.this);
                if(database.isRollDuplicate(className,roll)){
                    Toast.makeText(CreateStudentActivity.this, "Please enter another roll", Toast.LENGTH_SHORT).show();
                    return;
                }



                database.insertStudent(className,name,roll);

                nameText.setText("");
                rollText.setText("");

                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.create_new_class){
            startActivityForResult(new Intent(CreateStudentActivity.this,CreateClassActivity.class),1);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_admission_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        classNames=database.getClasses();

        spinner=findViewById(R.id.class_name_spinner);
        CustomSingleTextAdapter adapter=new CustomSingleTextAdapter(CreateStudentActivity.this,R.layout.admission_list_sample,R.id.admission_list_sample_text,classNames);
        spinner.setAdapter(adapter);
    }
}
