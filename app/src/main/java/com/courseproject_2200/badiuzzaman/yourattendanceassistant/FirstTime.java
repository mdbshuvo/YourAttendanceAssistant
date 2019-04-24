package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FirstTime extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time);

        Button loginButton,signupButton,continueButton;

        loginButton=findViewById(R.id.login_loginbutton);
        signupButton=findViewById(R.id.login_signupbutton);
        continueButton=findViewById(R.id.login_continuebutton);

        loginButton.setOnClickListener(this);
        signupButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //Toast.makeText(this, "onClick called", Toast.LENGTH_SHORT).show();
        
        if(v.getId()==R.id.login_loginbutton){
            startActivityForResult(new Intent(FirstTime.this,LoginActivity.class),1);
        }
        else if(v.getId()==R.id.login_signupbutton){
            startActivityForResult(new Intent(FirstTime.this,SignUpActivity.class),1);
        }
        else if(v.getId()==R.id.login_continuebutton){
            Toast.makeText(this, "continue clicked", Toast.LENGTH_SHORT).show();
            
            AlertDialog.Builder builder=new AlertDialog.Builder(FirstTime.this);
            builder.setTitle("Note");
            builder.setMessage("This account is temporary and not syncable. Any lost of data is irrecoverable unless you sign in!");
            builder.setCancelable(false);
            builder.setIcon(R.drawable.ic_warning_black_24dp);
            builder.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(new Intent(FirstTime.this,PartialSignUpActivity.class),1);
                }
            });

            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==1){
            Toast.makeText(this, "onActivityResult caled", Toast.LENGTH_SHORT).show();
            finish();
        }
        SharedPreferences sharedPreferences=getSharedPreferences("miscellenous", Context.MODE_PRIVATE);
        if(sharedPreferences.contains("isFirstRun")){
            if(sharedPreferences.getString("isFirstRun","nothing").matches("false")){
                finish();
            }
        }
    }
}
