package com.mirror.sns.model;

import java.util.ArrayList;

public class User {
    private String uid;
    private String email;
    private String password;
    private String nickName;
    private String photoUri;
    private ArrayList<Post> posts;
    private ArrayList<FollowerUser> followerUsers;
    private ArrayList<FollowingUser> followingUsers;

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

    public User(String uid, String email, String password, String nickName, String photoUri, ArrayList<Post> posts, ArrayList<FollowerUser> followerUsers, ArrayList<FollowingUser> followingUsers) {
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

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public ArrayList<FollowerUser> getFollowerUsers() {
        return followerUsers;
    }

    public ArrayList<FollowingUser> getFollowingUsers() {
        return followingUsers;
    }

    public void toStringUser() {
        System.out.println("uid: " + getUid() + "\nemail: " + getEmail() + "\npassword: " + getPassword() + "\nnickName: " + getNickName() + "\nphotoUri: " + getPhotoUri() + "\nposts.size: " + getPosts().size() + "\n" +
                "followerUsers.size: " + getFollowerUsers().size() + "\nfollowingUsers.size: " + getFollowingUsers().size());
    }
}
