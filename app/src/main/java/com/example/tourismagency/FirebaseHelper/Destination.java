package com.example.tourismagency.FirebaseHelper;

public class Destination {
    String id;
    String title;
    String description;
    Float price;
    String uid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Destination(String id, String title, String description, Float price, String uid) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.uid = uid;
    }

    public Destination() {
    }
}
