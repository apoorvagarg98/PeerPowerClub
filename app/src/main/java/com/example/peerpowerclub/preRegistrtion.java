package com.example.peerpowerclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peerpowerclub.registrationAndLogin.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class preRegistrtion extends AppCompatActivity {
TextView preemail,prepass,prepass2;
    private FirebaseAuth mAuth;
Button preregister;
    public static Uri imageUri;
    public static  final int REQUEST_CODE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_registrtion);
        preemail = findViewById(R.id.preemail);
        prepass = findViewById(R.id.prepassword);
        prepass2 = findViewById(R.id.prepassword2);
        mAuth = FirebaseAuth.getInstance();
        preregister = findViewById(R.id.yo);
preregister.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Registeruser();
    }
});

    }




    private void Registeruser() {
        String email =preemail.getText().toString();
        String password = prepass.getText().toString();
        String chpassword = prepass2.getText().toString();
        if(email.isEmpty())
        {
            preemail.setError("email required");
            preemail.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            prepass2.setError("password required");
            prepass2.requestFocus();
            return;
        }
        if(!password.equals(chpassword)) {
            prepass2.setError("Passwords do not match.Kindly recheck");
            prepass2.requestFocus();
            return;

        }
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                HashMap hashMap = new HashMap();
                hashMap.put("email",email);

                hashMap.put("areaOfInterest","");
                FirebaseUser user;
                user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase.getInstance().getReference("users").child(user.getUid())
                        .setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                        Toast.makeText(preRegistrtion.this, "done", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(preRegistrtion.this, Login.class));}
                        else {
                            Toast.makeText(preRegistrtion.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });



    }
}