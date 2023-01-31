package com.mirror.sns.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.mirror.sns.R;
import com.mirror.sns.classes.User;
import com.mirror.sns.databinding.ActivityAddFriendBinding;
import com.mirror.sns.viewmodel.UserManagementViewModel;

import java.util.List;

public class AddFriendActivity extends AppCompatActivity {

    private ActivityAddFriendBinding addFriendBinding;

    private UserManagementViewModel userManagementViewModel;

    private List<User> currUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFriendBinding = ActivityAddFriendBinding.inflate(getLayoutInflater());
        setContentView(addFriendBinding.getRoot());
        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        userManagementViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(UserManagementViewModel.class);
        userManagementViewModel.getUserList();
        userManagementViewModel.getUserListLiveData().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                currUsers = users;
            }
        });
        addFriendBinding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText = addFriendBinding.input.getText().toString();

                if (TextUtils.isEmpty(inputText))
                    return;

                for (User user: currUsers) {
                    if (user.getNickName().equals(inputText)) {
                        // find user
                        break;
                    }
                }
            }
        });



        addFriendBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_left);
            }
        });
    }
}