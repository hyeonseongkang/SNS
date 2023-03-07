package com.mirror.sns.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.mirror.sns.R;
import com.mirror.sns.databinding.ActivityChatBinding;
import com.mirror.sns.model.ChatRoom;
import com.mirror.sns.viewmodel.ChatViewModel;

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


    }
}