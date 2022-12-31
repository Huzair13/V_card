package com.example.barcode_reader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    LinearLayout scan;
    private Button btnLogin;
    private EditText LogEmail,username;
    private EditText LogPassword;
    private TextView ForgetPass;
    private  TextView Register;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    public static String us_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scan=findViewById(R.id.Scanner);
        username=findViewById(R.id.loginemailInput);
        us_name=username.getText().toString();
        LogPassword=findViewById(R.id.loginpasswordInput);
        btnLogin=findViewById(R.id.loginbutton);
        ForgetPass=findViewById(R.id.forgetpassword);
        Register=findViewById(R.id.Register);

        progressDialog=new ProgressDialog(this);

        mAuth=FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(view -> {
            LoginUserCheck();
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, com.example.barcode_reader.Register.class));
            }
        });

    }

    private void LoginUserCheck() {
        String uname=username.getText().toString();
        String password =LogPassword.getText().toString();
        if(TextUtils.isEmpty(uname)){
            username.setError("UserName can't be empty");
            username.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            LogPassword.setError("Password Cannot be empty");
            LogPassword.requestFocus();
        }else {
            String email=uname;
            progressDialog.setMessage("Logging in please wait.....");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            logInwithUserName(email,password);
        }
    }

    private void logInwithUserName(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    try {
                        if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    } catch (final IllegalArgumentException e) {
                        // Handle or log or ignore
                    } catch (final Exception e) {
                        // Handle or log or ignore
                    } finally {
                        progressDialog = null;
                    }

                    Toast.makeText(MainActivity.this, "User Logged in successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, newactivity.class));
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Login Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}