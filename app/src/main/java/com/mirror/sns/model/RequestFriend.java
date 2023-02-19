package com.mirror.sns.model;

public class RequestFriend {
    private String userUid;
    private String accept;

    public RequestFriend() {}

    public RequestFriend(String userUid, String accept) {
        this.userUid = userUid;
        this.accept = accept;
    }

    public String getUserUid() {
        return userUid;
    }

    public String getAccept() {
        return accept;
    }
}
