package com.example.peerpowerclub;


import static com.example.peerpowerclub.fragmentCodes.finalHome.imageUri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.peerpowerclub.fragmentCodes.Fragment1;
import com.example.peerpowerclub.fragmentCodes.Fragment2Chat;
import com.example.peerpowerclub.fragmentCodes.Fragment3courses;
import com.example.peerpowerclub.fragmentCodes.finalHome;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class myHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNav);
getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new finalHome()).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.profile:
              getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new Fragment1()).commit();
              break;
            case R.id.addPost:
                startActivity(new Intent(myHome.this,postkaro.class));

        }

        return super.onOptionsItemSelected(item);
    }



    private BottomNavigationView.OnNavigationItemSelectedListener onNav = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selected = null;
            switch (item.getItemId()){
                case R.id.profile_bottom:
                    selected = new finalHome();
                    break;
                case R.id.chat_bottom:
                selected = new Fragment2Chat();
                break;
                case R.id.courses_bottom:
                selected = new Fragment3courses();
                break;

            }

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,selected).commit();

            return true;

        }
    };
}