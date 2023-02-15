package com.mirror.sns.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.mirror.sns.R;
import com.mirror.sns.classes.User;
import com.mirror.sns.databinding.ActivityDetailUserBinding;
import com.mirror.sns.viewmodel.UserManagementViewModel;

public class DetailUserActivity extends AppCompatActivity {

    public static final String TAG = "DetailUserActivity";

    ActivityDetailUserBinding detailUserBinding;

    private UserManagementViewModel userManagementViewModel;

    String userUid;

    private User requestUser;
    private User responseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailUserBinding = ActivityDetailUserBinding.inflate(getLayoutInflater());
        setContentView(detailUserBinding.getRoot());
        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        Intent intent = getIntent();
        userUid = intent.getStringExtra("userUid");

        userManagementViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(UserManagementViewModel.class);
        userManagementViewModel.getUserInfo(userUid);
        userManagementViewModel.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {

                if (user.getUid().equals(userUid)) {
                    responseUser = user;
                    //  followingCount postCount followerCount userPhoto userNickName userJob userIntroduction userEmail userPhoto postsRecyclerView
                    detailUserBinding.userNickName.setText(user.getNickName());

                    String postSize = user.getPosts() == null ? "0" : String.valueOf(user.getPosts().size());
                    String followerSize = user.getFollowerUsers() == null ? "0" : String.valueOf(user.getFollowerUsers().size());
                    String followingSize = user.getFollowingUsers() == null ? "0" : String.valueOf(user.getFollowingUsers().size());

                    detailUserBinding.postCount.setText(postSize);
                    detailUserBinding.followerCount.setText(followerSize);
                    detailUserBinding.followingCount.setText(followingSize);

                    if (user.getPhotoUri().length() > 0 ) {
                        Glide.with(DetailUserActivity.this)
                                .load(Uri.parse(user.getPhotoUri()))
                                .into(detailUserBinding.userPhoto);
                    }
                } else {
                    requestUser = user;
                    userManagementViewModel.follow(responseUser, requestUser);
                }

            }
        });

        userManagementViewModel.getFollowRequest().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(DetailUserActivity.this, "팔로우 성공", Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(R.anim.none, R.anim.fadeout_left);
                }
            }
        });

        detailUserBinding.followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userManagementViewModel.getUserInfo(FirebaseAuth.getInstance().getUid());
            }
        });

        detailUserBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_left);
            }
        });

    }
}