package com.example.peerpowerclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.peerpowerclub.adapters.grpviewholder;
import com.example.peerpowerclub.adapters.myViewHolder;
import com.example.peerpowerclub.models.feedModel;
import com.example.peerpowerclub.models.user;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class groupparticipants extends AppCompatActivity {
    public RecyclerView recyclerView;
    public FirebaseUser user;
    public DatabaseReference group,profiletref;
    public static Uri imageUri;
    String userId;
    FirebaseRecyclerAdapter<user, grpviewholder> adapter;
    FirebaseRecyclerOptions<user> options;
    ProgressDialog mLoadingBar;
    String groupname;
    StorageReference profileImageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupparticipants);
        recyclerView = findViewById(R.id.grp);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        profileImageRef = FirebaseStorage.getInstance().getReference().child("profilePhotos");
        mLoadingBar = new ProgressDialog(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        TextView na =  findViewById(R.id.na);
      //  group= FirebaseDatabase.getInstance().getReference("groups");
        groupname= getIntent().getStringExtra("gt");
        profiletref = FirebaseDatabase.getInstance().getReference("groups").child(groupname).child("users");
na.setText(groupname);
        userId = user.getUid();
        loadParticipant();



    }

    private void loadParticipant() {

        options = new FirebaseRecyclerOptions.Builder<user>().setQuery(profiletref,user.class).build();
        adapter =  new FirebaseRecyclerAdapter<user,grpviewholder>(options){
            @NonNull
            @Override
            public grpviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.groupparticipantsitem,parent,false);

                return new grpviewholder(view);

            }

            @Override
            protected void onBindViewHolder(@NonNull grpviewholder holder, @SuppressLint("RecyclerView") int position, @NonNull com.example.peerpowerclub.models.user model) {

                holder.nameingroup.setText(model.fullname);
                Picasso.get().load(model.imageuri).into(holder.pf);
                holder.nameingroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(groupparticipants.this,userProfile.class);
                        intent.putExtra("uid",model.uid);
                        startActivity(intent);
                    }
                });

            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}