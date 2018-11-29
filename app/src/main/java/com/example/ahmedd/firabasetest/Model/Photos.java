package com.example.ahmedd.firabasetest.Model;

public class Photos {

    private String name;
    private String url;
    private String description;
    private String date;

    public Photos(String name, String url, String description, String date) {
        this.name = name;
        this.url = url;
        this.description = description;
        this.date = date;
    }

    public Photos(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public Photos() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
