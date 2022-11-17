package com.mirror.sns.classes;

public class Post {
    private String key;
    private String userUid;
    private String content;
    private String userPhotoUri;
    private String photoUri;

    public Post() {}

    public Post(String key, String userPhotoUri, String photoUri, String userUid, String content) {
        this.key = key;
        this.userPhotoUri = userPhotoUri;
        this.photoUri = photoUri;
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
