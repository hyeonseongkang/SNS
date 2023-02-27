package com.mirror.sns.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.mirror.sns.R;
import com.mirror.sns.adapter.PostAdapter;
import com.mirror.sns.model.Post;
import com.mirror.sns.model.User;
import com.mirror.sns.databinding.ActivityDetailUserBinding;
import com.mirror.sns.viewmodel.PostViewModel;
import com.mirror.sns.viewmodel.UserManagementViewModel;

import java.util.List;

public class DetailUserActivity extends AppCompatActivity {

    public static final String TAG = "DetailUserActivity";

    ActivityDetailUserBinding detailUserBinding;

    private PostViewModel postViewModel;
    private UserManagementViewModel userManagementViewModel;

    String userUid;

    private User requestUser;
    private User responseUser;

    private PostAdapter postAdapter;

    private FirebaseAuth firebaseAuth;

    private Boolean follow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailUserBinding = ActivityDetailUserBinding.inflate(getLayoutInflater());
        setContentView(detailUserBinding.getRoot());
        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        firebaseAuth = FirebaseAuth.getInstance();

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

                    if (user.getPhotoUri().length() > 0) {
                        Glide.with(DetailUserActivity.this)
                                .load(Uri.parse(user.getPhotoUri()))
                                .into(detailUserBinding.userPhoto);
                    }
                } else {
                    requestUser = user;
                    if (follow) {
                        userManagementViewModel.follow(responseUser, requestUser);
                    } else {
                        userManagementViewModel.unFollow(responseUser, requestUser);
                    }
                }

            }
        });

        userManagementViewModel.getFollowCheck(userUid, firebaseAuth.getUid());
        userManagementViewModel.getFollowCheck().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    // follow 상태
                    detailUserBinding.followButton.setVisibility(View.GONE);
                    detailUserBinding.unFollowButton.setVisibility(View.VISIBLE);
                } else {
                    // unfollow상태
                    detailUserBinding.unFollowButton.setVisibility(View.GONE);
                    detailUserBinding.followButton.setVisibility(View.VISIBLE);
                }
            }
        });

        postViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(PostViewModel.class);


        detailUserBinding.postsRecyclerView.setLayoutManager(new GridLayoutManager(DetailUserActivity.this, 3));
        detailUserBinding.postsRecyclerView.setHasFixedSize(true);
        postAdapter = new PostAdapter();
        detailUserBinding.postsRecyclerView.setAdapter(postAdapter);

        postAdapter.setOnItemClickListener(new PostAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Post post) {
                Intent intent = new Intent(DetailUserActivity.this, DetailPostActivity.class);
                intent.putExtra("userUid", post.getUserUid());
                intent.putExtra("itemKey", post.getKey());
                startActivity(intent);
            }
        });

        postViewModel.getUserPosts(userUid);
        postViewModel.getUserPosts().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                postAdapter.setPosts(posts);
            }
        });


        userManagementViewModel.getFollowRequest().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    userManagementViewModel.getUserInfo(userUid);
                    detailUserBinding.followButton.setVisibility(View.GONE);
                    detailUserBinding.unFollowButton.setVisibility(View.VISIBLE);
                }
            }
        });

        userManagementViewModel.getUnFollowRequest().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    userManagementViewModel.getUserInfo(userUid);
                    detailUserBinding.unFollowButton.setVisibility(View.GONE);
                    detailUserBinding.followButton.setVisibility(View.VISIBLE);
                }
            }
        });

        detailUserBinding.followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                follow = true;
                userManagementViewModel.getUserInfo(FirebaseAuth.getInstance().getUid());
            }
        });

        detailUserBinding.unFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                follow = false;
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