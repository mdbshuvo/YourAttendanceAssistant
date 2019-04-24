package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;


public class HomeActivity extends AppCompatActivity {

    boolean confirmExit=false;
    CustomDatabase database;
    private StorageReference mStorageRef;
    private String _email;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        database=new CustomDatabase(HomeActivity.this);
        firebaseAuth = FirebaseAuth.getInstance();

        SharedPreferences sharedPreferences=getSharedPreferences("miscellenous", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putString("isFirstRun","false");
        editor.commit();

        int[] icons={R.drawable.ic_school_black_24dp,R.drawable.ic_group_black_24dp,R.drawable.ic_assignment_black_24dp};
        String[] label=getResources().getStringArray(R.array.homepage_label);

        GridView gridView=findViewById(R.id.gridview);
        CustomHomeGridAdapter adapter=new CustomHomeGridAdapter(HomeActivity.this,icons,label);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    startActivity(new Intent(HomeActivity.this,CreateStudentActivity.class));
                }
                else if(position==1){
                    startActivity(new Intent(HomeActivity.this,ViewStudentsActivity.class));
                }
                else if(position==2){
                    startActivity(new Intent(HomeActivity.this,TakeAttendanceActivity.class));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(!confirmExit){
            confirmExit=true;

            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

            Thread thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    confirmExit=false;
                }
            });

            thread.start();
        }
        else {
            if(firebaseAuth.getCurrentUser()==null) {
                finish();
                return;
            }

            if(isNetworkAvailable()){
                sync();
            }else {
                AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(HomeActivity.this);
                alertDialogBuilder.setTitle("Warning");
                alertDialogBuilder.setMessage("This session data will not be synced online unless you are connected to the network.Are you sure not to sync your data now?");
                alertDialogBuilder.setPositiveButton("I will sync later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(HomeActivity.this, "Best of luck!!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                alertDialogBuilder.setNegativeButton("I want to sync now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(HomeActivity.this, "Then connect me to the network", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alertDialog=alertDialogBuilder.create();
                alertDialog.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!(firebaseAuth.getCurrentUser()==null)){
            getMenuInflater().inflate(R.menu.home_menu_logged_in,menu);
            TextView textView = findViewById(R.id.home_username);
            textView.setText(database.getUserName());
        }else {
            getMenuInflater().inflate(R.menu.home_menu,menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.home_log_in){
            if(firebaseAuth.getCurrentUser()!=null){
                Toast.makeText(this, "Sorry this activity has not been added yet", Toast.LENGTH_SHORT).show();
            }else {
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
            }

        }else if(item.getItemId()==R.id.home_about){
            startActivity(new Intent(HomeActivity.this,AboutUsActivity.class));
        }else if(item.getItemId()==R.id.home_log_out) {

            if (isNetworkAvailable()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Warning");
                builder.setMessage("Do you want to log out now??");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        _email = database.getEmail();
                        FirebaseStorage mStorageRef_ = FirebaseStorage.getInstance();
                        mStorageRef = mStorageRef_.getReference();
                        StorageReference riversRef = mStorageRef.child(_email + "_Admin.db");
                        Uri file = Uri.fromFile(new File("/data/data/com.courseproject_2200.badiuzzaman.yourattendanceassistant/databases/Admin.db"));

                        final ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
                        progressDialog.show();
                        progressDialog.setCancelable(false);

                        riversRef.putFile(file)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // Get a URL to the uploaded content
                                        Uri uploadUrl = taskSnapshot.getUploadSessionUri();

                                        firebaseAuth.signOut();
                                        database.clearAll();
                                        startActivity(new Intent(HomeActivity.this, FirstTime.class));

                                        SharedPreferences sharedPreferences = getSharedPreferences("miscellenous", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("isFirstRun", "true");
                                        editor.commit();
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle unsuccessful uploads
                                        // ...
                                        Toast.makeText(HomeActivity.this, "Sync failed : " + exception, Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        //calculating progress percentage
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                        //displaying percentage in progress dialog
                                        progressDialog.setMessage("Uploading current status " + ((int) progress) + "%...");
                                    }
                                });

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(HomeActivity.this, "Ok then :)", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else{
                Toast.makeText(this, "You should be connected to the internet to log out", Toast.LENGTH_SHORT).show();
            }
        }

        return true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void sync(){
        _email=database.getEmail();
        FirebaseStorage mStorageRef_ = FirebaseStorage.getInstance();
        mStorageRef = mStorageRef_.getReference();
        StorageReference riversRef = mStorageRef.child(_email+"_Admin.db");
        Uri file = Uri.fromFile(new File("/data/data/com.courseproject_2200.badiuzzaman.yourattendanceassistant/databases/Admin.db"));

        final ProgressDialog progressDialog=new ProgressDialog(HomeActivity.this);
        progressDialog.show();
        progressDialog.setCancelable(false);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri uploadUrl = taskSnapshot.getUploadSessionUri();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(HomeActivity.this, "Sync failed : "+exception, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    //calculating progress percentage
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                    //displaying percentage in progress dialog
                    progressDialog.setMessage("Uploading current status " + ((int) progress) + "%...");
                }
        });
    }
}