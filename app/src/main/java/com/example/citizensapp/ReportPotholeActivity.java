package com.example.citizensapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReportPotholeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_pothole);

        Button open_camera = findViewById(R.id.pothole_camera_button);
        open_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Write code to open camera for image upload
            }
        });

        Button upload_file = findViewById(R.id.pothole_upload_file);
        upload_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items ={"Upload Image","Upload Video"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ReportPotholeActivity.this);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (items[i].equals("Upload Image")){
                            //TODO: Write code for uploading Image
                        }

                        else if (items[i].equals("Upload Video")){
                            //TODO: Write code for uploading Video
                        }
                    }
                });
                builder.show();
            }
        });

    }
}