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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ChatRepository {

    public static final String TAG = "ChatRepository";

    Application application;

    private DatabaseReference chatRoomRef;
    private DatabaseReference chatRef;

    private MutableLiveData<List<HashMap<List<String>, List<Chat>>>> myChats;
    private List<HashMap<List<String>, List<Chat>>> myChatList;

    private MutableLiveData<List<ChatRoom>> chatRoomListLiveData;
    private List<ChatRoom> chatRoomList;

    private MutableLiveData<ChatRoom> chatRoomLiveData;

    private MutableLiveData<List<Chat>> chatListLiveData;

    private MutableLiveData<Boolean> resultSetChatRoom;
    private MutableLiveData<Chat> resultSetChat;

    private MutableLiveData<Boolean> visited;
    private MutableLiveData<Boolean> leaveChatRoom;

    private MutableLiveData<HashMap<List<String>, Integer>> unReadChatCount;

    public ChatRepository(Application application) {
        this.application = application;

        chatRoomRef = FirebaseDatabase.getInstance().getReference("chatRoom");
        chatRef = FirebaseDatabase.getInstance().getReference("chat");

        myChats = new MutableLiveData<>();
        myChatList = new ArrayList<>();

        chatRoomListLiveData = new MutableLiveData<>();
        chatRoomList = new ArrayList<>();

        chatRoomLiveData = new MutableLiveData<>();

        chatListLiveData = new MutableLiveData<>();

        resultSetChatRoom = new MutableLiveData<>();
        resultSetChat = new MutableLiveData<>();

        visited = new MutableLiveData<>();
        leaveChatRoom = new MutableLiveData<>();

        unReadChatCount = new MutableLiveData<>();
    }

    public MutableLiveData<List<HashMap<List<String>, List<Chat>>>> getMyChats() {
        return myChats;
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

    public MutableLiveData<Chat> getResultSetChat() { return resultSetChat; }

    public MutableLiveData<Boolean> getVisited() {
        return visited;
    }

    public MutableLiveData<Boolean> getLeaveChatRoom() {
        return leaveChatRoom;
    }

    public MutableLiveData<HashMap<List<String>, Integer>> getUnReadChatCount() { return unReadChatCount; }

    public void getMyChats(String myUid) {
        chatRef.child(myUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                myChatList.clear();
                // userUids
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    // itemKeys
                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                        HashMap<List<String>, List<Chat>> hashMap = new HashMap<>();
                        List<String> keys = new ArrayList<>();
                        keys.add(snapshot1.getKey()); // userUid
                        keys.add(snapshot2.getKey()); // itemKey;
                        List<Chat> chats = new ArrayList<>();
                        // chats
                        for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                            Chat chat = snapshot3.getValue(Chat.class);
                            chats.add(chat);
                        }
                        hashMap.put(keys, chats);
                        myChatList.add(hashMap);
                    }
                }
                myChats.setValue(myChatList);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
    public void getChatRoomList(String uid) {
        chatRoomRef.child(uid).addValueEventListener(new ValueEventListener() {
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

//        if (chat.getChat().isEmpty())
//            return;

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String formatedNow = now.format(formatter);

        chat.setTime(formatedNow);
        chatRef.child(chatRoomKey).push().setValue(chat).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    resultSetChat.setValue(chat);
                } else {
                    resultSetChat.setValue(null);
                }
            }
        });
    }

    public void setChatMetaData(String chatRoomKey, Chat chat, String requestUid, String responseUid) {
//        chatRoomRef.child(requestUid)
//                .child(chatRoomKey)
//                .child("chatMetaData")
//                .setValue(new ChatMetaData(chat.getNickName(), chat.getChat(), chat.getTime(), "")).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull @NotNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    chatRoomRef.child(responseUid)
//                            .child(chatRoomKey)
//                            .child("chatMetaData")
//                            .setValue(new ChatMetaData(chat.getNickName(), chat.getChat(), chat.getTime(), ""));
//                }
//            }
//        });
    }

    // 채팅방 나갔는지 확인
    public void getLeaveChatRoom(String userUid, String myUid, String itemKey) {
        chatRoomRef.child(userUid).child(myUid).child(itemKey).child("leaveChatRoom").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                boolean value = snapshot.getValue(Boolean.class);

                if (value) {
                    leaveChatRoom.setValue(true);
                } else {
                    leaveChatRoom.setValue(false);
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    // 읽지 않은 채팅 수
    public void getUnReadChatCount(String myUid) {
        HashMap<List<String>, Integer> hashMap = new LinkedHashMap<>();
        // chats / myUid / .. 값들에 변경사항이 일어나면 호출
        /*
        chats
                myUid
                        userUid(1)
                                    itemKey(1)
                                               pushKey(1)
                                                          Chat(class)
                                               pushKey(2)
                                                          Chat(class)
                                               pushKey(3)
                                                          Chat(class)

                                    itemKey(2)
                                               pushKey(1)
                                                          Chat(class)
                                    ...
         */
        chatRef.child(myUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                // userUids
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    // itemKeys
                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                        int count = 0;


                        // keyList에는 나와 채팅하는 상대의 uid(userUid)와 어떤 item으로 부터 채팅이 시작됐는지 확인하기 위한 itemKey가 저장됨
                        List<String> keyList = new ArrayList<>();
                        keyList.add(snapshot1.getKey());
                        boolean first = true;

                        // pushKeys
                        for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                            Chat chat = snapshot3.getValue(Chat.class);
                            if (first) {
                                // itemKey
                                keyList.add(snapshot2.getKey());
                                first = false;
                            }

                            // 내가 보낸 메시지가 아니라면
                            if (!(chat.getSender().equals(myUid))) {
                                // 확인을 하지 않았다면 count 증가
                                if (!(chat.getChecked())) {
                                    count++;
                                }
                            }
                            /*
                            keyList -> 상대 uid, itemKey
                            count -> 읽지 않은 채팅 수
                             */
                            hashMap.put(keyList, count);
                        }

                    }
                }
                unReadChatCount.setValue(hashMap);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}
