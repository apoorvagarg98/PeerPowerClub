package com.example.peerpowerclub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.peerpowerclub.registrationAndLogin.Registration;

public class joinchat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joinchat);
        Button jaojao = findViewById(R.id.joinpeer);
        jaojao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(joinchat.this, Registration.class));

            }
        });
    }
}