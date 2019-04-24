package com.courseproject_2200.badiuzzaman.yourattendanceassistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private TextView editTextEmail,editTextPassword;
    private Button buttonLogin;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Log In");

        TextView signupText=findViewById(R.id.go_to_sign_up);
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                finish();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        //if getCurrentUser does not returns null
        if(firebaseAuth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            Toast.makeText(this, "You are already signed in", Toast.LENGTH_SHORT).show();
            finish();
        }
        //initializing views
        editTextEmail =  findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);

        buttonLogin = findViewById(R.id.button_login);

        progressDialog = new ProgressDialog(LoginActivity.this);

        //attaching listener to button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser(){

        //getting email and password from edit texts
        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        final String _email=email;

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Logging in Please Wait...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //if the task is successfull
                        if(task.isSuccessful()){
                            //start the profile activity
                            Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();

                            mStorageRef= FirebaseStorage.getInstance().getReference();
                            StorageReference riversRef = mStorageRef.child(_email+"_Admin.db");
                            Uri file = Uri.fromFile(new File("/data/data/com.courseproject_2200.badiuzzaman.yourattendanceassistant/databases/Admin.db"));

                            riversRef.getFile(file)
                                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            // Successfully downloaded data to local file
                                            progressDialog.dismiss();
                                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle failed download
                                    // ...
                                }
                            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    //calculating progress percentage
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                    //displaying percentage in progress dialog
                                    progressDialog.setMessage("Downloading current status " + ((int) progress) + "%...");
                                }
                            });
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Log in failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
