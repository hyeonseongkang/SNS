package com.mirror.sns.classes;

public class FollowerUser {
    private String key;
    private User user;

    public FollowerUser(String key, User user) {
        this.key = key;
        this.user = user;
    }

    public String getKey() {
        return key;
    }

    public User getUser() {
        return user;
    }
}
