package com.mirror.sns.classes;

public class Comment {

    String key;
    User user;
    String comment;

    public Comment (User user, String key, String comment) {
        this.user = user;
        this.key = key;
        this.comment = comment;
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
}
