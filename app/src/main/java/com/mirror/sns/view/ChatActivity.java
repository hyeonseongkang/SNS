package com.mirror.sns.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.mirror.sns.R;
import com.mirror.sns.adapter.ChatAdapter;
import com.mirror.sns.databinding.ActivityChatBinding;
import com.mirror.sns.model.Chat;
import com.mirror.sns.model.ChatRoom;
import com.mirror.sns.model.User;
import com.mirror.sns.viewmodel.ChatViewModel;
import com.mirror.sns.viewmodel.UserManagementViewModel;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private ActivityChatBinding chatBinding;

    private UserManagementViewModel userManagementViewModel;
    private ChatViewModel chatViewModel;

    private ChatAdapter chatAdapter;

    private String userUid; // 상대방 UID
    private String chatRoomKey;

    private User currUser; // me
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(chatBinding.getRoot());
        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        Intent intent = getIntent();
        userUid = intent.getStringExtra("userUid");
        chatRoomKey = intent.getStringExtra("chatRoomKey");

        userManagementViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(UserManagementViewModel.class);
        userManagementViewModel.getUserInfo(FirebaseAuth.getInstance().getUid());
        userManagementViewModel.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                currUser = user;
            }
        });
        chatViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ChatViewModel.class);
        chatViewModel.getChatRoom(chatRoomKey);
        chatViewModel.getChatRoomLiveData().observe(this, new Observer<ChatRoom>() {
            @Override
            public void onChanged(ChatRoom chatRoom) {

            }
        });



        chatViewModel.getResultSetChat().observe(this, new Observer<Chat>() {
            @Override
            public void onChanged(Chat chat) {
                if (chat != null) {
                    chatBinding.message.setText("");
                    chatViewModel.setChatMetaData(chatRoomKey, chat, FirebaseAuth.getInstance().getUid(), userUid);
                }
            }
        });

        chatViewModel.getChatList(chatRoomKey);
        chatViewModel.getChatListLiveData().observe(this, new Observer<List<Chat>>() {
            @Override
            public void onChanged(List<Chat> chats) {
                for (Chat chat: chats) {
                    Log.d(TAG, chat.getChat());
                }

                chatAdapter.setChats(chats, FirebaseAuth.getInstance().getUid());
            }
        });

        chatAdapter = new ChatAdapter();
        chatBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatBinding.recyclerView.setHasFixedSize(true);
        chatBinding.recyclerView.setAdapter(chatAdapter);


        chatBinding.sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                chatViewModel.sendChat(chatRoomKey, new Chat(FirebaseAuth.getInstance().getUid(), currUser.getNickName(), chatBinding.message.getText().toString(), ""));
            }
        });


    }
}