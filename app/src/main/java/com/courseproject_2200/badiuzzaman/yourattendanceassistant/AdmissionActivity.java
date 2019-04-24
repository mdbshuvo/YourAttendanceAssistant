package com.courseproject_2200.badiuzzaman.yourattendanceassistant;
//This one has been detached

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class AdmissionActivity extends AppCompatActivity {

    CustomDatabase database;
    String[] classes;
    ListView listView;
    CustomSingleTextAdapter adapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission);

        getSupportActionBar().setTitle("Admission");

        database=new CustomDatabase(AdmissionActivity.this);
        classes=database.getClasses();

        listView=findViewById(R.id.admission_listview);
        adapter=new CustomSingleTextAdapter(this,R.layout.admission_list_sample,R.id.admission_list_sample_text,classes);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(AdmissionActivity.this,CreateStudentActivity.class);
                intent.putExtra("className",adapter.getItem(position));
                startActivity(intent);
            }
        });

        if(classes.length==0){
            TextView note=findViewById(R.id.note_no_data_admission);
            note.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

        //for(int i=0;i<classes.length;i++) Toast.makeText(this, classes[i], Toast.LENGTH_SHORT).show();

        searchView=findViewById(R.id.search_admission);
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
        if(item.getItemId()==R.id.create_new_class){
            startActivityForResult(new Intent(AdmissionActivity.this,CreateClassActivity.class),1);
        }
        else if(item.getItemId()==R.id.search_classes){
            TextView textView=findViewById(R.id.title_admission);
            textView.setVisibility(View.GONE);
            searchView.setVisibility(View.VISIBLE);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admission_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        classes=database.getClasses();
        adapter=new CustomSingleTextAdapter(this,R.layout.admission_list_sample,R.id.admission_list_sample_text,classes);
        listView.setAdapter(adapter);
        if(adapter.getCount()>0) listView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if(searchView.getVisibility()==View.VISIBLE){
            searchView.setVisibility(View.GONE);
            TextView textView=findViewById(R.id.title_admission);
            textView.setVisibility(View.VISIBLE);
        }
        else super.onBackPressed();
    }
}
