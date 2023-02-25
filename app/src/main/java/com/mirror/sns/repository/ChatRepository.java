package com.mirror.sns.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mirror.sns.model.ChatRoom;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChatRepository {

    Application application;

    private DatabaseReference chatRoomRef;

    private MutableLiveData<List<ChatRoom>> chatRoomListLiveData;
    private List<ChatRoom> chatRoomList;

    public ChatRepository(Application application) {
        this.application = application;

        chatRoomRef = FirebaseDatabase.getInstance().getReference("chatRoom");

        chatRoomListLiveData = new MutableLiveData<>();
        chatRoomList = new ArrayList<>();
    }

    public MutableLiveData<List<ChatRoom>> getChatRoomListLiveData() {
        return chatRoomListLiveData;
    }

    public void getChatRoomList(String uid) {
        chatRoomRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                chatRoomList.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    ChatRoom chatRoom = snapshot1.getValue(ChatRoom.class);
                    chatRoomList.add(chatRoom);
                }

                chatRoomListLiveData.setValue(chatRoomList);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }



}
