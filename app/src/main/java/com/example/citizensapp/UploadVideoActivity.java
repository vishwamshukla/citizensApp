package com.example.citizensapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class UploadVideoActivity extends AppCompatActivity {
    private static final int PICK_VIDEO_REQUEST=1;

    VideoView videoView;
    TextView show_videos_textView;
    Uri videoUri;
    MediaController mediaController;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    Member member;
    UploadTask uploadTask;
    private ProgressBar mProgressBar;
    Button uploadButton;
    private FirebaseAuth mAuth;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        uploadButton = findViewById(R.id.upload_button);
        mProgressBar=findViewById(R.id.progress_bar);
        member = new Member();
        videoView = findViewById(R.id.videoview);

        mAuth = FirebaseAuth.getInstance();

        currentUserID = mAuth.getCurrentUser().getUid();
      //  mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users").child("Citizens").child(currentUserID).child("potholeReports");

        storageReference = FirebaseStorage.getInstance().getReference("Videos");
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Citizens").child(currentUserID).child("potholeReports");

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectVideo();
            }
        });


        videoView.setMediaController(mediaController);
        videoView.start();

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadView();
            }
        });
    }

    private void SelectVideo(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            try {

                videoUri = data.getData();
                videoView.setVideoURI(videoUri);
            }catch (Exception e){
                Toast.makeText(this, "No File Selected", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private String getfileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void  UploadView() {

        if (videoUri != null) {
            mProgressBar.setVisibility(View.VISIBLE);
            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getfileExt(videoUri));

            uploadTask = reference.putFile(videoUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult();
                                mProgressBar.setVisibility(View.INVISIBLE);
                                member.setVideourl(downloadUrl.toString());
                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap.put("video", downloadUrl.toString());
                                databaseReference.child(currentUserID).updateChildren(userMap);
//                                String i = databaseReference.push().getKey();
//                                databaseReference.child(i).setValue(member);
                            } else{
                                Toast.makeText(UploadVideoActivity.this, "Video upload failed...", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
        }
        else {
            Toast.makeText(this, "O FileSelected", Toast.LENGTH_SHORT).show();
        }
    }
}