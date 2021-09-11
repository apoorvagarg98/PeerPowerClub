package com.example.peerpowerclub.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peerpowerclub.R;
import com.example.peerpowerclub.models.feedModel;

import java.util.ArrayList;

public class feedAdapter extends RecyclerView.Adapter<feedAdapter.ViewHolder> {
    public static Object ViewHolder;
    public ArrayList<feedModel> arrayList ;
    public feedAdapter(ArrayList<feedModel> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
feedModel feedmodel = arrayList.get(position);
//holder.caption.setText(feedmodel.getTitle());
//holder.name.setText(feedmodel.getname());
//holder.postImage.setImageURI(feedmodel.getPostImage());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //ImageView postImage;
       // TextView name, caption;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           // postImage = itemView.findViewById(R.id.feedimage);
          //  name = itemView.findViewById(R.id.nameonfeed);
           // caption = itemView.findViewById(R.id.captioninsert);
        }
    }
}
