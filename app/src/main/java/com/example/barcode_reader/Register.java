package com.example.barcode_reader;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Register extends AppCompatActivity {
    private static final int REQ = 1;
    TextView alreadyreg;
    private EditText username, email, password, passwordConfirm,phone,Bname,Badd,Bweb,Bloc;
    ImageView Blogo;
    private String uname_str, email_str, pass_str, pass_confirm_str,phone_str,Bname_str,Blogo_str,Badd_str,Bweb_str,Bloc_str;
    private Button Signupbtn;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    StorageReference storageReference;
    Boolean bool;
    String s = "yes";
    private String downloadUrl;

    FirebaseDatabase database;
    private Bitmap bitmap=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        alreadyreg = findViewById(R.id.alreadyreg);

        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.signupusernamelInput);
        email = findViewById(R.id.signupemaillInput);
        password = findViewById(R.id.signuppassInput);
        passwordConfirm = findViewById(R.id.signuppassInputConfirm);
        phone=findViewById(R.id.phone);
        Bname=findViewById(R.id.BusinessName);
        Blogo=findViewById(R.id.BusinessLogo);
        Badd=findViewById(R.id.BusinessAddress);
        Bweb=findViewById(R.id.WebsiteURL);
        Bloc=findViewById(R.id.BusinessLocation);

        Blogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        Signupbtn = findViewById(R.id.signUpBtn);

        database = FirebaseDatabase.getInstance();

        Signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckValidity();
//                PerforAuth();
            }
        });

        alreadyreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, MainActivity.class));
                //Toast.makeText(AdminDashboard.this, us, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openGallery() {
        Intent pickimage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickimage, REQ);
    }

    private void CheckValidity() {
        uname_str = username.getText().toString();
        email_str = email.getText().toString();

        phone_str=phone.getText().toString();
        Bname_str=Bname.getText().toString();
        Bloc_str=Bloc.getText().toString();
//        Blogo_str=Blogo.getText().toString();
        Badd_str=Badd.getText().toString();
        Bweb_str=Bweb.getText().toString();

        pass_str = password.getText().toString();
        pass_confirm_str = passwordConfirm.getText().toString();

        if (uname_str.isEmpty()) {
            username.setError("User name can't be empty");
            username.requestFocus();
        } else if (email_str.isEmpty()) {
            email.setError("Email can't be empty");
            email.requestFocus();
        } else if (phone_str.isEmpty()) {
            email.setError("Phone can't be empty");
            email.requestFocus();
        } else if (Bname_str.isEmpty()) {
            email.setError("Business Name can't be empty");
            email.requestFocus();
        }else if (Badd_str.isEmpty()) {
            email.setError("Business Address can't be empty");
            email.requestFocus();
        }else if (Bweb_str.isEmpty()) {
            email.setError("Website URL can't be empty");
            email.requestFocus();
        }else if (Bloc_str.isEmpty()) {
            email.setError("Business Location can't be empty");
            email.requestFocus();
        }
        else if (pass_str.isEmpty()) {
            password.setError("Please enter your password");
            password.requestFocus();
        } else if (!pass_confirm_str.equals(pass_str)) {
            passwordConfirm.setError("Password not matching");
            passwordConfirm.requestFocus();
        }else if(bitmap==null){
            Drawable drawable = getResources().getDrawable(R.drawable.vv);
            bitmap = ((BitmapDrawable)drawable).getBitmap();
        } else {
            insertImage();
        }
    }


    private void insertImage() {
        {
            progressDialog.setMessage("Please wait while Registration");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            try {
                progressDialog.show();
            }
            catch (WindowManager.BadTokenException e) {
                //use a log message
            }
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
            byte[] finalimg = baos.toByteArray();
            final StorageReference filepath;
            filepath=storageReference.child("Users").child(finalimg+"jpg");
            final UploadTask uploadTask=filepath.putBytes(finalimg);
            uploadTask.addOnCompleteListener(Register.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        downloadUrl=String.valueOf(uri);
                                        PerforAuth(downloadUrl);
                                    }
                                });
                            }
                        });
                    }else{
                        Toast.makeText(Register.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void PerforAuth(String downloadUrl) {
        insertImage();
        mAuth.createUserWithEmailAndPassword(email_str, pass_str).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();

                    String id = task.getResult().getUser().getUid();

                    UserDetails userDetails = new UserDetails(uname_str, email_str,phone_str,Bname_str,downloadUrl,Badd_str,Bweb_str,Bloc_str);

                    database.getReference().child("Users").child(id).setValue(userDetails);

                    //sendUserToNextActivity();
                    Toast.makeText(Register.this, "Registration Successful Log in Now", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Register.this, "ERROR !! May your email ID has been already registered.... Check your email ID please", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ && resultCode==RESULT_OK){
            Uri uri=data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Blogo.setImageBitmap(bitmap);
        }
    }
//    private void sendUserToNextActivity(){
//
//    }
}