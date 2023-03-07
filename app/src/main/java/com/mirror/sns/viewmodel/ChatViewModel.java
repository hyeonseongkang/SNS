package com.mirror.sns.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mirror.sns.model.ChatRoom;
import com.mirror.sns.repository.ChatRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {

    private ChatRepository repository;
    private LiveData<List<ChatRoom>> chatRoomListLiveData;

    private LiveData<ChatRoom> chatRoomLiveData;

    private LiveData<Boolean> resultSetChatRoom;

    public ChatViewModel(@NonNull @NotNull Application application) {
        super(application);

        repository = new ChatRepository(application);

        chatRoomListLiveData = repository.getChatRoomListLiveData();

        chatRoomLiveData = repository.getChatRoomLiveData();

        resultSetChatRoom = repository.getResultSetChatRoom();

    }

    public LiveData<List<ChatRoom>> getChatRoomListLiveData() {
        return chatRoomListLiveData;
    }

    public LiveData<Boolean> getResultSetChatRoom() {
        return resultSetChatRoom;
    }

    public LiveData<ChatRoom> getChatRoomLiveData() { return chatRoomLiveData; }

    public void getChatRoomListLiveData(String uid) {
        repository.getChatRoomList(uid);
    }

    public void setChatRoom(String requestUser, String responseUser) {
        repository.setChatRoom(requestUser, responseUser);
    }

    public void getChatRoom(String chatRoomKey) {
        repository.getChatRoom(chatRoomKey);
    }
}
