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
import android.widget.Toast;

import com.example.peerpowerclub.fragmentCodes.Fragment1;
import com.example.peerpowerclub.fragmentCodes.Fragment2Chat;
import com.example.peerpowerclub.fragmentCodes.Fragment3courses;
import com.example.peerpowerclub.fragmentCodes.finalHome;
import com.example.peerpowerclub.registrationAndLogin.Registration;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class myHome extends AppCompatActivity {
DatabaseReference userka;
Boolean hey;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_home);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userka = FirebaseDatabase.getInstance().getReference("users");
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNav);
getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new finalHome()).commit();
        userka.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("areaOfInterest").getValue().equals("")){
                    //  startActivity(new Intent(myHome.this,Registration.class));
                    Toast.makeText(myHome.this, "i am working", Toast.LENGTH_SHORT).show();
hey = true;
                }
                else{
                    Toast.makeText(myHome.this, "nope", Toast.LENGTH_SHORT).show();
hey = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
            case R.id.profile:{
                userka.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("areaOfInterest").getValue().toString().equals("")){
                            startActivity(new Intent(myHome.this, joinchat.class));
                        }
                        else{
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new Fragment1()).commit();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
              break;}
            case R.id.addPost:{
                userka.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("areaOfInterest").getValue().toString().equals("")){
                            startActivity(new Intent(myHome.this, joinchat.class));

                        }
                        else{
                            startActivity(new Intent(myHome.this,postkaro.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            }



        }

        return super.onOptionsItemSelected(item);
    }



    private BottomNavigationView.OnNavigationItemSelectedListener onNav = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            final Fragment[] selected = {null};
            switch (item.getItemId()){
                case R.id.profile_bottom:
                    selected[0] = new finalHome();
                    break;
                case R.id.chat_bottom:{

if(hey == true){                startActivity(new Intent(myHome.this,joinchat.class));
}
else {
selected[0] = new Fragment2Chat();}


                break;}
                case R.id.courses_bottom:
                selected[0] = new Fragment3courses();
                break;

            }

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, selected[0]).commit();

            return true;

        }
    };
}