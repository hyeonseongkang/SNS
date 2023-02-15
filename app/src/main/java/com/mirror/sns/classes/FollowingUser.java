package com.mirror.sns.classes;

public class FollowingUser {
    private String key;
    private User user;

    public FollowingUser() {}

    public FollowingUser(String key, User user) {
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
