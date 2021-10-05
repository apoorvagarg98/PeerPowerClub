package com.example.peerpowerclub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.peerpowerclub.registrationAndLogin.Login;

public class scregistered extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scregistered);
        Button jao = findViewById(R.id.btngotohome);
        jao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(scregistered.this, Login.class));

            }
        });
    }
}