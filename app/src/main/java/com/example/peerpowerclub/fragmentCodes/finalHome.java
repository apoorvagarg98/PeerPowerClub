package com.example.peerpowerclub.fragmentCodes;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peerpowerclub.R;
import com.example.peerpowerclub.adapters.courseViewHolder;
import com.example.peerpowerclub.adapters.item2ViewHolder;
import com.example.peerpowerclub.adapters.myViewHolder;
import com.example.peerpowerclub.groupparticipants;
import com.example.peerpowerclub.joinchat;
import com.example.peerpowerclub.models.coursemodel;
import com.example.peerpowerclub.models.feedModel;
import com.example.peerpowerclub.models.user;
import com.example.peerpowerclub.myHome;
import com.example.peerpowerclub.userProfile;
import com.example.peerpowerclub.viewCourse;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class finalHome extends Fragment {
    public FirebaseUser user;
    public DatabaseReference reference,postref;
    public String userId;

   public RecyclerView recyclerView;

    public DatabaseReference courseref;
    public RecyclerView recyclerViewpaidcoursewala,freewala;
    FirebaseRecyclerAdapter<coursemodel, item2ViewHolder> adapter1;
    FirebaseRecyclerOptions<coursemodel> options2;

    StorageReference courseImageRef;


    public static Uri imageUri;
    FirebaseRecyclerAdapter<feedModel,myViewHolder>adapter;
    FirebaseRecyclerOptions<feedModel> options;
    ProgressDialog mLoadingBar;
    StorageReference postImageRef;



    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_final_home,container,false);
       recyclerView = view.findViewById(R.id.rv);
     //feedadapter= new feedAdapter();
     recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewpaidcoursewala = view.findViewById(R.id.paidcoursewalarcv);
       freewala = view.findViewById(R.id.freewala);
        recyclerViewpaidcoursewala.setLayoutManager(layoutManager);
        freewala.setLayoutManager(layoutManager2);
TextView viewMorePaid = view.findViewById(R.id.viewMorePaid);
viewMorePaid.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        getFragmentManager().beginTransaction().replace(R.id.frame_layout,new Fragment3courses()).commit();

    }
});
        courseImageRef = FirebaseStorage.getInstance().getReference().child("courseimages");
        mLoadingBar = new ProgressDialog(getActivity());
        courseref = FirebaseDatabase.getInstance().getReference("courses");
loadfreecourse();
        loadCourse();

    /* attatchpost = view.findViewById(R.id.insertImage);
     sendpost = view.findViewById(R.id.sendpost);
     addcaption = view.findViewById(R.id.captioninsert);*/
     postImageRef = FirebaseStorage.getInstance().getReference().child("postImages");
     mLoadingBar = new ProgressDialog(getActivity());
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        postref = FirebaseDatabase.getInstance().getReference("posts");
        userId = user.getUid();

        loadPost();

       /* sendpost.setOnClickListener(new View.OnClickListener() {
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
                try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                    } else {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, REQUEST_CODE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });  */


        return view;

    }

    private void loadfreecourse() {
        options2 = new FirebaseRecyclerOptions.Builder<coursemodel>().setQuery(courseref,coursemodel.class).build();
        adapter1=  new FirebaseRecyclerAdapter<coursemodel,item2ViewHolder>(options2){
            @NonNull
            @Override
            public item2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2,parent,false);

                return new item2ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull item2ViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull coursemodel model) {



                Picasso.get().load(model.getCourseimageuri()).into(holder.courseimage);
                holder.courseimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.child("areaOfInterest").getValue().equals("")){
                                    startActivity(new Intent(getActivity(), joinchat.class));
                                }
                                else{
                                    getFragmentManager().beginTransaction().replace(R.id.frame_layout,new Fragment2Chat()).commit();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                    }
                });
            }
        };
        adapter1.startListening();
       freewala.setAdapter(adapter1);
    }

    private void loadCourse()

    {
        options2 = new FirebaseRecyclerOptions.Builder<coursemodel>().setQuery(courseref,coursemodel.class).build();
        adapter1=  new FirebaseRecyclerAdapter<coursemodel,item2ViewHolder>(options2){
            @NonNull
            @Override
            public item2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2,parent,false);

                return new item2ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull item2ViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull coursemodel model) {



                Picasso.get().load(model.getCourseimageuri()).into(holder.courseimage);
                holder.courseimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getActivity(), viewCourse.class);
                        intent.putExtra("courseName", getRef(position).getKey().toString());
                        intent.putExtra("cn", model.getCourseName());
                        intent.putExtra("ci", model.getCourseimageuri());
                        intent.putExtra("cld", model.getCourseLongDescription());
                        intent.putExtra("gl", model.getGroupLink());


                        startActivity(intent);

                    }
                });
            }
        };
        adapter1.startListening();
        recyclerViewpaidcoursewala.setAdapter(adapter1);
    }

    private void loadPost() {
        options = new FirebaseRecyclerOptions.Builder<feedModel>().setQuery(postref,feedModel.class).build();
        adapter =  new FirebaseRecyclerAdapter<feedModel,myViewHolder>(options){
            @NonNull
            @Override
            public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);

                return new myViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull feedModel model) {

                holder.name.setText(model.getname());
                holder.caption.setText(model.getcaption());
                Picasso.get().load(model.getProfileimageuri()).into(holder.profilePhoto);

                Picasso.get().load(model.getPostImageUrl()).into(holder.postImage);


                holder.name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), userProfile.class);
                        intent.putExtra("uid",model.uid);
                        startActivity(intent);
                    }
                });
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }


   /* public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Fragment fragment = getChildFragmentManager().findFragmentByTag("1");
        //fragment.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE && requestCode==RESULT_OK && data!=null)
        {

         imageUri = data.getData();
            attatchpost.setImageURI(finalHome.imageUri);


        }
    }

    private void addpost() {
        String postcap = addcaption.getText().toString();
        if(postcap.isEmpty()){
            addcaption.setError("please add caption");
        }
        else if(imageUri==null){
            Toast.makeText(getActivity(), "please select an image", Toast.LENGTH_SHORT).show();
        }
        else{

            mLoadingBar.setTitle("Adding post");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();
            postImageRef.child(userId).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        postImageRef.child(userId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                            @Override
                            public void onSuccess(Uri uri) {
                                Date date = new Date();

                                    @SuppressLint({"NewApi", "LocalSuppress"}) SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY hh:mm:ss");

                                String strDate = formatter.format(date);
                                HashMap hashMap = new HashMap();
                                hashMap.put("datePost",strDate);
                                hashMap.put("postImageUrl",uri.toString());
                                hashMap.put("caption",postcap);
                                postref.child(userId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            mLoadingBar.dismiss();
                                            Toast.makeText(getActivity(), "post added", Toast.LENGTH_SHORT).show();
                                            attatchpost.setImageURI(null);
                                            addcaption.setText("");
                                        }else{
                                            Toast.makeText(getActivity(), "" + task.getException().toString(), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                            }
                        });
                    }else{
                        mLoadingBar.dismiss();
                        Toast.makeText(getActivity(), "" + task.getException().toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }



    }*/
}