package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PartialSignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partial_sign_up);

        getSupportActionBar().setTitle("Partial Sign Up");

        Button signupButton=findViewById(R.id.partial_signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText fnamet,lnamet,emailt;

                fnamet=findViewById(R.id.fnamet);
                lnamet=findViewById(R.id.lnamet);
                emailt=findViewById(R.id.emailt);

                String fname=fnamet.getText().toString(),lname=lnamet.getText().toString(),email=emailt.getText().toString();

                if(fname.matches("") || lname.matches("")){
                    Toast.makeText(PartialSignUpActivity.this, "Please fill up the requied fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                CustomDatabase customDatabase=new CustomDatabase(PartialSignUpActivity.this);
                customDatabase.insertPartial(fname,lname,email);

                Toast.makeText(PartialSignUpActivity.this, "Temporary sign up successfull", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PartialSignUpActivity.this,HomeActivity.class));
                setResult(1,new Intent(PartialSignUpActivity.this,FirstTime.class));
                finish();
            }
        });
    }
}
