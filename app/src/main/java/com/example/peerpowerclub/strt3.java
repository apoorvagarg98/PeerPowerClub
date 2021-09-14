package com.example.peerpowerclub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.peerpowerclub.registrationAndLogin.startactivity;

public class strt3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strt3);
        Button start = findViewById(R.id.startforfirsttime);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(strt3.this, startactivity.class));
            }
        });
    }
}
