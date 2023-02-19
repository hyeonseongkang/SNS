package com.mirror.sns.model;

public class Sns {
    private String uid;
    private String title;
    private String firstPhotoUri;
    // etc...

    public Sns() {}

    public Sns(String uid, String title, String firstPhotoUri) {
        this.uid = uid;
        this.title = title;
        this.firstPhotoUri = firstPhotoUri;
    }

    public String getFirstPhotoUri() {
        return firstPhotoUri;
    }
}
