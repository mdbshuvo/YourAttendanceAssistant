package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        user=FirebaseAuth.getInstance();

        getSupportActionBar().setTitle("Sign Up");

        CheckBox acceptButton=findViewById(R.id.acceptbutton);
        final Button signupButton=findViewById(R.id.signupbutton);

        signupButton.setEnabled(false);

        acceptButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) signupButton.setEnabled(true);
                else signupButton.setEnabled(false);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView userNameText,passwordText,rePasswordText,fNameText,sNameText,emailText,institutionText,designationText;

                userNameText=findViewById(R.id.username_id);
                passwordText=findViewById(R.id.password_id);
                rePasswordText=findViewById(R.id.re_enter_password_id);
                fNameText=findViewById(R.id.first_name_id);
                sNameText=findViewById(R.id.second_name_id);
                emailText=findViewById(R.id.email_id);
                institutionText=findViewById(R.id.institution_id);
                designationText=findViewById(R.id.designation_id);


                String userNameStr,passwordStr,rePasswordStr,fNameStr,sNameStr,emailStr,institutionStr,designationStr;

                userNameStr=userNameText.getText().toString();
                passwordStr=passwordText.getText().toString();
                rePasswordStr=rePasswordText.getText().toString();
                fNameStr=fNameText.getText().toString();
                sNameStr=sNameText.getText().toString();
                emailStr=emailText.getText().toString();
                institutionStr=institutionText.getText().toString();
                designationStr=designationText.getText().toString();

                if(userNameStr.matches("")||passwordStr.matches("")||rePasswordStr.matches("")||fNameStr.matches("")||sNameStr.matches("")||emailStr.matches("")){
                    Toast.makeText(SignUpActivity.this, "Please fill up the required form", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(passwordStr.length()<6){
                    Toast.makeText(SignUpActivity.this, "Password should contain at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!passwordStr.matches(rePasswordStr)){
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                registerUser(userNameStr,passwordStr,rePasswordStr,fNameStr,sNameStr,emailStr,institutionStr,designationStr);
            }
        });
    }

    private void registerUser(final String userNameStr, String passwordStr, String rePasswordStr, final String fNameStr, final String sNameStr, final String emailStr, final String institutionStr, final String designationStr) {
        final ProgressDialog progressDialog=new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Signing up please wait ....");
        progressDialog.show();

        //creating a new user
        user.createUserWithEmailAndPassword(emailStr, passwordStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //checking if success
                        if(task.isSuccessful()){
                            Toast.makeText(SignUpActivity.this, "Successfully signed up", Toast.LENGTH_SHORT).show();

                            CustomDatabase database=new CustomDatabase(SignUpActivity.this);
                            database.clearCurrentUser();
                            database.insertUser(userNameStr,fNameStr,sNameStr,emailStr,institutionStr,designationStr);

                            startActivity(new Intent(SignUpActivity.this,HomeActivity.class));
                            finish();
                        }else{
                            Toast.makeText(SignUpActivity.this, "Signing up failed", Toast.LENGTH_SHORT).show();

                        }

                        progressDialog.dismiss();
                    }
                });
    }
}
