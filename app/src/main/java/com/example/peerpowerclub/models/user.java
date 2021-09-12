package com.example.peerpowerclub.models;

public class user {

    public String fullname, email,phonenumber, areaOfInterest, prefferedTime,uid,status,imageuri;
    public user()
    {

    }
    public user(String fullname, String email, String phonenumber, String areaOfInterest, String prefferedTime,String uid,String status,String imageuri)
    {
        this.fullname = fullname;
        this.email = email;
        this.phonenumber = phonenumber;
        this.areaOfInterest = areaOfInterest;
        this.prefferedTime = prefferedTime;
        this.uid = uid;
        this.status = status;
        this.imageuri = imageuri;

    }
}
