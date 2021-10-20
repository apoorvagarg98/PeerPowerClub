
package com.example.peerpowerclub.registrationAndLogin;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peerpowerclub.R;
import com.example.peerpowerclub.fragmentCodes.Fragment2Chat;
import com.example.peerpowerclub.models.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registration extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText editTextname,  editTextphonenumber,profileurl,stg,ltg,instapro,twitterpro,otherpro;
    private TextView registeruser;
    public static  final int REQUEST_CODE = 101;
    public static Uri imageUri;

    public CircleImageView profilePhoto;
    StorageReference profilePhotoImageRef;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    FirebaseUser user;

  public static String AreaofInterest = "automobile",daynight = "sunday",timecall = " 8am-10am",skillsknow="python";
    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter<CharSequence> adapter2;
    ArrayAdapter<CharSequence> adapter3;
    ArrayAdapter<CharSequence> adapter4;
    DatabaseReference groups;

    Spinner spinner,dayskaspin,timeforcall,skillsuknow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        profilePhoto = findViewById(R.id.groupwalekaphoto);
        user = FirebaseAuth.getInstance().getCurrentUser();

        skillsuknow= findViewById(R.id.skills);
        profileurl = findViewById(R.id.linkedIdProfileUrl);
        ltg = findViewById(R.id.ltg);
        stg = findViewById(R.id.stg2);
        instapro = findViewById(R.id.instagram);
        twitterpro = findViewById(R.id.twitter);
        otherpro = findViewById(R.id.other);
        timeforcall= findViewById(R.id.timeforcall);
        registeruser = (Button) findViewById(R.id.preg);
        profilePhotoImageRef = FirebaseStorage.getInstance().getReference().child("profilePhotos");
        registeruser.setOnClickListener(this);
        editTextname = (EditText) findViewById(R.id.name);

        editTextphonenumber = (EditText) findViewById(R.id.contact);

        dayskaspin = findViewById(R.id.dayornight);
        groups = FirebaseDatabase.getInstance().getReference().child("groups");
        spinner = findViewById(R.id.Skillstolearn);
         adapter = ArrayAdapter.createFromResource(this,R.array.subjects, android.R.layout.simple_spinner_item);
         adapter2 = ArrayAdapter.createFromResource(this,R.array.dayornight, android.R.layout.simple_spinner_item);
         adapter3= ArrayAdapter.createFromResource(this,R.array.time, android.R.layout.simple_spinner_item);
         adapter4= ArrayAdapter.createFromResource(this,R.array.skills, android.R.layout.simple_spinner_item);
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         spinner.setAdapter(adapter);
        dayskaspin.setAdapter(adapter2);
        timeforcall.setAdapter(adapter3);
        skillsuknow.setAdapter(adapter4);
         spinner.setOnItemSelectedListener(this);
        dayskaspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                daynight = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(Registration.this, "please select", Toast.LENGTH_SHORT).show();

            }
        });
        timeforcall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                timecall = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(Registration.this, "please select", Toast.LENGTH_SHORT).show();

            }
        });
        skillsuknow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                skillsknow=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(Registration.this, "please select", Toast.LENGTH_SHORT).show();

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
        String insta= instapro.getText().toString();
        String twitter= twitterpro.getText().toString();
        String others= otherpro.getText().toString();


        if (imageUri == null) {
            Toast.makeText(this, "please select an image", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.isEmpty()) {
            editTextname.setError("full name required");
            editTextname.requestFocus();
            return;
        }
        if (insta.isEmpty()) {
            instapro.setError("insta profile required");
            instapro.requestFocus();
            return;
        }
        if (twitter.isEmpty()) {
            twitterpro.setError("twitter profile required");
            twitterpro.requestFocus();
            return;
        }
        if (others.isEmpty()) {
            otherpro.setError("full name required");
           otherpro.requestFocus();
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
                            useR[0] = new user(name,  phone, AreaofInterest, daynight, status, uri.toString(),profilekaurl,shorttm,longtm,user.getUid().toString(),insta,twitter,others,timecall ,skillsknow);
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
      //  Intent intent = new Intent(this, BroadcastService.class);
      //  startService(intent);
      //  Log.d("hello","startedService");



    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
      AreaofInterest = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(Registration.this, "please select", Toast.LENGTH_SHORT).show();

    }
}
