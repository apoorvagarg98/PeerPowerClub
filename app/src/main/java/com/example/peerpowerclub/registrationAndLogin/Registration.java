
package com.example.peerpowerclub.registrationAndLogin;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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
import com.example.peerpowerclub.fragmentCodes.Fragment1;
import com.example.peerpowerclub.fragmentCodes.Fragment2Chat;
import com.example.peerpowerclub.models.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registration extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText editTextname,  editTextphonenumber,language,profileurl,stg,ltg;
    private TextView registeruser;
    public static  final int REQUEST_CODE = 101;
    public static Uri imageUri;
    public CircleImageView profilePhoto;
    StorageReference profilePhotoImageRef;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    ToggleButton toggleButton;
  public static String AreaofInterest = "automobile",daynight = "day";
    ArrayAdapter<CharSequence> adapter;
    DatabaseReference groups;

    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        profilePhoto = findViewById(R.id.groupwalekaphoto);
        language = findViewById(R.id.languages);
        profileurl = findViewById(R.id.linkedIdProfileUrl);
        ltg = findViewById(R.id.longTermGoal);
        stg = findViewById(R.id.shortTermGoal);
        registeruser = (Button) findViewById(R.id.preg);
        profilePhotoImageRef = FirebaseStorage.getInstance().getReference().child("profilePhotos");
        registeruser.setOnClickListener(this);
        editTextname = (EditText) findViewById(R.id.name);

        editTextphonenumber = (EditText) findViewById(R.id.contact);

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

profilePhoto.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent , REQUEST_CODE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUri = data.getData();
        profilePhoto.setImageURI(imageUri);
    }

    private void Registeruser() {

        String name = editTextname.getText().toString();
        String phone = editTextphonenumber.getText().toString();
        String profilekaurl = profileurl.getText().toString();
        String longtm = ltg.getText().toString();
        String shorttm= stg.getText().toString();
        String languageSpoken = language.getText().toString();


        if (imageUri == null) {
            Toast.makeText(this, "please select an image", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.isEmpty()) {
            editTextname.setError("full name required");
            editTextname.requestFocus();
            return;
        }
        if (profilekaurl.isEmpty()) {
            profileurl.setError("profile url required");
            profileurl.requestFocus();
            return;
        }
        if (shorttm.isEmpty()) {
            stg.setError("short Term Goal required");
            stg.requestFocus();
            return;
        }
        if (longtm.isEmpty()) {
            ltg.setError("Long Term Goal required");
            ltg.requestFocus();
            return;
        }
        if (languageSpoken.isEmpty()) {
           language.setError("languages Spoken required");
            language.requestFocus();
            return;
        }


        if (phone.isEmpty()) {
            editTextphonenumber.setError("phonenumber required");
            editTextphonenumber.requestFocus();
            return;
        }



        String status = "unenrolled";
        final user[] useR = new user[1];

        profilePhotoImageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    profilePhotoImageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Toast.makeText(Registration.this, "done", Toast.LENGTH_LONG).show();
                            useR[0] = new user(name,  phone, AreaofInterest, daynight, status, uri.toString(),profilekaurl,shorttm,longtm,languageSpoken);
                            FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(useR[0]).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(Registration.this, "details added", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(Registration.this, "details addition failed", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                            FirebaseDatabase.getInstance().getReference("groups").child(AreaofInterest + daynight).child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(useR[0]).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(Registration.this, "added in group", Toast.LENGTH_SHORT).show();
                                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new Fragment2Chat()).commit();


                                        startActivity(new Intent(Registration.this, Login.class));
                                    } else {
                                        Toast.makeText(Registration.this, "group addition failed", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });


                        }
                    });
                }
            }
        });

                    /*groups.child(AreaofInterest + daynight).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(useR[0]).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Registration.this, "registered fully", Toast.LENGTH_LONG).show();


                        }
                    });*/


    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
      AreaofInterest = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
