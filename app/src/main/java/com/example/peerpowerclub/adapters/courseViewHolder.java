package com.example.peerpowerclub.adapters;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peerpowerclub.R;

public class courseViewHolder extends RecyclerView.ViewHolder{
    public TextView longdesc,coursename;
    public ImageView courseimage;

    public courseViewHolder(@NonNull View itemView) {
        super(itemView);

        courseimage = itemView.findViewById(R.id.courseImage);
        coursename = itemView.findViewById(R.id.CourseName);


    }
}
