package com.mirror.sns.classes;

public class User {
    private String uid;
    private String email;
    private String password;
    private String nickName;
    private String photoUri;
    private String posts;
    private String followers;
    private String following;

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String uid, String email, String password) {
        this.uid = uid;
        this.email = email;
        this.password = password;
    }

    public User(String uid, String email, String password, String nickName, String photoUri, String posts, String followers, String following) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.photoUri = photoUri;
        this.posts = posts;
        this.followers = followers;
        this.following = following;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUid() {
        return uid;
    }

    public String getNickName() {
        return nickName;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String uri) {
        this.photoUri = uri;
    }

    public String getPosts() {
        return posts;
    }

    public String getFollowers() {
        return followers;
    }

    public String getFollowing() {
        return following;
    }
}
