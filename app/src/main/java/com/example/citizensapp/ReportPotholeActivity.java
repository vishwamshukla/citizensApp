package com.example.citizensapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ReportPotholeActivity extends AppCompatActivity {
    private static final int PICK_VIDEO_REQUEST=1;
    private static final int PICK_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;

    private ImageView backButton;
    private Button mButtonUpload, videoUploadnew;
    private TextInputLayout mEditTextPothole_Type, mEditTextAddress, mEditTextLandmark, mEditTextDimensions, mEditTextComments;
    private ImageView mImageView;
    private TextView button_remove_image;
    Integer REQUEST_CAMERA = 0;
    private VideoView mVideoView;
    String currentPhotoPath;

    // MediaController mediaController;

    private ProgressBar mProgressBar;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

    private FirebaseAuth mAuth;
    String currentUserID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_pothole);



        videoUploadnew = findViewById(R.id.video_view_button);
        videoUploadnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile1();
                startActivity(new Intent(ReportPotholeActivity.this, UploadVideoActivity.class));
            }
        });



        mProgressBar=findViewById(R.id.progress_bar);
        button_remove_image = findViewById(R.id.pothole_remove_image_button);

        mButtonUpload = findViewById(R.id.p_button_continue);
        mImageView = findViewById(R.id.pothole_image_view);
        mProgressBar = findViewById(R.id.progress_bar);
        mEditTextPothole_Type = findViewById(R.id.pothole_type_textView);
        mEditTextAddress = findViewById(R.id.pothole_address_textView);
        mEditTextComments = findViewById(R.id.potholes_comments_textview);
        mEditTextDimensions = findViewById(R.id.pothole_dimension_textview);
        mEditTextLandmark = findViewById(R.id.pothole_landmark_textview);
        mAuth = FirebaseAuth.getInstance();

        currentUserID = mAuth.getCurrentUser().getUid();

        mStorageRef = FirebaseStorage.getInstance().getReference("Reported Potholes");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users").child("Citizens").child(currentUserID).child("potholeReports");



        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask!= null && mUploadTask.isInProgress()){
                    {
                        Toast.makeText(ReportPotholeActivity.this, "Upload in Process", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    uploadFile();

                }
            }
        });

        CardView image_layout = findViewById(R.id.pothole_image_layout);
        image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items ={"Open Camera","Upload from Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ReportPotholeActivity.this);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (items[i].equals("Open Camera")){
                            askCameraPermissions();
                            update_imageView_layout(true);
                        }
                        else if (items[i].equals("Upload from Gallery")){
                            OpenImageFileChooser();
                            update_imageView_layout(true);
                        }
                    }
                });
                builder.show();
            }
        });

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportPotholeActivity.this.finish();
            }
        });
    }

    private void SelectVideo(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_VIDEO_REQUEST);
    }

    private void initiate_remove_image_button(){
        button_remove_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Delete image from the view when this button is clicked.
                update_imageView_layout(false);
            }
        });
    }

    private void update_imageView_layout(Boolean isImageLoaded){
        LinearLayout hint_view = findViewById(R.id.upload_image_hint_view);
        if (isImageLoaded){
            hint_view.setVisibility(View.GONE);
            button_remove_image.setVisibility(View.VISIBLE);
            initiate_remove_image_button();
        }
        else{
            hint_view.setVisibility(View.VISIBLE);
            button_remove_image.setVisibility(View.GONE);
            mImageView.setImageResource(0);
        }
    }

    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else
        {
            dispatchTakePictureIntent();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }
            else{
                Toast.makeText(this, "Camera Permission needed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void OpenImageFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST  && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(mImageView);

        }
        else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){
            File f = new File(currentPhotoPath);
            mImageView.setImageURI(Uri.fromFile(f));
            mImageUri = Uri.fromFile(f);
        }
        /*if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            mVideoUri = data.getData();

            mVideoView.setVideoURI(mVideoUri);
        }*/
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        if (mImageUri != null) {
            mProgressBar.setVisibility(View.VISIBLE);
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mProgressBar.setProgress(0);
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {
                                    mProgressBar.setVisibility(View.INVISIBLE);

                                    DateFormat dateFormat = new SimpleDateFormat("dd/MM");
                                    Date date = new Date();

                                    DateFormat datefull = new SimpleDateFormat("dd/MM/yyyy");
                                    Date date1 = new Date();
                                    DateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
                                    Date time = new Date();
                                    String mPotholeType = mEditTextPothole_Type.getEditText().getText().toString();
                                    String mAddress = mEditTextAddress.getEditText().getText().toString();
                                    String mLandmark = mEditTextLandmark.getEditText().getText().toString();
                                    String mDimension = mEditTextDimensions.getEditText().getText().toString().trim();
                                    String mComment = mEditTextComments.getEditText().getText().toString();
                                    String mDate = dateFormat.format(date).toString();
                                    String mDateFull = datefull.format(date1).toString();
                                    String mTime = timeformat.format(time).toString();

//                                    mDatabaseRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                            if(snapshot.exists()){
//                                                if (snapshot.child("videourl").exists()){
//                                                    String mVideo = String.valueOf(snapshot.child("videourl").getValue());
//                                                    DateFormat dateFormat = new SimpleDateFormat("dd/MM");
//                                                    Date date = new Date();
//
//                                                    DateFormat datefull = new SimpleDateFormat("dd/MM/yyyy");
//                                                    Date date1 = new Date();
//                                                    DateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
//                                                    Date time = new Date();
//                                                    String mPotholeType = mEditTextPothole_Type.getEditText().getText().toString();
//                                                    String mAddress = mEditTextAddress.getEditText().getText().toString();
//                                                    String mLandmark = mEditTextLandmark.getEditText().getText().toString();
//                                                    String mDimension = mEditTextDimensions.getEditText().getText().toString().trim();
//                                                    String mComment = mEditTextComments.getEditText().getText().toString();
//                                                    String mDate = dateFormat.format(date).toString();
//                                                    String mDateFull = datefull.format(date1).toString();
//                                                    String mTime = timeformat.format(time).toString();
//
//                                                   // DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Citizens");
//
//                                                    HashMap<String, Object> userMap = new HashMap<>();
//                                                    userMap.put("ImageUrl", uri.toString());
//                                                    userMap.put("mPotholeType", mPotholeType);
//                                                    userMap.put("mAddress", mAddress);
//                                                    userMap.put("mLandmark", mLandmark);
//                                                    userMap.put("mDimension", mDimension);
//                                                    userMap.put("mComment", mComment);
//                                                    userMap.put("mDate", mDate);
//                                                    userMap.put("mDateFull", mDateFull);
//                                                    userMap.put("mTime", mTime);
//                                                    userMap.put("mVideo", mVideo);
//
//                                                    mDatabaseRef.child(currentUserID).updateChildren(userMap);
//
//                                                }
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//
//                                        }
//                                    });

                                    Upload upload = new Upload(uri.toString(), mPotholeType, mAddress, mLandmark, mDimension, mComment, mDate, mDateFull, mTime);
                                    String uploadId = mDatabaseRef.push().getKey();
                                    assert uploadId != null;
                                    mDatabaseRef.child(uploadId).setValue(upload);

                                    Toast.makeText(ReportPotholeActivity.this, "Thank you for reporting!", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(ReportPotholeActivity.this, HomeActivity.class));

                                }
                            });
                            /*Toast.makeText(Image_video_upload.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                               Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                               while (!urlTask.isSuccessful()) ;
                               Uri downloadUrl = urlTask.getResult();
                               Upload upload = new Upload(mEditTextFilename.getText().toString().trim(),downloadUrl.toString());
                               String uploadId = mDatabaseRef.push().getKey();
                               mDatabaseRef.child(uploadId).setValue(upload);*/

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ReportPotholeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        }

       /* if (mVideoUri != null){
            StorageReference reference = mStorageRefVideo.child(System.currentTimeMillis()+"."+getfileExt(mVideoUri));

            reference.putFile(mVideoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(),"Video Successfully Uploaded",Toast.LENGTH_SHORT).show();
                            Upload upload = new Upload(mEditTextFilename.getText().toString().trim(),
                                    taskSnapshot.getUploadSessionUri().toString());
                            String uploadId = mDatabaseRefVideo.push().getKey();
                            mDatabaseRefVideo.child(uploadId).setValue(upload);
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }*/

        else {
            Toast.makeText(this,"No File Selected",Toast.LENGTH_SHORT).show();
        }
    }
    private void uploadFile1(){
        if (mImageUri != null) {
            mProgressBar.setVisibility(View.VISIBLE);
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mProgressBar.setProgress(0);
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(final Uri uri) {
                                    mProgressBar.setVisibility(View.INVISIBLE);

                                    DateFormat dateFormat = new SimpleDateFormat("dd/MM");
                                    Date date = new Date();

                                    DateFormat datefull = new SimpleDateFormat("dd/MM/yyyy");
                                    Date date1 = new Date();
                                    DateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
                                    Date time = new Date();
                                    String mPotholeType = mEditTextPothole_Type.getEditText().getText().toString();
                                    String mAddress = mEditTextAddress.getEditText().getText().toString();
                                    String mLandmark = mEditTextLandmark.getEditText().getText().toString();
                                    String mDimension = mEditTextDimensions.getEditText().getText().toString().trim();
                                    String mComment = mEditTextComments.getEditText().getText().toString();
                                    String mDate = dateFormat.format(date).toString();
                                    String mDateFull = datefull.format(date1).toString();
                                    String mTime = timeformat.format(time).toString();

//                                    mDatabaseRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                            if(snapshot.exists()){
//                                                if (snapshot.child("videourl").exists()){
//                                                    String mVideo = String.valueOf(snapshot.child("videourl").getValue());
//                                                    DateFormat dateFormat = new SimpleDateFormat("dd/MM");
//                                                    Date date = new Date();
//
//                                                    DateFormat datefull = new SimpleDateFormat("dd/MM/yyyy");
//                                                    Date date1 = new Date();
//                                                    DateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
//                                                    Date time = new Date();
//                                                    String mPotholeType = mEditTextPothole_Type.getEditText().getText().toString();
//                                                    String mAddress = mEditTextAddress.getEditText().getText().toString();
//                                                    String mLandmark = mEditTextLandmark.getEditText().getText().toString();
//                                                    String mDimension = mEditTextDimensions.getEditText().getText().toString().trim();
//                                                    String mComment = mEditTextComments.getEditText().getText().toString();
//                                                    String mDate = dateFormat.format(date).toString();
//                                                    String mDateFull = datefull.format(date1).toString();
//                                                    String mTime = timeformat.format(time).toString();
//
//                                                   // DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Citizens");
//
//                                                    HashMap<String, Object> userMap = new HashMap<>();
//                                                    userMap.put("ImageUrl", uri.toString());
//                                                    userMap.put("mPotholeType", mPotholeType);
//                                                    userMap.put("mAddress", mAddress);
//                                                    userMap.put("mLandmark", mLandmark);
//                                                    userMap.put("mDimension", mDimension);
//                                                    userMap.put("mComment", mComment);
//                                                    userMap.put("mDate", mDate);
//                                                    userMap.put("mDateFull", mDateFull);
//                                                    userMap.put("mTime", mTime);
//                                                    userMap.put("mVideo", mVideo);
//
//                                                    mDatabaseRef.child(currentUserID).updateChildren(userMap);
//
//                                                }
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//
//                                        }
//                                    });

                                    Upload upload = new Upload(uri.toString(), mPotholeType, mAddress, mLandmark, mDimension, mComment, mDate, mDateFull, mTime);
                                    String uploadId = mDatabaseRef.push().getKey();
                                    assert uploadId != null;
                                    mDatabaseRef.child(uploadId).setValue(upload);

                                }
                            });
                            /*Toast.makeText(Image_video_upload.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                               Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                               while (!urlTask.isSuccessful()) ;
                               Uri downloadUrl = urlTask.getResult();
                               Upload upload = new Upload(mEditTextFilename.getText().toString().trim(),downloadUrl.toString());
                               String uploadId = mDatabaseRef.push().getKey();
                               mDatabaseRef.child(uploadId).setValue(upload);*/

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ReportPotholeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        }

       /* if (mVideoUri != null){
            StorageReference reference = mStorageRefVideo.child(System.currentTimeMillis()+"."+getfileExt(mVideoUri));

            reference.putFile(mVideoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(),"Video Successfully Uploaded",Toast.LENGTH_SHORT).show();
                            Upload upload = new Upload(mEditTextFilename.getText().toString().trim(),
                                    taskSnapshot.getUploadSessionUri().toString());
                            String uploadId = mDatabaseRefVideo.push().getKey();
                            mDatabaseRefVideo.child(uploadId).setValue(upload);
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }*/

        else {
            Toast.makeText(this,"No File Selected",Toast.LENGTH_SHORT).show();
        }
    }



}