package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class LoadingScreen extends AppCompatActivity {

    boolean isFirstRun=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final ProgressBar progressBar=findViewById(R.id.loading_progress);
        SharedPreferences sharedPreferences=getSharedPreferences("miscellenous", Context.MODE_PRIVATE);
        if(sharedPreferences.contains("isFirstRun")){
            if(sharedPreferences.getString("isFirstRun","nothing").matches("false")){

                      isFirstRun=false;
            }
        }

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                for(int progress=0;progress<=100;progress+=10){
                    progressBar.setProgress(progress);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Intent intent;

                if(isFirstRun){
                    intent=new Intent(LoadingScreen.this,FirstTime.class);
                }
                else{
                    intent=new Intent(LoadingScreen.this,HomeActivity.class);
                }

                startActivity(intent);
                finish();
            }
        });
        thread.start();
    }
}
