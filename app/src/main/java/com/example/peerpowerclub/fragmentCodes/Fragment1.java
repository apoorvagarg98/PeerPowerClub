package com.example.peerpowerclub.fragmentCodes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.peerpowerclub.R;
import com.example.peerpowerclub.models.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment1 extends Fragment {
    private FirebaseUser user;
    public DatabaseReference reference;
    private String userId;
    static  String areaofinterest,dayNight,email;

    private Button buttons,logout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1home,container,false);
     /*   logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                getActivity().finish();

            }
        });*/

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userId = user.getUid();
        final TextView fullnameTextView = (TextView) view.findViewById(R.id.name);
        final TextView emailTextView = (TextView) view.findViewById(R.id.Email);
        final TextView phonenumberTextView = (TextView) view.findViewById(R.id.phonenumber);
        final TextView prefferedTextView = (TextView) view.findViewById(R.id.prefferedtime);
        final TextView areaofInterestTextView = (TextView) view.findViewById(R.id.areaofinterest);
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                com.example.peerpowerclub.models.user user4profile = snapshot.getValue(user.class);
                if (user4profile != null) {
                    dayNight= user4profile.prefferedTime;
                     email = user4profile.email;
                    String phone = user4profile.phonenumber;
                    areaofinterest = user4profile.areaOfInterest;

                    String name = user4profile.fullname;

                    fullnameTextView.setText("Name - " + name);
                    emailTextView.setText("Email - " + email);
                    phonenumberTextView.setText("PhoneNumber- " + phone);
                    prefferedTextView.setText("prefferedTime- " + dayNight);
                    areaofInterestTextView.setText("areOfInterest - " + areaofinterest);

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
