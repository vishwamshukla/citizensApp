package com.example.citizensapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportPotholeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;

    private Button OpenCamera;
    private Button SelectFileBtn;
    private Button mButtonUpload;
    private EditText mEditTextPothole_Type;
    private EditText mEditTextAddress;
    private EditText mEditTextLandmark;
    private EditText mEditTextDimensions;
    private EditText mEditTextComments;
    private ImageView mImageView;
    Integer REQUEST_CAMERA = 0;
    private VideoView mVideoView;
    String currentPhotoPath;

    // MediaController mediaController;

    private ProgressBar mProgressBar;

    private Uri mImageUri;
    private Uri mVideoUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_pothole);

        mProgressBar=findViewById(R.id.progress_bar);

        OpenCamera = findViewById(R.id.Open_Camera_button);
        SelectFileBtn = findViewById(R.id.pothole_Select_file);
        mButtonUpload = findViewById(R.id.p_button_continue);
        mImageView = findViewById(R.id.image_upload_view);
        mProgressBar = findViewById(R.id.progress_bar);
        mEditTextPothole_Type = findViewById(R.id.pothole_type_edittext);
        mEditTextAddress = findViewById(R.id.pothole_address_edittext);
        mEditTextComments = findViewById(R.id.pothole_comments_edittext);
        mEditTextDimensions = findViewById(R.id.pothole_dimensions_edittext);
        mEditTextLandmark = findViewById(R.id.pothole_landmark_edittext);


        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        Button open_camera = findViewById(R.id.Open_Camera_button);
        open_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermissions();
            }
        });

        Button upload_file = findViewById(R.id.pothole_Select_file);
        upload_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items ={"Upload Image","Upload Video"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ReportPotholeActivity.this);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (items[i].equals("Upload Image")){
                            OpenImageFileChooser();
                        }

                        else if (items[i].equals("Upload Video")){
                            //TODO: Write code for uploading Video
                        }
                    }
                });
                builder.show();
            }
        });


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
                                public void onSuccess(Uri uri) {
                                    mProgressBar.setVisibility(View.INVISIBLE);


                                    String mPotholeType = mEditTextPothole_Type.getText().toString();
                                    String mAddress = mEditTextAddress.getText().toString();
                                    String mLandmark = mEditTextLandmark.getText().toString();
                                    String mDimension = mEditTextDimensions.getText().toString().trim();
                                    String mComment = mEditTextComments.getText().toString();


                                    Upload upload = new Upload(uri.toString(), mPotholeType, mAddress, mLandmark, mDimension, mComment);
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


}