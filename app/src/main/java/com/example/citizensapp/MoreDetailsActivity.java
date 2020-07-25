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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MoreDetailsActivity extends AppCompatActivity {

    TextInputLayout name3, username3, phone3;
    Button submitButton;

    FirebaseDatabase rootNode;
    DatabaseReference reference, UsersRef;
    FirebaseAuth mAuth;
    String currentUserID;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_details);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progressBar = findViewById(R.id.progress_bar);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Citizens").child(currentUserID);
        name3 = findViewById(R.id.name3);
        username3= findViewById(R.id.username3);

        phone3 = findViewById(R.id.phone3);

        submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfoSaved();
            }
        });
    }

    private void userInfoSaved() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Citizens");
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", name3.getEditText().getText().toString());
        userMap.put("username", username3.getEditText().getText().toString());
        userMap.put("phone", phone3.getEditText().getText().toString());
        ref.child(currentUserID).updateChildren(userMap);


        Toast.makeText(MoreDetailsActivity.this, "Thank you", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MoreDetailsActivity.this, HomeActivity.class));
    }
}