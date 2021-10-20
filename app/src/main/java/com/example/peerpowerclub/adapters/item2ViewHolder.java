package com.example.peerpowerclub.adapters;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peerpowerclub.R;

public class item2ViewHolder extends RecyclerView.ViewHolder{
    public ImageView courseimage;
    public item2ViewHolder(@NonNull View itemView) {
        super(itemView);
        courseimage= itemView.findViewById(R.id.imageView3);
    }
}
