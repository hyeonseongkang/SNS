package com.mirror.sns.model;

public class ChatRoom {

    private String key;
    private User user;
    private LastMessage lastMessage;
    private boolean visited;
    private int unReadChatCount;
    private boolean leaveChatRoom;
    private boolean firstUp;

    public ChatRoom() {}

    public ChatRoom(String key,User user, LastMessage lastMessage, boolean visited, int unReadChatCount, boolean leaveChatRoom, boolean firstUp) {
        this.key = key;
        this.user = user;
        this.lastMessage = lastMessage;
        this.visited = visited;
        this.unReadChatCount = unReadChatCount;
        this.leaveChatRoom = leaveChatRoom;
        this.firstUp = firstUp;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user;}

    public LastMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(LastMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean getVisited() {
        return visited;
    }

    public void setUnReadChatCount(int count) {
        this.unReadChatCount = count;
    }

    public int getUnReadChatCount() {
        return unReadChatCount;
    }

    public boolean isLeaveChatRoom() {
        return leaveChatRoom;
    }

    public boolean isFirstUp() {
        return firstUp;
    }

    public void setFirstUp(boolean firstUp) {
        this.firstUp = firstUp;
    }
}
