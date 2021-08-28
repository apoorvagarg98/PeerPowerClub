
package com.example.peerpowerclub;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.peerpowerclub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText editTextname, editTextemail, editTextpassword, editTextphonenumber, chpassword2;
    private TextView registeruser;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    ToggleButton toggleButton;
   static String AreaofInterest = "automobile",daynight = "day";
    ArrayAdapter<CharSequence> adapter;
    DatabaseReference groups;

    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        registeruser = (Button) findViewById(R.id.preg);
        registeruser.setOnClickListener(this);
        editTextname = (EditText) findViewById(R.id.name);
        editTextemail = (EditText) findViewById(R.id.emailreg);
        editTextpassword = (EditText) findViewById(R.id.inputPassword);
        editTextphonenumber = (EditText) findViewById(R.id.contact);
        chpassword2 = (EditText) findViewById(R.id.inputConfirmPassword);
        toggleButton = findViewById(R.id.dayNight);
        groups = FirebaseDatabase.getInstance().getReference().child("groups");
        spinner = findViewById(R.id.interest);
         adapter = ArrayAdapter.createFromResource(this,R.array.subjects, android.R.layout.simple_spinner_item);
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         spinner.setAdapter(adapter);
         spinner.setOnItemSelectedListener(this);

         toggleButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(toggleButton.isChecked())
                 {
                     daynight = "Night";

                 }
                 else {
                     daynight = "day";
                 }

             }
         });



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.preg:
                Registeruser();
        }
    }

    private void Registeruser()
    {
        String email = editTextemail.getText().toString();

        String name = editTextname.getText().toString();
        String phone = editTextphonenumber.getText().toString();
        String password = editTextpassword.getText().toString();
        String chpassword = chpassword2.getText().toString();


        if(name.isEmpty())
        {
            editTextname.setError("full name required");
            editTextname.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            editTextpassword.setError("password required");
            editTextpassword.requestFocus();
            return;
        }

        if(phone.isEmpty())
        {
            editTextphonenumber.setError("phonenumber required");
            editTextphonenumber.requestFocus();
            return;
        }
        if(email.isEmpty())
        {
            editTextemail.setError("email required");
            editTextemail.requestFocus();
            return;
        }
        if(!password.equals(chpassword)) {
            chpassword2.setError("Passwords do not match.Kindly recheck");
            chpassword2.requestFocus();
            return;

        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextemail.setError("please provide valid email");
            editTextemail.requestFocus();
            return;

        }
        if(password.length()<6)
        {
            editTextpassword.setError("min pass require length should be 6");
            editTextpassword.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user useR = new user(name, email, phone, AreaofInterest,daynight);
                    FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(useR).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Registration.this, "registered", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(Registration.this,Login.class));
                            } else {
                                Toast.makeText(Registration.this, "Registration Failed", Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                    groups.child(AreaofInterest).child(daynight).setValue(useR).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Registration.this, "registered fully", Toast.LENGTH_LONG).show();


                        }
                    });


                } else {
                    Toast.makeText(Registration.this, "registration failed", Toast.LENGTH_LONG).show();

                }
            }

        });}

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
      AreaofInterest = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
