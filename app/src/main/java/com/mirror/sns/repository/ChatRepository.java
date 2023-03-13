package com.mirror.sns.repository;

import android.app.Application;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mirror.sns.model.Chat;
import com.mirror.sns.model.ChatMetaData;
import com.mirror.sns.model.ChatRoom;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ChatRepository {

    public static final String TAG = "ChatRepository";

    Application application;

    private DatabaseReference chatRoomRef;
    private DatabaseReference chatRef;

    private MutableLiveData<List<ChatRoom>> chatRoomListLiveData;
    private List<ChatRoom> chatRoomList;

    private MutableLiveData<ChatRoom> chatRoomLiveData;

    private MutableLiveData<List<Chat>> chatListLiveData;

    private MutableLiveData<Boolean> resultSetChatRoom;
    private MutableLiveData<Boolean> resultSetChat;

    public ChatRepository(Application application) {
        this.application = application;

        chatRoomRef = FirebaseDatabase.getInstance().getReference("chatRoom");
        chatRef = FirebaseDatabase.getInstance().getReference("chat");

        chatRoomListLiveData = new MutableLiveData<>();
        chatRoomList = new ArrayList<>();

        chatRoomLiveData = new MutableLiveData<>();

        chatListLiveData = new MutableLiveData<>();

        resultSetChatRoom = new MutableLiveData<>();
        resultSetChat = new MutableLiveData<>();
    }

    public MutableLiveData<List<ChatRoom>> getChatRoomListLiveData() {
        return chatRoomListLiveData;
    }

    public MutableLiveData<Boolean> getResultSetChatRoom() {
        return resultSetChatRoom;
    }

    public MutableLiveData<ChatRoom> getChatRoomLiveData() {
        return chatRoomLiveData;
    }

    public MutableLiveData<List<Chat>> getChatListLiveData() { return chatListLiveData; }

    public MutableLiveData<Boolean> getResultSetChat() { return resultSetChat; }

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
                    Log.d(TAG, chatRoom.getUser1() + " " + chatRoom.getUser2() + " " + requestUser + " " + responseUser);
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
                    chatRoomRef.child(requestUser).child(key).setValue(new ChatRoom(key, requestUser, responseUser, new ChatMetaData("", "", "", ""))).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                chatRoomRef.child(responseUser).child(key).setValue(new ChatRoom(key, requestUser, responseUser, new ChatMetaData("", "", "", "")));
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


    public void getChatRoom(String chatRoomKey) {
        Log.d(TAG,chatRoomKey + " 1212");
        chatRoomRef.child(chatRoomKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ChatRoom chatRoom = snapshot.getValue(ChatRoom.class);
                chatRoomLiveData.setValue(chatRoom);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void getChatList(String chatRoomKey) {
        chatRef.child(chatRoomKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                List<Chat> chats = new ArrayList<>();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    Chat chat = snapshot1.getValue(Chat.class);
                    chats.add(chat);
                }

                chatListLiveData.setValue(chats);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendChat(String chatRoomKey, Chat chat) {

        if (chat.getChat().isEmpty())
            return;

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String formatedNow = now.format(formatter);

        chat.setTime(formatedNow);
        chatRef.child(chatRoomKey).push().setValue(chat).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    resultSetChat.setValue(true);
                } else {
                    resultSetChat.setValue(false);
                }
            }
        });
    }

    public void setChatMetaData(String chatRoomKey, Chat chat, String requestUid, String responseUid) {
        chatRoomRef.child(requestUid)
                .child(chatRoomKey)
                .child("chatMetaData")
                .setValue(new ChatMetaData(chat.getNickName(), chat.getChat(), chat.getTime(), "")).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    chatRoomRef.child(responseUid)
                            .child(chatRoomKey)
                            .child("chatMetaData")
                            .setValue(new ChatMetaData(chat.getNickName(), chat.getChat(), chat.getTime(), ""));
                }
            }
        });
        //  (lastUserName, lastMessage, lastMessageDate, unReadChatCount)
    }


}
