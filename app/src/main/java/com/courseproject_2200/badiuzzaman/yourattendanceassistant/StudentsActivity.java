package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class StudentsActivity extends AppCompatActivity {

    CustomDatabase database;
    Student[] students;
    String className;
    ListView listView;
    CustomDoubleTextAdapter adapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        getSupportActionBar().setTitle("Students");

        Bundle bundle=getIntent().getExtras();
        className=bundle.getString("className");

        database=new CustomDatabase(StudentsActivity.this);
        students=database.getStudents(className);
        //Toast.makeText(this, "I am here", Toast.LENGTH_SHORT).show();
        listView=findViewById(R.id.student_list_listview);
        adapter=new CustomDoubleTextAdapter(this,R.layout.student_list_sample,students);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(StudentsActivity.this, adapter.getItem(position).name, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(StudentsActivity.this,StudentDetailsActivity.class);
                intent.putExtra("className",className);
                intent.putExtra("studentRoll",adapter.getItem(position).roll);
                startActivityForResult(intent,1);
            }
        });

        if(students.length==0){
            Toast.makeText(this, "This class is empty. Please admit some students first.", Toast.LENGTH_SHORT).show();
            finish();
        }

        searchView=findViewById(R.id.search_student_list);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.search_classes){
            TextView textView=findViewById(R.id.title_student_list);
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
            TextView textView=findViewById(R.id.title_student_list);
            textView.setVisibility(View.VISIBLE);
        }
        else super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        students=database.getStudents(className);
        adapter=new CustomDoubleTextAdapter(this,R.layout.student_list_sample,students);
        listView.setAdapter(adapter);
    }
}
