package com.mirror.sns.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

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

    public ChatViewModel(@NonNull @NotNull Application application) {
        super(application);

        repository = new ChatRepository(application);

        chatRoomListLiveData = repository.getChatRoomListLiveData();

        chatRoomLiveData = repository.getChatRoomLiveData();

        chatListLiveData = repository.getChatListLiveData();

        resultSetChatRoom = repository.getResultSetChatRoom();

    }

    public LiveData<List<ChatRoom>> getChatRoomListLiveData() {
        return chatRoomListLiveData;
    }

    public LiveData<Boolean> getResultSetChatRoom() {
        return resultSetChatRoom;
    }

    public LiveData<ChatRoom> getChatRoomLiveData() { return chatRoomLiveData; }

    public LiveData<List<Chat>> getChatListLiveData() { return chatListLiveData; }

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

}
