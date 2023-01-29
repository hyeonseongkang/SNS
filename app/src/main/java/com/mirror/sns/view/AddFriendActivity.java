package com.mirror.sns.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mirror.sns.R;
import com.mirror.sns.databinding.ActivityAddFriendBinding;

public class AddFriendActivity extends AppCompatActivity {

    private ActivityAddFriendBinding addFriendBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFriendBinding = ActivityAddFriendBinding.inflate(getLayoutInflater());
        setContentView(addFriendBinding.getRoot());
    }
}