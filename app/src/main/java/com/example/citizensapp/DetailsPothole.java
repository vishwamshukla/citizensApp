package com.example.citizensapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.citizensapp.HomeActivity.EXTRA_ADDRESS;
import static com.example.citizensapp.HomeActivity.EXTRA_COMMENT;
import static com.example.citizensapp.HomeActivity.EXTRA_DIMENSION;
import static com.example.citizensapp.HomeActivity.EXTRA_LANDMARK;
import static com.example.citizensapp.HomeActivity.EXTRA_POTHOLE_TYPE;
import static com.example.citizensapp.HomeActivity.EXTRA_TIMEKEY;
import static com.example.citizensapp.HomeActivity.EXTRA_URL;
import static com.example.citizensapp.HomeActivity.EXTRA_STATUS;

public class DetailsPothole extends AppCompatActivity implements OnMapReadyCallback {

    Button back_btn;
    private DatabaseReference mDatabaseRef;
    private FirebaseStorage mStorage;
    private FirebaseAuth mAuth;
    String currentUserID;
    private List<Upload> mUploads;

    GoogleMap mMap;
    //Double dlatitude,dlongitude;

    DatabaseReference locationRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_pothole);

        AtomicReference<SupportMapFragment> supportMapFragment = new AtomicReference<>((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map));
        supportMapFragment.get().getMapAsync(DetailsPothole.this);

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
        String potholeType = intent.getStringExtra(EXTRA_POTHOLE_TYPE);
        String landmark = intent.getStringExtra(EXTRA_LANDMARK);
        String address = intent.getStringExtra(EXTRA_ADDRESS);
        String dimension = intent.getStringExtra(EXTRA_DIMENSION);
        String comment = intent.getStringExtra(EXTRA_COMMENT);
        String status = intent.getStringExtra(EXTRA_STATUS);

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