package com.example.peerpowerclub.registrationAndLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.peerpowerclub.R;
import com.example.peerpowerclub.strt1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            //show start activity

            startActivity(new Intent(MainActivity.this, strt1.class));
            Toast.makeText(MainActivity.this, "First Run", Toast.LENGTH_LONG)
                    .show();
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
}
