package com.example.peerpowerclub.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peerpowerclub.R;
import com.example.peerpowerclub.models.modelGroupChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AdapterGroupChat extends RecyclerView.Adapter<AdapterGroupChat.HolderGroupChat> {
    
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    
    private Context context;
    private ArrayList<modelGroupChat> modelGroupChatList;
    private FirebaseAuth firebaseAuth;
    public AdapterGroupChat(Context context, ArrayList<modelGroupChat> modelGroupChatList)
    {
        this.context = context;
        this.modelGroupChatList = modelGroupChatList;
        firebaseAuth = FirebaseAuth.getInstance();
        
    }

    @NonNull
    @Override
    public HolderGroupChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if(viewType== MSG_TYPE_RIGHT)
       {
           View view = LayoutInflater.from(context).inflate(R.layout.row_groupchat_right,parent,false);
           return new HolderGroupChat(view);
       }else{
           View view = LayoutInflater.from(context).inflate(R.layout.row_groupchat_left,parent,false);
           return new HolderGroupChat(view);
       }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull HolderGroupChat holder, int position) {
modelGroupChat model = modelGroupChatList.get(position);
String message = model.getMessage();
String timestamp = model.getTimestamp();
String senderUid = model.getSender();

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timestamp));
        Date d = cal.getTime();
        SimpleDateFormat sdf = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sdf = new SimpleDateFormat("dd/MM/YYYY hh:mm");
        }
        String date2 = sdf.format(d);

holder.messagetv.setText(message  );
holder.timetv.setText(date2);
    setUserName(model, holder);
    }

    private void setUserName(modelGroupChat model, HolderGroupChat holder) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.orderByChild("uid").equalTo(model.getSender()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for (DataSnapshot ds : datasnapshot.getChildren())
                {
                    String name =  ds.child("fullname").getValue().toString();
                    holder.nameTv.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return modelGroupChatList.size();
    }

    @Override
    public int getItemViewType(int position) {
     
        if (modelGroupChatList.get(position).getSender().equals(firebaseAuth.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else return MSG_TYPE_LEFT;
    }

    class HolderGroupChat extends RecyclerView.ViewHolder{
private TextView nameTv,messagetv,timetv;
        public HolderGroupChat(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.nameTv);
           messagetv= itemView.findViewById(R.id.messageTv);
            timetv = itemView.findViewById(R.id.timetv);
        }
    }
    
}
