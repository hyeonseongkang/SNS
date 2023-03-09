package com.mirror.sns.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.mirror.sns.R;
import com.mirror.sns.databinding.ActivityChatBinding;
import com.mirror.sns.model.Chat;
import com.mirror.sns.model.ChatRoom;
import com.mirror.sns.viewmodel.ChatViewModel;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding chatBinding;

    private ChatViewModel chatViewModel;

    private String userUid; // 상대방 UID
    private String chatRoomKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(chatBinding.getRoot());
        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        Intent intent = getIntent();
        userUid = intent.getStringExtra("userUid");
        chatRoomKey = intent.getStringExtra("chatRoomKey");

        chatViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ChatViewModel.class);
        chatViewModel.getChatRoom(chatRoomKey);
        chatViewModel.getChatRoomLiveData().observe(this, new Observer<ChatRoom>() {
            @Override
            public void onChanged(ChatRoom chatRoom) {

            }
        });

        chatViewModel.getChatList(chatRoomKey);
        chatViewModel.getChatListLiveData().observe(this, new Observer<List<Chat>>() {
            @Override
            public void onChanged(List<Chat> chats) {

            }
        });

        chatBinding.sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                chatViewModel.sendChat(chatRoomKey, new Chat(FirebaseAuth.getInstance().getUid(), "", chatBinding.message.getText().toString(), ""));
            }
        });


    }
}