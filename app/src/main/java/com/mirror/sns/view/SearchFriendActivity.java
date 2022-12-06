package com.mirror.sns.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mirror.sns.R;
import com.mirror.sns.databinding.ActivityCreatePostBinding;
import com.mirror.sns.databinding.ActivitySearchFriendBinding;

public class SearchFriendActivity extends AppCompatActivity {

    public static final String TAG = "SearchFriendActivity";

    private ActivitySearchFriendBinding searchFriendBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchFriendBinding = ActivitySearchFriendBinding.inflate(getLayoutInflater());
        setContentView(searchFriendBinding.getRoot());
    }
}