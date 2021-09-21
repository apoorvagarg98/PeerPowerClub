package com.example.peerpowerclub.models;

public class user {

    public String fullname,phonenumber, areaOfInterest, prefferedTime,status,imageuri,profileurl,stg,ltg,language,uid;
    public user()
    {

    }
    public user(String fullname, String phonenumber, String areaOfInterest, String prefferedTime,String status,String imageuri,String profileurl,String stg,String ltg,String language,String uid)
    {
        this.fullname = fullname;

        this.phonenumber = phonenumber;
        this.areaOfInterest = areaOfInterest;
        this.prefferedTime = prefferedTime;

        this.status = status;
        this.imageuri = imageuri;
        this.profileurl=profileurl;
        this.stg = stg;
        this.ltg =ltg;
        this.language=language;
        this.uid = uid;

    }
}
