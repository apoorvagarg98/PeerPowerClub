package com.example.peerpowerclub.fragmentCodes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peerpowerclub.Chatmenusettings;
import com.example.peerpowerclub.adapters.AdapterGroupChat;
import com.example.peerpowerclub.R;
import com.example.peerpowerclub.faltuKaInfo;
import com.example.peerpowerclub.groupparticipants;
import com.example.peerpowerclub.models.modelGroupChat;
import com.example.peerpowerclub.models.user;
import com.example.peerpowerclub.registrationAndLogin.Registration;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Fragment2Chat extends Fragment {
    public FirebaseAuth mAuth;
    public FirebaseUser user;
    public DatabaseReference reference,userref;
    public ImageButton attachbtn,sendbtn;
    public TextView groupTitleTv;
    public EditText messageEt;
    Toolbar toolbar;
    RecyclerView chatRv;
    String dayNight, areaofinterest;
    public ArrayList<modelGroupChat> groupChatList;
    public AdapterGroupChat adapterGroupChat;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment2chat,container,false);
        mAuth = FirebaseAuth.getInstance();
        toolbar = view.findViewById(R.id.toolbar);
        groupTitleTv = view.findViewById(R.id.groupName);
        attachbtn = view.findViewById(R.id.attachbtn);
        sendbtn = view.findViewById(R.id.sendbtn);
        chatRv = view.findViewById(R.id.chatRv);
       userref= FirebaseDatabase.getInstance().getReference("users");
        messageEt = view.findViewById(R.id.messageEt);
        user = FirebaseAuth.getInstance().getCurrentUser();

        loadGroupInfo();
        loaadGroupMessges();
        groupTitleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), groupparticipants.class);
                intent.putExtra("gt",areaofinterest + dayNight);
                startActivity(intent);
            }
        });
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String message = messageEt.getText().toString().trim();
            if(TextUtils.isEmpty(message))
            {
                Toast.makeText(getActivity(), "pls enter message", Toast.LENGTH_SHORT).show();

            }
            else {
                sendmsg(message);
            }
            }
        });
        return view;
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
         menuInflater.inflate(R.menu.menuforchat,menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.Groupmembers:
            {
                Intent intent = new Intent(getActivity(), groupparticipants.class);
                intent.putExtra("gt",areaofinterest + dayNight);
                startActivity(intent);
                break;
            }
            case R.id.Menuofchat:{
                Intent intent = new Intent(getActivity(), Chatmenusettings.class);

                startActivity(intent);
                break;
            }
            case R.id.infowala:{
                Intent intent = new Intent(getActivity(), faltuKaInfo.class);
                startActivity(intent);
                break;
            }



        }

        return super.onOptionsItemSelected(item);
    }

    public void loaadGroupMessges() {


        userref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                com.example.peerpowerclub.models.user user4profile = snapshot.getValue(com.example.peerpowerclub.models.user.class);
                dayNight= user4profile.prefferedTime;

                areaofinterest = user4profile.areaOfInterest;
                groupChatList = new ArrayList<>();
                reference = FirebaseDatabase.getInstance().getReference().child("groups");
                reference.child(areaofinterest+dayNight).child("messages").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                groupChatList.clear();
                for(DataSnapshot ds: datasnapshot.getChildren()){
                    modelGroupChat model = ds.getValue(modelGroupChat.class);
                    groupChatList.add(model);

                }
                adapterGroupChat = new AdapterGroupChat(getActivity(),groupChatList);
                chatRv.setAdapter(adapterGroupChat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
             }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}});


    }

    public void sendmsg(String message) {

        String timestamp = "" + System.currentTimeMillis();
        HashMap<String, Object> hashMap= new HashMap();
        hashMap.put("sender","" + mAuth.getUid());
        hashMap.put("message","" +message);
        hashMap.put("timestamp","" + timestamp);
        hashMap.put("type","" + "text");//text/image/file
        reference = FirebaseDatabase.getInstance().getReference().child("groups");
        reference.child(groupTitleTv.getText().toString()).child("messages").child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //message sent
                messageEt.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void loadGroupInfo() {
        userref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                com.example.peerpowerclub.models.user user4profile = snapshot.getValue(com.example.peerpowerclub.models.user.class);
               dayNight= user4profile.prefferedTime;

              areaofinterest = user4profile.areaOfInterest;
                groupTitleTv.setText(areaofinterest + dayNight);}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}});


       /* reference = FirebaseDatabase.getInstance().getReference().child("groups");
        reference.orderByChild("email").equalTo(Fragment1.email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for (DataSnapshot ds:datasnapshot.getChildren())
                {
                    String groupTitle = Fragment1.areaofinterest + Fragment1.dayNight;
                    groupTitleTv.setText(Fragment1.areaofinterest + Fragment1.dayNight);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

    }



    private void learnToMakeADialogue()
    { String[] options = {"yoyoooo","ll"};
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        builder.setTitle("yoyo").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }
}
