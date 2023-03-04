package com.mirror.sns.model;

public class ChatRoom {

    private String user1;
    private String user2;
    private Chat lastChat;

    public ChatRoom(){}

    public ChatRoom(String user1, String user2, Chat lastChat) {
        this.user1 = user1;
        this.user2 = user2;
        this.lastChat = lastChat;
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

    public Chat getLastChat() {
        return lastChat;
    }

    public void setLastChat(Chat lastChat) {
        this.lastChat = lastChat;
    }
}
