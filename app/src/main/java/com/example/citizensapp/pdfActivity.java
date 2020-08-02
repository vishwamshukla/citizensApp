package com.example.citizensapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.zxing.pdf417.PDF417Writer;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class pdfActivity extends AppCompatActivity {

    private EditText myEditText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        button = findViewById(R.id.pdfbutton);

        myEditText = findViewById(R.id.pdfedittext);
        ActivityCompat.requestPermissions(pdfActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Document doc = new Document();
                String mfile = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
                String mfilepat = Environment.getExternalStorageDirectory()+"/"+mfile+".pdf";
                Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

                try {


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createPdf() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PdfDocument myPdfDocument = new PdfDocument();
                Paint myPaint = new Paint();
                PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(250,400,1).create();
                PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);

                Canvas canvas = myPage1.getCanvas();
                canvas.drawText("Helloooo", 40, 50, myPaint);
                myPdfDocument.finishPage(myPage1);

                File file = new File(Environment.getExternalStorageDirectory(), "/firstpdf.pdf");
                try {
                    myPdfDocument.writeTo(new FileOutputStream(file));
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(pdfActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
                myPdfDocument.close();
            }
        });
    }

    public void createMyPDF(View view){

        PdfDocument myPdfDocument = new PdfDocument();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(300,600,1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);

        Paint myPaint = new Paint();
        String myString = myEditText.getText().toString();
        int x = 10, y=25;
        for (String line:myString.split("\n")){
            myPage.getCanvas().drawText(line, x, y, myPaint);
            y+=myPaint.descent()-myPaint.ascent();
        }

        myPdfDocument.finishPage(myPage);

        String myFilePath = Environment.getExternalStorageDirectory().getPath() + "/myPDFFile.pdf   ";
        File myFile = new File(myFilePath);
        try {
            myPdfDocument.writeTo(new FileOutputStream(myFile));
        }
        catch (Exception e){
            e.printStackTrace();
            myEditText.setText("Error");
        }

        myPdfDocument.close();
    }


}
