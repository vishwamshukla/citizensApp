package com.example.citizensapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;


import androidx.appcompat.app.AppCompatActivity;

import com.example.citizensapp.Core.ApplicationClass;
import com.example.citizensapp.SplashActivity;
import com.google.firebase.crash.FirebaseCrash;


import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class PlaceholderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseCrash.setCrashCollectionEnabled(true);
        SharedPreferences onboardingPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationClass.getInstance());
        boolean onboarding = onboardingPreferences.getBoolean("onboarding", true);

        // initialising TensorFlow
        TensorFlowInferenceInterface tensorFlowInferenceInterface = new TensorFlowInferenceInterface(getAssets(), "tensorflow_accelerometer_model.pb");
        ApplicationClass.getInstance().setTensorFlowInferenceInterface(tensorFlowInferenceInterface);

        if (onboarding) {
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
