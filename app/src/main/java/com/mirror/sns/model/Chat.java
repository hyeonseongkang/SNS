package com.mirror.sns.model;

public class Chat {
    private String userUid;
    private String nickName;
    private String chat;
    private String time;

    public Chat() {}

    public Chat(String userUid, String nickName, String chat, String time) {
        this.userUid = userUid;
        this.nickName = nickName;
        this.chat = chat;
        this.time = time;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
