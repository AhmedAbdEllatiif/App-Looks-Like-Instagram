package com.example.ahmedd.firabasetest.Model;

public class Chats {

    //must named as the dataBase
    private String sender;
    private String receiver;
    private String message;
    private String time;
    private Boolean isSeen;

    public Chats() {}

    public Chats(String sender, String receiver, String message, String time, Boolean isSeen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.time = time;
        this.isSeen = isSeen;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(Boolean isSeen) {
        this.isSeen = isSeen;
    }
}
