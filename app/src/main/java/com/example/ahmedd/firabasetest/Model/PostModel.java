package com.example.ahmedd.firabasetest.Model;

public class PostModel {

    private Photos photos;
    private User user;

    private String postImg;
    private String postEmoji;
    private String postDate;
    private String postOwnerName;
    private String userProfile_img;


    public PostModel(Photos photos) {
        this.photos = photos;

    }

    public String getPostImg() {
        return photos.getUrl();
    }

    public String getPostEmoji() {
        return photos.getEmoji();
    }

    public String getPostDate() {
        return photos.getDate();
    }

    public String getPostOwnerName() {
        return photos.getUserName();
    }

    public String getUserProfile_img() {
        return photos.getUserImage();
    }
}
