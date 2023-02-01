package com.mirror.sns.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.mirror.sns.R;
import com.mirror.sns.classes.User;
import com.mirror.sns.databinding.ActivityAddFriendBinding;
import com.mirror.sns.viewmodel.UserManagementViewModel;

import java.util.List;

public class AddFriendActivity extends AppCompatActivity {

    private static final String TAG = "AddFriendActivity";
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

        userManagementViewModel.getFindUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.d(TAG, "찾았따." + user.getNickName());
            }
        });

        addFriendBinding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText = addFriendBinding.input.getText().toString();

                userManagementViewModel.getFindUser(inputText);

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