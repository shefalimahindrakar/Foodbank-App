package com.example.qr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    private Button scan_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OnClickListener listnr = new OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i= new Intent("ReaderActivity");
                startActivity(i);
            }
        };
        Button scan_btn =(Button) findViewById(R.id.scan_btn);
        scan_btn.setOnClickListener(listnr);

    }
}