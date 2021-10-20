package com.example.peerpowerclub.models;

public class user {

    public String fullname,phonenumber, areaOfInterest, prefferedTime,status,imageuri,profileurl,stg,ltg,uid,insta,twitter,others,timecall ,skillsknow;
    public user()
    {

    }
    public user(String fullname, String phonenumber, String areaOfInterest, String prefferedTime,String status,String imageuri,String profileurl,String stg,String ltg,String uid,String insta,String twitter,String others,String timecall ,String skillsknow)
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

        this.uid = uid;
        this.insta=insta;
    this.twitter=twitter;
    this.others=others;
    this.timecall =timecall;
    this.skillsknow=skillsknow;

    }
}
