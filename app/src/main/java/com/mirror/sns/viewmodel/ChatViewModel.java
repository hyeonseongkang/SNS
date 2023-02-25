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

    public ChatViewModel(@NonNull @NotNull Application application) {
        super(application);

        repository = new ChatRepository(application);

        chatRoomListLiveData = repository.getChatRoomListLiveData();
    }

    public LiveData<List<ChatRoom>> getChatRoomListLiveData() {
        return chatRoomListLiveData;
    }

    public void getChatRoomListLiveData(String uid) {
        repository.getChatRoomList(uid);
    }
}
