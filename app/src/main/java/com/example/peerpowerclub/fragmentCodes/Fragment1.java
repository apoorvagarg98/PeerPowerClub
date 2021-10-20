package com.example.peerpowerclub.fragmentCodes;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.peerpowerclub.R;
import com.example.peerpowerclub.models.user;
import com.example.peerpowerclub.myHome;
import com.example.peerpowerclub.registrationAndLogin.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment1 extends Fragment {
    private FirebaseUser user;
    public DatabaseReference reference;
    private String userId;
    static  String areaofinterest,dayNight,email,ltg,stg,prl,insta,twitter,others,timecall ,skillsknow;
    public CircleImageView profilephoto;



    private Button buttons;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1home,container,false);



        profilephoto = view.findViewById(R.id.profile_image);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userId = user.getUid();
        final TextView fullnameTextView = (TextView) view.findViewById(R.id.name);
        final TextView emailTextView = (TextView) view.findViewById(R.id.Email);
        final TextView phonenumberTextView = (TextView) view.findViewById(R.id.phonenumber);
        final TextView prefferedTextView = (TextView) view.findViewById(R.id.timeforcall);
        final TextView areaofInterestTextView = (TextView) view.findViewById(R.id.pSkillstolearn);

        final TextView shtg = (TextView) view.findViewById(R.id.stg2);
        final TextView lotg = (TextView) view.findViewById(R.id.ltg);

        final TextView dayornight = (TextView) view.findViewById(R.id.dayornight);
        final TextView purl= (TextView) view.findViewById(R.id.linkedIdProfileUrl);
        final TextView instatv= (TextView) view.findViewById(R.id.instagram);
        final TextView twittertv= (TextView) view.findViewById(R.id.twitter);
        final TextView othertv= (TextView) view.findViewById(R.id.other);
        final TextView skillsuknowtv= (TextView) view.findViewById(R.id.skills);
        final TextView day= (TextView) view.findViewById(R.id.dayornight);

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                com.example.peerpowerclub.models.user user4profile = snapshot.getValue(user.class);
                if (user4profile != null) {
                     dayNight= user4profile.prefferedTime;
                    ltg = user4profile.ltg;
                    stg = user4profile.stg;

                    prl = user4profile.profileurl;
                    email = user.getEmail();
                     String phone = user4profile.phonenumber;
                     areaofinterest = user4profile.areaOfInterest;

                     String name = user4profile.fullname;
                     insta = user4profile.insta;
                     twitter = user4profile.twitter;
                     others = user4profile.others;
                     timecall = user4profile.timecall;
                     skillsknow = user4profile.skillsknow;


                    fullnameTextView.setText("Name: " + name);
                    emailTextView.setText("Email:  " + email);
                    phonenumberTextView.setText("Phone Number: " + phone);
                    prefferedTextView.setText("Preffered Time: " + timecall);
                    areaofInterestTextView.setText(areaofinterest);

                    shtg.setText("short term goal:" + user4profile.stg);
                    lotg.setText("long term goal:" + user4profile.ltg);
                    instatv.setText("Instagram:" + insta);
                            twittertv.setText("twitter:" + twitter);
                    othertv.setText("others:" + others);
                            skillsuknowtv.setText( skillsknow);
                    day.setText(user4profile.prefferedTime);
                    purl.setText("linkedIn:" + user4profile.profileurl);
                    Picasso.get().load(user4profile.imageuri).into(profilephoto);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        return view;


    }

}
