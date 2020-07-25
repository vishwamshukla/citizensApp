package com.example.citizensapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.core.models.User;

import java.util.Objects;

public class RegisterActivity2 extends AppCompatActivity {

    TextInputLayout name3, username3, email3, phone3, password3, confirmPassword3;
    Button registerButton, registerToLoginButton;

    FirebaseDatabase rootNode;
    DatabaseReference reference, userRef;
    FirebaseAuth mAuth;
    String currentUserId;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progressBar = findViewById(R.id.progress_bar);
        mAuth = FirebaseAuth.getInstance();
        //currentUserId = mAuth.getCurrentUser().getUid();
//        name3 = findViewById(R.id.name3);
//        username3= findViewById(R.id.username3);
        email3 = findViewById(R.id.email3);
//        phone3 = findViewById(R.id.phone3);
        password3 = findViewById(R.id.passsword3);
        confirmPassword3 = findViewById(R.id.confirmpasssword3);

        registerButton = findViewById(R.id.register_button);
        registerToLoginButton = findViewById(R.id.registerToLogin_button);
        registerToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity2.this, LoginActivity2.class));
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference().child("Users").child("Citizens");

               // String id = currentUserId;
//                String name = name3.getEditText().getText().toString();
//                String username = username3.getEditText().getText().toString();
                String email = email3.getEditText().getText().toString();
//                String phone = phone3.getEditText().getText().toString();
                String password = password3.getEditText().getText().toString();
                String confirmPassword = confirmPassword3.getEditText().getText().toString();
                String emailpattern = "[a-zA=z0-9._-]+@[a-z]+\\.+[a-z]+";

//                if (TextUtils.isEmpty(name)){
//                    Toast.makeText(RegisterActivity2.this, "Name is required", Toast.LENGTH_SHORT).show();
//                }
//                else if (TextUtils.isEmpty(username)){
//                    Toast.makeText(RegisterActivity2.this, "Username is required", Toast.LENGTH_SHORT).show();
//                }
                if(email.isEmpty()){
                    email3.setError("Field can't be empty");
                }
                else if(!email.matches(emailpattern)){
                    email3.setError("Email address is not valid");
                }
                else if(password.isEmpty()){
                    password3.setError("Field can't be empty");
                }
                else if(confirmPassword.isEmpty()){
                    confirmPassword3.setError("Field can't be empty");
                }
//                if (TextUtils.isEmpty(email)){
//                    Toast.makeText(RegisterActivity2.this, "Email is required", Toast.LENGTH_SHORT).show();
//                }
//                else if (TextUtils.isEmpty(phone)){
//                    Toast.makeText(RegisterActivity2.this, "Phone number is required", Toast.LENGTH_SHORT).show();
//                }
//                else if (TextUtils.isEmpty(password)){
//                    Toast.makeText(RegisterActivity2.this, "Password is required", Toast.LENGTH_SHORT).show();
//                }
//                else if(!password.equals(confirmPassword)){
//                    Toast.makeText(RegisterActivity2.this, "Password do not match", Toast.LENGTH_SHORT).show();
//                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        startActivity(new Intent(RegisterActivity2.this, MoreDetailsActivity.class));
                                        Toast.makeText(RegisterActivity2.this, "Account Created...", Toast.LENGTH_SHORT).show();
                                        //loadingBar.dismiss();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                    else{
                                        String message = task.getException().getMessage();
                                        Toast.makeText(RegisterActivity2.this, "Error-> "+message, Toast.LENGTH_SHORT).show();
                                        //loadingBar.dismiss();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                }
//                UserHelper helper = new UserHelper(name, username, email, phone, password);
//                reference.child(phone).setValue(helper);
            }
        });
    }
}