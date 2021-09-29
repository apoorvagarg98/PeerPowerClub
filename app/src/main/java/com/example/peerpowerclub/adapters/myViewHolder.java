package com.example.peerpowerclub.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peerpowerclub.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class myViewHolder extends RecyclerView.ViewHolder {
    public ImageView postImage;
   public TextView name, caption;
  public CircleImageView profilePhoto;
    public myViewHolder(@NonNull View itemView) {

        super(itemView);
        postImage = itemView.findViewById(R.id.feedimage);
        name = itemView.findViewById(R.id.loo);
        caption = itemView.findViewById(R.id.caption);
       profilePhoto= itemView.findViewById(R.id.feedprofilephoto);
    }
}
