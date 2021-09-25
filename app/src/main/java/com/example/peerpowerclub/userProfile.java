package com.example.peerpowerclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peerpowerclub.models.user;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class userProfile extends AppCompatActivity {
    private FirebaseUser user;
    public DatabaseReference reference;
    private String userId;
    static  String areaofinterest,dayNight,email,ltg,stg,ln,prl;
    public CircleImageView profilephoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        userId = getIntent().getStringExtra("uid");
        reference = FirebaseDatabase.getInstance().getReference("users");


        final TextView fullnameTextView = (TextView) findViewById(R.id.name4);

        final TextView phonenumberTextView = (TextView) findViewById(R.id.phonenumber4);
        final TextView prefferedTextView = (TextView) findViewById(R.id.prefferedtime4);
        final TextView areaofInterestTextView = (TextView) findViewById(R.id.areaofinterest4);
        final TextView status = (TextView) findViewById(R.id.status4);
        final TextView shtg = (TextView) findViewById(R.id.stg4);
        final TextView lotg = (TextView) findViewById(R.id.ltg4);
        final TextView lng = (TextView) findViewById(R.id.lng4);
        final TextView purl= (TextView) findViewById(R.id.profileurl4);
        profilephoto = findViewById(R.id.profile_image4);
       reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                com.example.peerpowerclub.models.user user4profile = snapshot.getValue(com.example.peerpowerclub.models.user.class);
                if (user4profile != null) {
                    dayNight= user4profile.prefferedTime;
                    ltg = user4profile.ltg;
                    stg = user4profile.stg;
                    ln = user4profile.language;
                    prl = user4profile.profileurl;

                    String phone = user4profile.phonenumber;
                    areaofinterest = user4profile.areaOfInterest;

                    String name = user4profile.fullname;

                    fullnameTextView.setText("Name: " + name);

                    phonenumberTextView.setText("Phone Number: " + phone);
                    prefferedTextView.setText("Preffered Time: " + dayNight);
                    areaofInterestTextView.setText("Area of interest: " + areaofinterest);
                    status.setText("Status: " + user4profile.status);
                    shtg.setText("short term goal:" + user4profile.stg);
                    lotg.setText("long term goal:" + user4profile.ltg);
                    lng.setText("language: " + user4profile.language);
                    purl.setText("profileUrl:" + user4profile.profileurl);
                    Picasso.get().load(user4profile.imageuri).into(profilephoto);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(userProfile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }
}