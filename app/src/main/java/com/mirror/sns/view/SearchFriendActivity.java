package com.mirror.sns.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mirror.sns.R;
import com.mirror.sns.model.User;
import com.mirror.sns.databinding.ActivitySearchFriendBinding;
import com.mirror.sns.viewmodel.UserManagementViewModel;

import java.util.List;

public class SearchFriendActivity extends AppCompatActivity {

    public static final String TAG = "SearchFriendActivity";

    private ActivitySearchFriendBinding searchFriendBinding;

    private UserManagementViewModel userManagementViewModel;
    private String userNickName;
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

        userManagementViewModel.addFriendCheck().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(SearchFriendActivity.this, "친구 추가 완료", Toast.LENGTH_SHORT).show();
                    Intent data = new Intent();
                    data.putExtra("check", true);
                    setResult(RESULT_OK, data);
                    finish();
                    overridePendingTransition(R.anim.none, R.anim.fadeout_up);
                } else {
                    Toast.makeText(SearchFriendActivity.this, "이미 등록된 친구입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchFriendBinding.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNickName = searchFriendBinding.userNickName.getText().toString();
                
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