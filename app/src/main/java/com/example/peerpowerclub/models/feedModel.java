package com.example.peerpowerclub.models;

public class feedModel {
    public String postImageUrl;
    public String caption;
    public String name;
    public String datepost;
    public String uid;

    public feedModel(){

    }

    public feedModel(String  postImageUrl, String caption, String name,String datepost,String uid) {
        this.postImageUrl = postImageUrl;
        this.caption = caption;
        this.name = name;
        this.datepost = datepost;
        this.uid = uid;
    }
    public String getDatepost() {
        return datepost;
    }

    public void setDatepost(String datepost) {
        this.datepost = datepost;
    }

    public String getname() {
        return name;
    }
    public String getUid(){return uid;}


    public String getPostImageUrl() {
        return postImageUrl;
    }



    public String getcaption() {
        return caption;
    }

    public void setTitle(String title) {
        this.caption = caption;
    }



    public void setusername(String name) {
        this.name = name;
    }
}
