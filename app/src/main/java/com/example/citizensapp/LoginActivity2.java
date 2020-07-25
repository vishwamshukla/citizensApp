package com.example.citizensapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity2 extends AppCompatActivity {

    Button register, login_btn, resetButton;
    ImageView image;
    TextView logoText, slogantext;
    TextInputLayout email2, password2;
    private static int SPLASH_SCREEN = 5000;

    FirebaseAuth mAuth;
    String currentuser;
    private ProgressBar progressBar;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login2);
        progressBar = findViewById(R.id.progress_bar);

        mAuth = FirebaseAuth.getInstance();
//        currentuser = mAuth.getCurrentUser().getUid();

        register = findViewById(R.id.new_user);
        image = findViewById(R.id.logo_image);
        logoText = findViewById(R.id.logo_name);
        slogantext = findViewById(R.id.slogan_name);
        email2 = findViewById(R.id.emaill);
        password2=findViewById(R.id.passswordd);
        login_btn = findViewById(R.id.login_btn);
        resetButton = findViewById(R.id.reset_password_button);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               LoginUser();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity2.this, RegisterActivity2.class);
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(image, "logo_image");
                pairs[1] = new Pair<View, String>(logoText, "logo_text");
                pairs[2] = new Pair<View, String>(slogantext, "logo_desc");
                pairs[3] = new Pair<View, String>(email2, "email_tran");
                pairs[4] = new Pair<View, String>(password2, "password_tran");
                pairs[5] = new Pair<View, String>(login_btn, "button_tran");
                pairs[6] = new Pair<View, String>(register, "register_tran");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity2.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity2.this,ResetPasswordActivity2.class);
                Pair[] pairs = new Pair[5];
                pairs[0] = new Pair<View, String>(image, "logo_image");
                pairs[1] = new Pair<View, String>(logoText, "logo_text");
                pairs[2] = new Pair<View, String>(slogantext, "logo_desc");
                pairs[3] = new Pair<View, String>(email2, "email_tran");
                pairs[4] = new Pair<View, String>(resetButton, "button_tran");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity2.this, pairs);
                startActivity(intent, options.toBundle());

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser1 = mAuth.getCurrentUser();
        if (currentUser1 !=  null){

            SendUserToHomeActivity();
        }
    }
    private void SendUserToHomeActivity() {
        Intent intent = new Intent(LoginActivity2.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void LoginUser() {
        String email1 = email2.getEditText().getText().toString();
        String password1 = password2.getEditText().getText().toString();

        if (TextUtils.isEmpty(email1)) {
            Toast.makeText(this, "Enter Phone Number!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password1)) {
            Toast.makeText(this, "Enter Password!", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(email1, password1)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                SendUserToHomeActivity();
                                Toast.makeText(LoginActivity2.this, "Logged in", Toast.LENGTH_SHORT).show();
                                //loadingBar.dismiss();
                                progressBar.setVisibility(View.INVISIBLE);
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(LoginActivity2.this, "Error-" + message, Toast.LENGTH_SHORT).show();
                                //loadingBar.dismiss();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
        }
    }
}