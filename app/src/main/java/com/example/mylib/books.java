package com.example.mylib;

public class books {
    String name,autr,location,copies,genre,url,url2;

    books(){

    }
    public books(String name, String autr, String location, String copies, String genre, String url,String url2) {
        this.name = name;
        this.autr = autr;
        this.location = location;
        this.copies = copies;
        this.genre = genre;
        this.url = url;
        this.url2 =url2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAutr() {
        return autr;
    }

    public void setAutr(String autr) {
        this.autr = autr;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCopies() {
        return copies;
    }

    public void setCopies(String copies) {
        this.copies = copies;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }
}
