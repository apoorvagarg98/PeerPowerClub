package com.example.peerpowerclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
//import com.payu.base.models.PayUPaymentParams;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

/*import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;*/



import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Properties;

public class viewCourse extends AppCompatActivity  {
    String coursename,grouplink;
    TextView link;
    private FirebaseUser user;
    public DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        coursename= getIntent().getStringExtra("cn");
        grouplink= getIntent().getStringExtra("gl");
        String courseimageuri= getIntent().getStringExtra("ci");
        String courseLongDescription= getIntent().getStringExtra("cld");
        TextView cn = findViewById(R.id.badacoursename);
        TextView cld = findViewById(R.id.LongDescription);
        ImageView ci = findViewById(R.id.bigcourseImage);

        cn.setText(coursename);
        cld.setText(courseLongDescription);
        Picasso.get().load(courseimageuri).into(ci);
        Button btpay = findViewById(R.id.pay);
        String sAmount = "200";
        int amount = Math.round(Float.parseFloat(sAmount)*100);
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("status").getValue().toString().equals("enrolled in "+ coursename))
                {
                    link.setVisibility(View.VISIBLE);
                    link.setText( grouplink);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*btpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PayUPaymentParams.Builder builder = new PayUPaymentParams.Builder();
                builder.setAmount("1.0")
        .setIsProduction(true)
        .setProductInfo("test")
        .setKey("xH1qvl")
        .setPhone("9711445734")
        .setTransactionId(String.valueOf(System.currentTimeMillis()))
        .setFirstName("me")
        .setEmail("apoorvagarg9148@gmail.com")
        .setSurl("")
        .setFurl(""); //Optional, can contain any additional PG params
                PayUPaymentParams payUPaymentParams = builder.build();
            }
        });*/
        btpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewCourse.this,MainActivity.class);
                intent.putExtra("grplnk",grouplink);
                intent.putExtra("userid",user.getUid());
                intent.putExtra("coursename", coursename);
                startActivity(intent);
             /*   Checkout checkout = new Checkout();
                checkout.setKeyID("rzp_test_rfooZLYQbv7p5h");
                checkout.setImage(R.drawable.rzp_logo);
                JSONObject object = new JSONObject();
                try {
                    object.put("name","peerpower");
                    object.put("description","payment for"+coursename);
                    object.put("theme.color","#0093DD");
                    object.put("currency","INR");
                    object.put("amount",amount);
                    object.put("prefill.contact","9711445734");
                    object.put("prefill.email","apoorvagarg9148@gmail.com");
                    checkout.open(viewCourse.this,object);


                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
           }
        });
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
        //        .permitAll().build();
        //StrictMode.setThreadPolicy(policy);

    }

 /*   @Override
    public void onPaymentSuccess(String s) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("paymment ID");
        builder.setMessage(s);
        builder.show();
        link.setVisibility(View.VISIBLE);
        link.setText("join whatsapp group" + grouplink);

        HashMap hashMap = new HashMap();
        hashMap.put("status","enrolled in "+ coursename);
        reference.child(user.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                Toast.makeText(viewCourse.this, "you enrolled succesfully please join the group with the link", Toast.LENGTH_SHORT).show();
            }
        });



    }
    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(), ""+s, Toast.LENGTH_SHORT).show();
    }*/

   /* private void sendEmail() {
final String username = "peerpowerclub@gmail.com";
final String password = "Toptrove_4378";
String messagetosend = "welcome to the course ";
        Properties props = new Properties();

        props.put("mail.smtp.starttls.enable",true);
props.put("mail.smtp.auth","true");
props.put("mail.smtp.start.enable","true");
props.put("mail.smtp.host","smtp.gmail.com");
props.put("mail.smtp.port","587");


        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("apoorvagarg9148@gmail.com"));
            message.setSubject("sending email without opening gmail apps");
            message.setText(messagetosend);
            Transport.send(message);
            Toast.makeText(getApplicationContext(), "email sent", Toast.LENGTH_SHORT).show();
        }catch (MessagingException e){
            Toast.makeText(getApplicationContext(), "nhi gyA", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }



    }
*/




}