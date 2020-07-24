package com.example.citizensapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static int SPLASH_LENGTH = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, SPLASH_LENGTH);

    }

    //    @Override
//    protected void onStart() {
//        super.onStart();
//
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if (currentUser ==  null){
//            SendUserToHomeActivity();
//        }
//    }
}
