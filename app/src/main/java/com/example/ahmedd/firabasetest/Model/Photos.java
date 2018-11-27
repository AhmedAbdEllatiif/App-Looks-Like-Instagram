package com.example.ahmedd.firabasetest.Model;

public class Photos {

    private String name;
    private String url;

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
}
