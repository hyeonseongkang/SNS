package com.mirror.sns.model;

public class ChatMetaData {
    private String lastUserName;
    private String lastMessage;
    private String lastMessageDate;
    private String unReadChatCount;

    public ChatMetaData() {};

    public ChatMetaData(String lastUserName, String lastMessage, String lastMessageDate, String unReadChatCount) {
        this.lastUserName = lastUserName;
        this.lastMessage = lastMessage;
        this.lastMessageDate = lastMessageDate;
        this.unReadChatCount = unReadChatCount;
    }

    public String getLastUserName() {
        return lastUserName;
    }

    public void setLastUserName(String lastUserName) {
        this.lastUserName = lastUserName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(String lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    public String getUnReadChatCount() {
        return unReadChatCount;
    }

    public void setUnReadChatCount(String unReadChatCount) {
        this.unReadChatCount = unReadChatCount;
    }
}
