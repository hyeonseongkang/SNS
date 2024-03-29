package com.mirror.sns.model;

import java.util.ArrayList;

public class Post {
    private String key;
    private String userUid;
    private String nickName;
    private String content;
    private String userPhotoUri;
    private String postPhotoUri;
    private ArrayList<Tag> tags;
    private ArrayList<User> likes;


    public Post() {}

    public Post(String key, String userUid, String nickName, String content, String userPhotoUri, String postPhotoUri, ArrayList<Tag> tags, ArrayList<User> likes) {
        this.key = key;
        this.userUid = userUid;
        this.nickName = nickName;
        this.content = content;
        this.userPhotoUri = userPhotoUri;
        this.postPhotoUri = postPhotoUri;
        this.tags = tags;
        this.likes = likes;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getNickName() {
        return nickName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserPhotoUri() {
        return userPhotoUri;
    }

    public void setUserPhotoUri(String userPhotoUri) {
        this.userPhotoUri = userPhotoUri;
    }

    public String getPostPhotoUri() {
        return postPhotoUri;
    }

    public void setPostPhotoUri(String postPhotoUri) {
        this.postPhotoUri = postPhotoUri;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public ArrayList<User> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<User> likes) {
        this.likes = likes;
    }
}
