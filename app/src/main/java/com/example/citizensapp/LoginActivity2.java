package com.example.citizensapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity2 extends AppCompatActivity {

    Button register, login_btn;
    ImageView image;
    TextView logoText, slogantext;
    TextInputLayout email, password;
    private static int SPLASH_SCREEN = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login2);

        register = findViewById(R.id.new_user);
        image = findViewById(R.id.logo_image);
        logoText = findViewById(R.id.logo_name);
        slogantext = findViewById(R.id.slogan_name);
        email = findViewById(R.id.emaill);
        password=findViewById(R.id.passswordd);
        login_btn = findViewById(R.id.login_btn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity2.this, RegisterActivity2.class);
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(image, "logo_image");
                pairs[1] = new Pair<View, String>(logoText, "logo_text");
                pairs[2] = new Pair<View, String>(slogantext, "logo_desc");
                pairs[3] = new Pair<View, String>(email, "email_tran");
                pairs[4] = new Pair<View, String>(password, "password_tran");
                pairs[5] = new Pair<View, String>(login_btn, "button_tran");
                pairs[6] = new Pair<View, String>(register, "register_tran");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity2.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });
    }
}