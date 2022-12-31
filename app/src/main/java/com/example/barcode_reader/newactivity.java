package com.example.barcode_reader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class newactivity extends AppCompatActivity {

    LinearLayout layout_view;
    ImageView imgResultImage;
    Button btnConvertToImage;
    private String downloadUrl;
    private ProgressDialog pd;
    private Bitmap bitmap=null;
    private  StorageReference storageReference;
    private DatabaseReference reference;
    private String uid;

    private TextView name,email,phone,bname,badd,weburl,bloc;
    private ImageView logo;

    private String  name_str,email_str,phone_str,bname_str,logo_str,badd_str,weburl_str,bloc_str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newactivity);

        layout_view = findViewById(R.id.layout_view);
        imgResultImage = findViewById(R.id.imgResultImage);
        btnConvertToImage = findViewById(R.id.btnConvertToimage);

        reference= FirebaseDatabase.getInstance().getReference();

        name=findViewById(R.id.name_view);
        phone=findViewById(R.id.phone_view);
        email=findViewById(R.id.email_view);
        bname=findViewById(R.id.BusinessName_view);
        badd=findViewById(R.id.BusinessAddress_view);
        weburl=findViewById(R.id.WebsiteURL_view);
        bloc=findViewById(R.id.BusinessLocation_view);

        logo=findViewById(R.id.Blogo);

        pd=new ProgressDialog(this);

        uid= FirebaseAuth.getInstance().getUid();

        ShowDetails();


        btnConvertToImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });


    }

    private void ShowDetails() {
        reference.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name_str=snapshot.child("name").getValue(String.class);
                email_str=snapshot.child("email").getValue(String.class);
                phone_str=snapshot.child("phone").getValue(String.class);
                bname_str=snapshot.child("bname").getValue(String.class);
                badd_str=snapshot.child("baddress").getValue(String.class);
                bloc_str=snapshot.child("bloc").getValue(String.class);
                weburl_str=snapshot.child("burl").getValue(String.class);
                logo_str=snapshot.child("blogo").getValue(String.class);

                name.setText(name_str);
                email.setText(email_str);
                phone.setText(phone_str);
                bname.setText(bname_str);
                badd.setText(badd_str);
                bloc.setText(bloc_str);
                weburl.setText(weburl_str);
                Glide.with(newactivity.this).load(logo_str).into(logo);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap

        return returnedBitmap;
    }

    private void share() {

        Bitmap bitmap=getBitmapFromView(layout_view);
        //imgResultImage.setImageBitmap(bitmap);

        Bitmap icon = bitmap;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "title");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);


        OutputStream outstream;
        try {
            outstream = getContentResolver().openOutputStream(uri);
            icon.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
            outstream.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.putExtra(Intent.EXTRA_TEXT, weburl_str+"\nContact :"+phone_str);
        startActivity(Intent.createChooser(share, "Share Image"));

    }


    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[] { path },null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}