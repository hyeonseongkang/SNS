package com.mirror.sns.classes;

import java.util.ArrayList;

public class Post {
    private String key;
    private String userUid;
    private String content;
    private String userPhotoUri;
    private ArrayList<String> photoKeys;
    private String firstPhotoUri;
    private ArrayList<String> likes;

    public Post() {}

    public Post(String key, String userUid, String content, String userPhotoUri, ArrayList<String> photoKeys, String firstPhotoUri, ArrayList<String> likes) {
        this.key = key;
        this.userUid = userUid;
        this.content = content;
        this.userPhotoUri = userPhotoUri;
        this.photoKeys = photoKeys;
        this.firstPhotoUri = firstPhotoUri;
        this.likes = likes;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getUserUid() {
        return userUid;
    }

    public String getContent() {
        return content;
    }

    public String getUserPhotoUri() {
        return userPhotoUri;
    }

    public ArrayList<String> getPhotoKeys() {
        return photoKeys;
    }

    public String getFirstPhotoUri() {
        return firstPhotoUri;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

}
