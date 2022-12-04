package com.example.barcode_reader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DisplayCode extends AppCompatActivity {

    private String s;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_code);
        s = getIntent().getStringExtra("SCAN_RESULT");
        tv=findViewById(R.id.scanned_data);
        tv.setText(s);
    }
}