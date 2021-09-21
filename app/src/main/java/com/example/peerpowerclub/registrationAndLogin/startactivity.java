package com.example.peerpowerclub.registrationAndLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.peerpowerclub.R;
import com.example.peerpowerclub.preRegistrtion;
import com.example.peerpowerclub.registrationAndLogin.Login;
import com.example.peerpowerclub.registrationAndLogin.Registration;

public class startactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startactivity);
           Button register =  findViewById(R.id.register);
           register.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent i=new Intent(getBaseContext(), preRegistrtion.class);
                   startActivity(i);

               }
           });
        Button login =  findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getBaseContext(), Login.class);
                startActivity(i);

            }
        });
    }
}