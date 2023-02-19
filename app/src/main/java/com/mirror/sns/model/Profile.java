package com.mirror.sns.model;

public class Profile {
    private String uid;
    private String email;
    private String nickName;
    private String photoUri;

    public Profile() {}

    public Profile(String uid, String email, String nickName, String photoUri) {
        this.uid = uid;
        this.email = email;
        this.nickName = nickName;
        this.photoUri = photoUri;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getNickName() {
        return nickName;
    }

    public String getPhotoUri() {
        return photoUri;
    }
}
