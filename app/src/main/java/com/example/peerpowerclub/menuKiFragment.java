package com.example.peerpowerclub;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.peerpowerclub.registrationAndLogin.Login;
import com.google.firebase.auth.FirebaseAuth;


public class menuKiFragment extends Fragment {

    private TextView logout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_ki,container,false);
        logout= view.findViewById(R.id.logoutfr);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getActivity(), Login.class);

                startActivity(intent);

                getActivity().finish();

            }
        });
        return view;

    }
}