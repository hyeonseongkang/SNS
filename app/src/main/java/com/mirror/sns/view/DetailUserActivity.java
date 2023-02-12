package com.mirror.sns.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.mirror.sns.R;
import com.mirror.sns.classes.User;
import com.mirror.sns.databinding.ActivityDetailUserBinding;
import com.mirror.sns.viewmodel.UserManagementViewModel;

public class DetailUserActivity extends AppCompatActivity {

    public static final String TAG = "DetailUserActivity";

    ActivityDetailUserBinding detailUserBinding;

    private UserManagementViewModel userManagementViewModel;

    String userUid;

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
                //  followingCount postCount followerCount userPhoto userNickName userJob userIntroduction userEmail userPhoto postsRecyclerView
                detailUserBinding.userNickName.setText(user.getNickName());
                detailUserBinding.postCount.setText(user.getPosts());
                detailUserBinding.followerCount.setText(user.getFollowers());
                detailUserBinding.followingCount.setText(user.getFollowing());
                detailUserBinding.userEmail.setText(user.getEmail());

                if (user.getPhotoUri().length() > 0 ) {
                    Glide.with(DetailUserActivity.this)
                            .load(Uri.parse(user.getPhotoUri()))
                            .into(detailUserBinding.userPhoto);
                }
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