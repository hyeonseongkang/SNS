package com.mirror.sns.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.mirror.sns.R;
import com.mirror.sns.classes.User;
import com.mirror.sns.databinding.ActivityCreatePostBinding;
import com.mirror.sns.databinding.ActivitySearchFriendBinding;
import com.mirror.sns.viewmodel.UserManagementViewModel;

import java.util.List;

public class SearchFriendActivity extends AppCompatActivity {

    public static final String TAG = "SearchFriendActivity";

    private ActivitySearchFriendBinding searchFriendBinding;

    private UserManagementViewModel userManagementViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchFriendBinding = ActivitySearchFriendBinding.inflate(getLayoutInflater());
        setContentView(searchFriendBinding.getRoot());

        userManagementViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(UserManagementViewModel.class);
        userManagementViewModel.getUserList();
        userManagementViewModel.getUserListLiveData().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {

            }
        });


        searchFriendBinding.closeButton.setColorFilter(Color.parseColor("#FFFFFF"));
        searchFriendBinding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("check", false);
                setResult(RESULT_OK, data);
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_up);
            }
        });
    }
}