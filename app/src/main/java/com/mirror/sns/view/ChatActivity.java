package com.mirror.sns.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mirror.sns.R;
import com.mirror.sns.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding chatBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(chatBinding.getRoot());
    }
}