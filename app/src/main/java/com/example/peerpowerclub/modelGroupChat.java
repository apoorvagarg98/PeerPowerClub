package com.example.peerpowerclub;

public class modelGroupChat {
    String message;
    String timestamp;
    String type;
    String sender;

    public modelGroupChat(){}
    public modelGroupChat(String message,String timestamp,String type,String sender)
    {
        this.message=message;
        this.timestamp=timestamp;
        this.type=type;
        this.sender = sender;
    }
    public String getMessage(){return message;}
    public String getTimestamp(){return timestamp;}
    public String getType(){return type;}
    public String getSender(){return sender;}

    public void setMessage(String message){this.message = message;}
    public void setTimestamp(String timestamp){this.timestamp = timestamp;}
    public void setType(String type){this.type = type;}
    public void setSender(String sender){this.sender = sender;}
}
