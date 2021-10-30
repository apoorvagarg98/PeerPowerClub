package com.example.peerpowerclub.fragmentCodes;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peerpowerclub.R;
import com.example.peerpowerclub.adapters.courseViewHolder;
import com.example.peerpowerclub.adapters.myViewHolder;
import com.example.peerpowerclub.models.coursemodel;
import com.example.peerpowerclub.models.feedModel;
import com.example.peerpowerclub.viewCourse;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Fragment3courses extends Fragment {
    public DatabaseReference courseref;
    public RecyclerView recyclerView;
    FirebaseRecyclerAdapter<coursemodel, courseViewHolder> adapter;
    FirebaseRecyclerOptions<coursemodel> options;
    ProgressDialog mLoadingBar;
    StorageReference courseImageRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3courses,container,false);
        LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);

        recyclerView = view.findViewById(R.id.courserv);
        recyclerView.setLayoutManager(layoutManager);
        courseImageRef = FirebaseStorage.getInstance().getReference().child("courseimages");
        mLoadingBar = new ProgressDialog(getActivity());
        courseref = FirebaseDatabase.getInstance().getReference("courses");
        loadCourse();
        return view;

    }

    private void loadCourse() {
        options = new FirebaseRecyclerOptions.Builder<coursemodel>().setQuery(courseref,coursemodel.class).build();
        adapter =  new FirebaseRecyclerAdapter<coursemodel,courseViewHolder>(options){
            @NonNull
            @Override
            public courseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_courses_item,parent,false);

                return new courseViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull courseViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull coursemodel model) {


holder.coursename.setText(model.getCourseName());
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
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}
