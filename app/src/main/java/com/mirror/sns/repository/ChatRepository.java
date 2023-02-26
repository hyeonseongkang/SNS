package com.mirror.sns.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    private MutableLiveData<Boolean> resultSetChatRoom;

    public ChatRepository(Application application) {
        this.application = application;

        chatRoomRef = FirebaseDatabase.getInstance().getReference("chatRoom");

        chatRoomListLiveData = new MutableLiveData<>();
        chatRoomList = new ArrayList<>();

        resultSetChatRoom = new MutableLiveData<>();
    }

    public MutableLiveData<List<ChatRoom>> getChatRoomListLiveData() {
        return chatRoomListLiveData;
    }

    public MutableLiveData<Boolean> getResultSetChatRoom() {
        return resultSetChatRoom;
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

    public void setChatRoom(String requestUser, String responseUser) {
        if (requestUser.equals(responseUser)) return;

        chatRoomRef.child(requestUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                boolean overlap = false;
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    ChatRoom chatRoom = snapshot1.getValue(ChatRoom.class);
                    if ((chatRoom.getUser1().equals(requestUser) || chatRoom.getUser2().equals(requestUser)) &&
                            (chatRoom.getUser1().equals(responseUser) || chatRoom.getUser2().equals(responseUser))) {
                        // 이미 존재 하는 채팅방
                        overlap = true;
                        break;
                    }
                }

                if (overlap) {
                    resultSetChatRoom.setValue(false);
                } else {
                    resultSetChatRoom.setValue(true);
                    String key = chatRoomRef.push().getKey();
                    chatRoomRef.child(requestUser).child(key).setValue(new ChatRoom(requestUser, responseUser)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                chatRoomRef.child(responseUser).child(key).setValue(new ChatRoom(requestUser, responseUser));
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


}
