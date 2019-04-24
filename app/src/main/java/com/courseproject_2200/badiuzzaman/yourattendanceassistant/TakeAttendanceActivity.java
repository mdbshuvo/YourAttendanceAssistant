package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TakeAttendanceActivity extends AppCompatActivity {

    CustomDatabase database;
    ArrayList<String> classesList=new ArrayList<>();
    String[] classes;
    ListView listView;
    CustomSingleTextAdapter adapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        getSupportActionBar().setTitle("Take Attendance");

        database=new CustomDatabase(TakeAttendanceActivity.this);
        classes= database.getClasses();
        for (int i=0;i<classes.length;i++){
            if(!isNotTimeOfClass(classes[i])){
                classesList.add(classes[i]);
            }
        }

        listView=findViewById(R.id.view_listview);


        if(classesList.size()==0){
            TextView note=findViewById(R.id.note_no_data_view);
            note.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            classes= classesList.toArray(new String[classesList.size()]);

            adapter=new CustomSingleTextAdapter(this,R.layout.admission_list_sample,R.id.admission_list_sample_text,classes);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    /*String routine=database.getRoutine(adapter.getItem(position));

                    if(isNotTimeOfClass(routine)){
                        Toast.makeText(TakeAttendanceActivity.this, "It is not the time of this class.", Toast.LENGTH_SHORT).show();
                        return;
                    }*/

                    if(database.isTakenTodaysAttendance(adapter.getItem(position))){
                        Toast.makeText(TakeAttendanceActivity.this, "This class attendance is already taken", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intent=new Intent(TakeAttendanceActivity.this,StudentAttendanceActivity.class);
                    intent.putExtra("className",adapter.getItem(position));
                    startActivity(intent);
                }
            });

            //for(int i=0;i<classes.length;i++) Toast.makeText(this, classes[i], Toast.LENGTH_SHORT).show();
        }

        searchView=findViewById(R.id.search_view);
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
    }

    private boolean isNotTimeOfClass(String className) {
        String routine=database.getRoutine(className);

        String[] routine_splitted=routine.split(" ");
        String todayDay=TimeGenerator.getDay();

        for(int i=0;i<routine_splitted.length;i+=3){
            if(routine_splitted[i].matches(todayDay)){
                String[] stTime=routine_splitted[i+1].split(":");
                String[] enTime=routine_splitted[i+2].split(":");

                int stHour=Integer.parseInt(stTime[0]),stMin=Integer.parseInt(stTime[1]);
                int enHour=Integer.parseInt(enTime[0]),enMin=Integer.parseInt(enTime[1]);

                int curHour=TimeGenerator.getHour(),curMin=TimeGenerator.getMinute();

                if(stHour<curHour && curHour<enHour){
                    return false;
                }
                else if(stHour==curHour && stMin<=curMin){
                    return false;
                }
                else if(enHour==curHour && (enMin+10)>=curMin){     //10 minutes more given
                    if(curMin>enMin) {
                        Toast.makeText(this, "You are late", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.search_classes){
            TextView textView=findViewById(R.id.title_view);
            textView.setVisibility(View.GONE);
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
            TextView textView=findViewById(R.id.title_view);
            textView.setVisibility(View.VISIBLE);
        }
        else super.onBackPressed();
    }
}
