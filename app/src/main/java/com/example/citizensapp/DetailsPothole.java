package com.example.citizensapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.citizensapp.HomeActivity.EXTRA_ADDRESS;
import static com.example.citizensapp.HomeActivity.EXTRA_COMMENT;
import static com.example.citizensapp.HomeActivity.EXTRA_DIMENSION;
import static com.example.citizensapp.HomeActivity.EXTRA_LANDMARK;
import static com.example.citizensapp.HomeActivity.EXTRA_POSITION;
import static com.example.citizensapp.HomeActivity.EXTRA_POTHOLE_TYPE;
import static com.example.citizensapp.HomeActivity.EXTRA_PROOF_COMMENT;
import static com.example.citizensapp.HomeActivity.EXTRA_PROOF_IMAGE;
import static com.example.citizensapp.HomeActivity.EXTRA_TIMEKEY;
import static com.example.citizensapp.HomeActivity.EXTRA_URL;
import static com.example.citizensapp.HomeActivity.EXTRA_STATUS;

public class DetailsPothole extends AppCompatActivity implements OnMapReadyCallback {

    Button back_btn, nverifiedbtn,verifiedbtn;
    ImageView proof_image_view;
    TextView proof_comment_tv,comment_status_proof;
    private DatabaseReference mDatabaseRef;
    private FirebaseStorage mStorage;
    private FirebaseAuth mAuth;
    String currentUserID;
    private List<Upload> mUploads;

    GoogleMap mMap;
    //Double dlatitude,dlongitude;

    DatabaseReference locationRef;
    int pos=0;

