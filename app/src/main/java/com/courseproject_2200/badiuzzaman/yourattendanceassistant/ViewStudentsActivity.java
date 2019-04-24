package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

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

public class ViewStudentsActivity extends AppCompatActivity {

    CustomDatabase database;
    String[] classes;
    ListView listView;
    CustomSingleTextAdapter adapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);

        getSupportActionBar().setTitle("View Students");

        database=new CustomDatabase(ViewStudentsActivity.this);
        classes=database.getClasses();

        listView=findViewById(R.id.view_listview);
        adapter=new CustomSingleTextAdapter(this,R.layout.admission_list_sample,R.id.admission_list_sample_text,classes);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(ViewStudentsActivity.this,StudentsActivity.class);
                intent.putExtra("className",adapter.getItem(position));
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent(ViewStudentsActivity.this,ClassDetailsActivity.class);
                intent.putExtra("className",adapter.getItem(position));
                startActivityForResult(intent,1);

                return true;
            }
        });

        if(classes.length==0){
            TextView note=findViewById(R.id.note_no_data_view);
            note.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

        //for(int i=0;i<classes.length;i++) Toast.makeText(this, classes[i], Toast.LENGTH_SHORT).show();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        classes=database.getClasses();
        adapter=new CustomSingleTextAdapter(this,R.layout.admission_list_sample,R.id.admission_list_sample_text,classes);
        listView.setAdapter(adapter);
    }
}
