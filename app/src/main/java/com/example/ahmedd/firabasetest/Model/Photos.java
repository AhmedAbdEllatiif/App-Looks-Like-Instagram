package com.example.ahmedd.firabasetest.Model;

public class Photos {

    private String emoji;
    private String url;
    private String description;
    private String date;
    private String key;
    private String userName;
    private String userImage;
    private String userID;

    public Photos(String emoji, String url, String description, String date) {
        this.emoji = emoji;
        this.url = url;
        this.description = description;
        this.date = date;
    }

    public Photos(String emoji, String url) {
        this.emoji = emoji;
        this.url = url;
    }

    public Photos() {}

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
