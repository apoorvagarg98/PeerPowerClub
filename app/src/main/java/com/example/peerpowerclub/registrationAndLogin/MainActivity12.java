package com.example.peerpowerclub.registrationAndLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.peerpowerclub.R;
import com.example.peerpowerclub.myHome;
import com.example.peerpowerclub.strt1;

public class MainActivity12 extends AppCompatActivity {
    String checkbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main12);
        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        checkbox = preferences.getString("remember","");
        if(checkbox.equals("true"))
        { Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep(2*1000);

                    // After 5 seconds redirect to another intent
                    Intent i=new Intent(getBaseContext(), myHome.class);
                    startActivity(i);

                    //Remove activity
                    finish();
                } catch (Exception e) {
                }
            }
        };
            // start thread
            background.start();


        }
        else{

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            //show start activity

            startActivity(new Intent(MainActivity12.this, strt1.class));

        }
        else{
            Thread background = new Thread() {
                public void run() {
                    try {
                        // Thread will sleep for 5 seconds
                        sleep(2*1000);

                        // After 5 seconds redirect to another intent
                        Intent i=new Intent(getBaseContext(), startactivity.class);
                        startActivity(i);

                        //Remove activity
                        finish();
                    } catch (Exception e) {
                    }
                }
            };
            // start thread
            background.start();

        }


        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();

    }
}}
