package com.example.peerpowerclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class postkaro extends AppCompatActivity {
    public static  final int REQUEST_CODE = 101;
    public static ImageView attatchpost,sendpost,badaPost;
    public EditText addcaption;
    public static Uri imageUri;
    ProgressDialog mLoadingBar;
    StorageReference postImageRef;
    public static  String areaofinterest,dayNight,email,name;
    public FirebaseUser user;
    public DatabaseReference reference,postref;
    public String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postkaro);
        attatchpost = findViewById(R.id.insertImage);
        badaPost = findViewById(R.id.imageView);
        sendpost = findViewById(R.id.sendpost);
        addcaption = findViewById(R.id.name);
        postImageRef = FirebaseStorage.getInstance().getReference().child("postImages");
        mLoadingBar = new ProgressDialog(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        postref = FirebaseDatabase.getInstance().getReference("posts");
        userId = user.getUid();
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                com.example.peerpowerclub.models.user user4profile = snapshot.getValue(com.example.peerpowerclub.models.user.class);
                dayNight= user4profile.prefferedTime;
                //email = user4profile.email;
                areaofinterest = user4profile.areaOfInterest;
                name = user4profile.fullname;

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}});

        sendpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addpost();
            }
        });
        attatchpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent , REQUEST_CODE);
                /*try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                    } else {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, REQUEST_CODE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        });




    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if(requestCode==REQUEST_CODE && requestCode==RESULT_OK && data!=null)
        //{

            imageUri = data.getData();
            attatchpost.setImageURI(imageUri);
            badaPost.setImageURI(imageUri);


       // }
    }

    private void addpost() {
        String postcap = addcaption.getText().toString();
        if(postcap.isEmpty()){
            addcaption.setError("please add caption");
        }
        else if(imageUri==null){
            Toast.makeText(this, "please select an image", Toast.LENGTH_SHORT).show();
        }
        else{

            mLoadingBar.setTitle("Adding post");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();
            Date date = new Date();

            @SuppressLint({"NewApi", "LocalSuppress"}) SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY hh:mm:ss");

            String strDate = formatter.format(date);
            postImageRef.child(userId + strDate).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        postImageRef.child(userId + strDate).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                            @Override
                            public void onSuccess(Uri uri) {

                                HashMap hashMap = new HashMap();
                                hashMap.put("datePost",strDate);
                                hashMap.put("name",name);
                                hashMap.put("postImageUrl",uri.toString());
                                hashMap.put("caption",postcap);


                                postref.child(userId + strDate).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            mLoadingBar.dismiss();
                                            Toast.makeText(postkaro.this, "post added", Toast.LENGTH_SHORT).show();
                                            attatchpost.setImageResource(R.drawable.ic_baseline_add_a_photo_24);
                                           badaPost.setImageURI(null);
                                            addcaption.setText("");
                                        }else{
                                            Toast.makeText(postkaro.this, "" + task.getException().toString(), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                            }
                        });
                    }else{
                        mLoadingBar.dismiss();
                        Toast.makeText(postkaro.this, "" + task.getException().toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }



    }
}