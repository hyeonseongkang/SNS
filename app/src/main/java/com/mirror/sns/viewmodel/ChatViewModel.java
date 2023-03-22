package com.mirror.sns.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mirror.sns.model.Chat;
import com.mirror.sns.model.ChatRoom;
import com.mirror.sns.repository.ChatRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {

    private ChatRepository repository;
    private LiveData<List<ChatRoom>> chatRoomListLiveData;

    private LiveData<ChatRoom> chatRoomLiveData;

    private LiveData<List<Chat>> chatListLiveData;

    private LiveData<Boolean> resultSetChatRoom;

    private LiveData<Chat> resultSetChat;

    private MutableLiveData<Boolean> leaveChatRoom;

    public ChatViewModel(@NonNull @NotNull Application application) {
        super(application);

        repository = new ChatRepository(application);

        chatRoomListLiveData = repository.getChatRoomListLiveData();

        chatRoomLiveData = repository.getChatRoomLiveData();

        chatListLiveData = repository.getChatListLiveData();

        resultSetChatRoom = repository.getResultSetChatRoom();

        resultSetChat = repository.getResultSetChat();

        leaveChatRoom = repository.getLeaveChatRoom();

    }

    public LiveData<List<ChatRoom>> getChatRoomListLiveData() {
        return chatRoomListLiveData;
    }

    public LiveData<Boolean> getResultSetChatRoom() {
        return resultSetChatRoom;
    }

    public LiveData<ChatRoom> getChatRoomLiveData() { return chatRoomLiveData; }

    public LiveData<List<Chat>> getChatListLiveData() { return chatListLiveData; }

    public LiveData<Chat> getResultSetChat() { return resultSetChat; }

    public MutableLiveData<Boolean> getLeaveChatRoom() { return leaveChatRoom; }

    public void getChatRoomListLiveData(String uid) {
        repository.getChatRoomList(uid);
    }

    public void setChatRoom(String requestUser, String responseUser) {
        repository.setChatRoom(requestUser, responseUser);
    }

    public void getChatRoom(String chatRoomKey) {
        repository.getChatRoom(chatRoomKey);
    }

    public void getChatList(String chatRoomKey) { repository.getChatList(chatRoomKey);}

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendChat(String chatRoomKey, Chat chat) {
        repository.sendChat(chatRoomKey, chat);
    }

    public void setChatMetaData(String chatRoomKey, Chat chat, String requestUid, String responseUid) {
        repository.setChatMetaData(chatRoomKey, chat, requestUid, responseUid);
    }

    public void getLeaveChatRoom(String userUid, String myUid, String itemKey) {
        repository.getLeaveChatRoom(userUid, myUid, itemKey);
    }
}
