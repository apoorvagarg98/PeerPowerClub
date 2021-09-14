
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

    private EditText editTextname, editTextemail, editTextpassword, editTextphonenumber, chpassword2;
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
        registeruser = (Button) findViewById(R.id.preg);
        profilePhotoImageRef = FirebaseStorage.getInstance().getReference().child("profilePhotos");
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

    private void Registeruser()
    {
        String email = editTextemail.getText().toString();
        String profilephotouri= imageUri.toString();
        String name = editTextname.getText().toString();
        String phone = editTextphonenumber.getText().toString();
        String password = editTextpassword.getText().toString();
        String chpassword = chpassword2.getText().toString();

if(imageUri==null)
{
    Toast.makeText(this, "please select an image", Toast.LENGTH_SHORT).show();
}
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
                    String status = "unenrolled";
                    final user[] useR = new user[1];

                    profilePhotoImageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                profilePhotoImageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Toast.makeText(Registration.this, "done", Toast.LENGTH_LONG).show();
                                        useR[0] = new user(name, email, phone, AreaofInterest,daynight,FirebaseAuth.getInstance().getCurrentUser().getUid(),status,uri.toString());
                                        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(useR[0]).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    Toast.makeText(Registration.this, "registered", Toast.LENGTH_SHORT).show();

                                                    startActivity(new Intent(Registration.this, Login.class));
                                                } else {
                                                    Toast.makeText(Registration.this, "Registration Failed1", Toast.LENGTH_LONG).show();

                                                }
                                            }
                                        });
                                        FirebaseDatabase.getInstance().getReference("groups").child(AreaofInterest + daynight).child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(useR[0]).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    Toast.makeText(Registration.this, "registered fully", Toast.LENGTH_SHORT).show();

                                                    startActivity(new Intent(Registration.this, Login.class));
                                                } else {
                                                    Toast.makeText(Registration.this, "Registration Failed2", Toast.LENGTH_LONG).show();

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


                } else {
                    Toast.makeText(Registration.this, "registration failed3", Toast.LENGTH_LONG).show();

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
