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
    static  String areaofinterest,dayNight,email,ltg,stg,ln,prl;
    public CircleImageView profilephoto;



    private Button buttons,logout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1home,container,false);
        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getActivity(), Login.class);

                startActivity(intent);

                getActivity().finish();

            }
        });

        profilephoto = view.findViewById(R.id.profile_image);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userId = user.getUid();
        final TextView fullnameTextView = (TextView) view.findViewById(R.id.name);
        final TextView emailTextView = (TextView) view.findViewById(R.id.Email);
        final TextView phonenumberTextView = (TextView) view.findViewById(R.id.phonenumber);
        final TextView prefferedTextView = (TextView) view.findViewById(R.id.prefferedtime);
        final TextView areaofInterestTextView = (TextView) view.findViewById(R.id.areaofinterest);
        final TextView status = (TextView) view.findViewById(R.id.status);
        final TextView shtg = (TextView) view.findViewById(R.id.stg);
        final TextView lotg = (TextView) view.findViewById(R.id.ltg);
        final TextView lng = (TextView) view.findViewById(R.id.lng);
        final TextView purl= (TextView) view.findViewById(R.id.profileurl);
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                com.example.peerpowerclub.models.user user4profile = snapshot.getValue(user.class);
                if (user4profile != null) {
                     dayNight= user4profile.prefferedTime;
                    ltg = user4profile.ltg;
                    stg = user4profile.stg;
                    ln = user4profile.language;
                    prl = user4profile.profileurl;
                    email = user.getEmail();
                     String phone = user4profile.phonenumber;
                     areaofinterest = user4profile.areaOfInterest;

                     String name = user4profile.fullname;

                    fullnameTextView.setText("Name: " + name);
                    emailTextView.setText("Email:  " + email);
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
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        return view;


    }

}
