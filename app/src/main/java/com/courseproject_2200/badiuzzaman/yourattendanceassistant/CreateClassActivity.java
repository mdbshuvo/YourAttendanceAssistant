package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import static java.lang.Character.isDigit;

public class CreateClassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);

        getSupportActionBar().setTitle("Class Create");

        ListView listView=findViewById(R.id.create_class_routine_listview);
        final CustomCreateClassAdapter adapter=new CustomCreateClassAdapter(CreateClassActivity.this,R.layout.create_class_routine_sample,getResources().getStringArray(R.array.days_of_week));
        listView.setAdapter(adapter);

        Button createButton=findViewById(R.id.class_create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameET,descriptionET;
                nameET=findViewById(R.id.new_class_name);
                descriptionET=findViewById(R.id.new_class_des);

                String name,des;
                name=nameET.getText().toString();
                des=descriptionET.getText().toString();

                if(name.matches("")){
                    Toast.makeText(CreateClassActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                    return;
                }

                StringBuilder builder=new StringBuilder();

                /*CheckBox[] days=new CheckBox[7];
                int i=0;
                days[i++]=findViewById(R.id.create_class_friday);
                days[i++]=findViewById(R.id.create_class_saturday);
                days[i++]=findViewById(R.id.create_class_sunday);
                days[i++]=findViewById(R.id.create_class_monday);
                days[i++]=findViewById(R.id.create_class_tuesday);
                days[i++]=findViewById(R.id.create_class_wednesday);
                days[i]=findViewById(R.id.create_class_thursday);

                boolean nothingChecked=true;
                for (i=0;i<days.length;i++){
                    if(days[i].isChecked()){
                        builder.append(days[i].getText().toString());
                        nothingChecked=false;
                    }
                }

                onDays=builder.toString();

                //Toast.makeText(CreateClassActivity.this, onDays, Toast.LENGTH_SHORT).show();
                
                if(nothingChecked){
                    Toast.makeText(CreateClassActivity.this, "Please select on days ", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                String routine=adapter.getRoutine();

                if(routine==null){
                    Toast.makeText(CreateClassActivity.this, "Please create a routine", Toast.LENGTH_SHORT).show();
                    return;
                }

                String[] splittedRoutine=routine.split(" ");

                for(int i=0;i<splittedRoutine.length;i+=3){
                    if(!isDigit(splittedRoutine[i+1].charAt(0)) || !isDigit(splittedRoutine[i+2].charAt(0))){
                        Toast.makeText(CreateClassActivity.this, "Please select both start and end", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }



                CustomDatabase database=new CustomDatabase(CreateClassActivity.this);
                database.createClass(name,des,routine);

                Toast.makeText(CreateClassActivity.this, "New class "+name+" is created", Toast.LENGTH_SHORT).show();

                setResult(1,new Intent(CreateClassActivity.this,AdmissionActivity.class));
                finish();
            }
        });
    }
}
