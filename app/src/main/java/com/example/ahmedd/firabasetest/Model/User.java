package com.example.ahmedd.firabasetest.Model;

public class User {

    //class 3shan el object el gay mn el dataBase gay json
    //lazm ykon nfs el 2sami bzbt
    //3shan my3mlsh null pointer exception

    private String id;
    private String userName;
    private String ImageURL;

    public User() {}

    public User(String userID, String userName, String imageURL) {
        this.id = userID;
        this.userName = userName;
        this.ImageURL = imageURL;
    }


    public String getUserID() {
        return id;
    }

    public void setUserID(String userID) {
        this.id = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        this.ImageURL = imageURL;
    }
}
