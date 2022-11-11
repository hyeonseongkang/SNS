package com.mirror.sns.classes;

public class Post {
    private String key;
    private String userUid;
    private String content;

    public Post() {}

    public Post(String key, String userUid, String content) {
        this.key = key;
        this.userUid = userUid;
        this.content = content;
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
}
