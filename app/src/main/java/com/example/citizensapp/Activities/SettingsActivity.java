package com.example.citizensapp.Activities;

import android.os.Bundle;

import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.SwitchPreferenceCompat;

import com.example.citizensapp.Fragments.SettingsFragment;
import com.example.citizensapp.R;


public class SettingsActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private SwitchPreferenceCompat deleteFileOption;
    private SwitchPreferenceCompat autoUploadOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        frameLayout = (FrameLayout) findViewById(R.id.settings_frame);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings_frame, new SettingsFragment())
                .commit();
    }
}


