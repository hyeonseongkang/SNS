package com.mirror.sns.model;

public class ChatRoom {

    private String key;
    private String user1;
    private String user2;
    private ChatMetaData chatMetaData;

    public ChatRoom(){}

    public ChatRoom(String key, String user1, String user2, ChatMetaData chatMetaData) {
        this.key = key;
        this.user1 = user1;
        this.user2 = user2;
        this.chatMetaData = chatMetaData;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public ChatMetaData getChatMetaData() {
        return chatMetaData;
    }

    public void setChatMetaData(ChatMetaData chatMetaData) {
        this.chatMetaData = chatMetaData;
    }
}