    Uri mImageUri;
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("Reported Potholes");
    StorageTask mUploadTask;
    String potholeType,landmark,comment;

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_pothole);

        AtomicReference<SupportMapFragment> supportMapFragment = new AtomicReference<>((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map));
        supportMapFragment.get().getMapAsync(DetailsPothole.this);

        nverifiedbtn=findViewById(R.id.not_verified);
        verifiedbtn=findViewById(R.id.verified);
        proof_image_view=findViewById(R.id.pothole_image_view_proof);
        proof_comment_tv = findViewById(R.id.potholes_comments_textview_proof);
        comment_status_proof = findViewById(R.id.comment_status_proof);

        nverifiedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        verifiedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mImageUri != null) {
                    //mProgressBar.setVisibility(View.VISIBLE);
                    final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                            + "." + getFileExtension(mImageUri));
                    mUploadTask = fileReference.putFile(mImageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    //mProgressBar.setProgress(0);
                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(final Uri uri) {
                                            //mProgressBar.setVisibility(View.INVISIBLE);

                                            DateFormat dateFormat = new SimpleDateFormat("dd/MM");
                                            Date date = new Date();

                                            DateFormat datefull = new SimpleDateFormat("dd/MM/yyyy");
                                            Date date1 = new Date();
                                            DateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
                                            Date time = new Date();
                                            String mPotholeType = potholeType;
                                            //String mAddress = "none";
                                            String mLandmark = landmark;
                                            //String mDimension = "none";
                                            //String mComment = comment;
                                            String mDate = dateFormat.format(date).toString();
                                            //String mDateFull = datefull.format(date1).toString();
                                            String mTime = timeformat.format(time).toString();
                                            //String mSeverity = severity_textView.getText().toString();
                                            //String mName = "none";
                                            //String mEmail = mAuth.getCurrentUser().getEmail().toString();
                                            String mUserId = mAuth.getCurrentUser().getUid().toString();
                                            //String mPhone = phoneNumber.getEditText().getText().toString();
                                            String mStatus = "Completed";
                                            Calendar calendar = Calendar.getInstance();
                                            SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM");
                                            String saveCurrentDate = currentDate.format(calendar.getTime());


                                            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                                            String saveCurrentTime = currentTime.format(calendar.getTime());

                                            String mTimeKey = mTime + "-"+saveCurrentDate;
                                            //String mlatitude = lat.getBytes().toString();
                                            //  String mlongitude = lang.getBytes().toString();

//                                    Double mlat = currentLocation.getLatitude();
//                                    Double mlang = currentLocation.getLongitude();
                                            //Double mlat;
                                            //Double mlang;
//
//                                            if (latitude==null){
//                                                mlat = currentLocation.getLatitude();
//                                                mlang = currentLocation.getLongitude();
//                                            }
//                                            else {
//                                                mlat =  Double.parseDouble(latitude);
//                                                mlang = Double.parseDouble(longitude);
//                                            }

                                            //String proof_image = "NA";
                                            //String proof_comment = "NA";


                                            Upload2 upload = new Upload2(uri.toString(), mPotholeType, mLandmark, mDate,  mTime, mUserId, mTimeKey, mStatus);
                                            String uploadId = mDatabaseRef.push().getKey();
                                            assert uploadId != null;
                                            DatabaseReference mDatabaseRef2 = FirebaseDatabase.getInstance().getReference("History");
                                            mDatabaseRef2.child(mTimeKey).setValue(upload);

                                            Toast.makeText(DetailsPothole.this, "Added to History", Toast.LENGTH_LONG).show();
                                            //Toast.makeText(mContext, lat, Toast.LENGTH_SHORT).show();
                                            // Toast.makeText(mContext, lang, Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(DetailsPothole.this, HomeActivity.class));

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
                                    Toast.makeText(DetailsPothole.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    //mProgressBar.setProgress((int) progress);
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
                    Toast.makeText(DetailsPothole.this,"No File Selected",Toast.LENGTH_SHORT).show();
                }
                onDeleteClick(pos);
            }
        });

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users").child("Citizens").child(currentUserID).child("potholeReports");
        back_btn =findViewById(R.id.p_button_back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailsPothole.this,HomeActivity.class));
            }
        });
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_URL);
        potholeType = intent.getStringExtra(EXTRA_POTHOLE_TYPE);
        landmark = intent.getStringExtra(EXTRA_LANDMARK);
        String address = intent.getStringExtra(EXTRA_ADDRESS);
        String dimension = intent.getStringExtra(EXTRA_DIMENSION);
        comment = intent.getStringExtra(EXTRA_COMMENT);
        String status = intent.getStringExtra(EXTRA_STATUS);
        String proof_image = intent.getStringExtra(EXTRA_PROOF_IMAGE);
        String proof_comment = intent.getStringExtra(EXTRA_PROOF_COMMENT);
        pos = Integer.parseInt(intent.getStringExtra(EXTRA_POSITION));

        final String timeKey = intent.getStringExtra(EXTRA_TIMEKEY);
        locationRef = FirebaseDatabase.getInstance().getReference("Users").child("Citizens").child(currentUserID).child("potholeReports").child(timeKey);


        ImageView imageView = findViewById(R.id.pothole_image_view);
        TextView pothole_type_textView = findViewById(R.id.pothole_type_textView);
        TextView landmark_textView = findViewById(R.id.pothole_landmark_textview);
        TextView address_textView = findViewById(R.id.pothole_address_textView);
        TextView dimension_textView = findViewById(R.id.pothole_dimension_textview);
        TextView comment_textView = findViewById(R.id.potholes_comments_textview);

        Picasso.get().load(imageUrl).fit().into(imageView);
        pothole_type_textView.setText(potholeType);
        landmark_textView.setText(landmark);
        address_textView.setText(address);
        dimension_textView.setText(dimension);
        comment_textView.setText(comment);
        Picasso.get().load(proof_image).fit().into(proof_image_view);
        proof_comment_tv.setText(proof_comment);

        TextView potholeStatus = findViewById(R.id.pothole_status_textView);
        ProgressBar mprogressBar = findViewById(R.id.progress_bar_pothole);

        setProgressBar(status == null ? "Reported" : status, mprogressBar, potholeStatus);

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap=googleMap;
        locationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Double dlatitude = snapshot.child("mlat").getValue(Double.class);
                Double dlongitude = snapshot.child("mlang").getValue(Double.class);

                LatLng latLng = new LatLng(dlatitude,dlongitude);
                mMap.addMarker(new MarkerOptions().position(latLng).title("Pothole Here"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setProgressBar(String progress, ProgressBar mprogressBar, TextView potholeStatus){
        switch (progress) {
            case "Completed":
                mprogressBar.setProgress(4);
                mprogressBar.setProgressTintList(ColorStateList.valueOf(0xFF4caf50));
                proof_image_view.setVisibility(View.VISIBLE);
                proof_comment_tv.setVisibility(View.VISIBLE);
                nverifiedbtn.setVisibility(View.VISIBLE);
                verifiedbtn.setVisibility(View.VISIBLE);
                comment_status_proof.setVisibility(View.VISIBLE);
                break;
            case "Midway":
                mprogressBar.setProgress(3);
                mprogressBar.setProgressTintList(ColorStateList.valueOf(0xFFffeb3b));
                break;
            case "Processing":
                mprogressBar.setProgress(2);
                mprogressBar.setProgressTintList(ColorStateList.valueOf(0xFFff9800));
                break;
            default:
                mprogressBar.setProgress(1);
                mprogressBar.setProgressTintList(ColorStateList.valueOf(0xFFf44336));
                break;
        }
        potholeStatus.setText(progress);
    }

    private void onDeleteClick(int position) {
        Upload selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getKey();
        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(DetailsPothole.this, "Item Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}