package com.example.ahmedd.firabasetest.Model;

public class User {

    //class 3shan el object el gay mn el dataBase gay json
    //lazm ykon nfs el 2sami bzbt
    //3shan my3mlsh null pointer exception

    private String ImageURL;
    private String id;
    private String userName;
    private String status;
    private String birthday;

    public User() {}

    public User(String id, String userName, String ImageURL,String status,String birthday) {
        this.id = id;
        this.userName = userName;
        this.ImageURL = ImageURL;
        this.status = status;
        this.birthday = birthday;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
