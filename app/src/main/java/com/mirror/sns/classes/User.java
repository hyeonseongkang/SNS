package com.mirror.sns.classes;

import java.util.List;

public class User {
    private String uid;
    private String email;
    private String password;
    private String nickName;
    private String photoUri;
    private List<Post> posts;
    private List<FollowerUser> followerUsers;
    private List<FollowingUser> followingUsers;

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

    public User(String uid, String email, String password, String nickName, String photoUri, List<Post> posts, List<FollowerUser> followerUsers, List<FollowingUser> followingUsers) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.photoUri = photoUri;
        this.posts = posts;
        this.followerUsers = followerUsers;
        this.followingUsers = followingUsers;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public List<FollowerUser> getFollowerUsers() {
        return followerUsers;
    }

    public List<FollowingUser> getFollowingUsers() {
        return followingUsers;
    }
}
