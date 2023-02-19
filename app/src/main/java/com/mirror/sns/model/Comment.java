package com.mirror.sns.model;

public class Comment {

    String key;
    User user;
    String comment;
    String like;

    public Comment (User user, String key, String comment, String like) {
        this.user = user;
        this.key = key;
        this.comment = comment;
        this.like = like;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public String getLike() { return like; }
}
