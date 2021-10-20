package com.example.peerpowerclub.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peerpowerclub.R;

public class grpviewholder  extends RecyclerView.ViewHolder {
public ImageView pf;
public TextView nameingroup;
    public grpviewholder(@NonNull View itemView) {
        super(itemView);
        pf = itemView.findViewById(R.id.groupwalekaphoto);
        nameingroup = itemView.findViewById(R.id.groupParticipantname);


    }
}
