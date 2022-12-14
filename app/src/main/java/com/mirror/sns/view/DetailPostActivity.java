package com.mirror.sns.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.mirror.sns.R;
import com.mirror.sns.adapter.DetailPostItemAdapter;
import com.mirror.sns.adapter.TagAdapter;
import com.mirror.sns.classes.Post;
import com.mirror.sns.classes.Tag;
import com.mirror.sns.classes.User;
import com.mirror.sns.databinding.ActivityDetailPostBinding;
import com.mirror.sns.viewmodel.LoginViewModel;
import com.mirror.sns.viewmodel.PostViewModel;
import com.mirror.sns.viewmodel.UserManagementViewModel;

import java.util.List;

public class DetailPostActivity extends AppCompatActivity {

    private static final String TAG = "DetailPostActivity";
    private FirebaseAuth firebaseAuth;
    private ActivityDetailPostBinding detailPostBinding;
    private UserManagementViewModel userManagementViewModel;

    private PostViewModel postViewModel;

    private String userUid = null;
    private String itemkey = null;

    private Post currentPost;

    private String userName;
    private String userPhoto;
    private String userEmail;

    private User currentUser;

    TagAdapter tagAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailPostBinding = ActivityDetailPostBinding.inflate(getLayoutInflater());
        setContentView(detailPostBinding.getRoot());

        overridePendingTransition(R.anim.fadein_left, R.anim.none);
        firebaseAuth = FirebaseAuth.getInstance();

        userManagementViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(UserManagementViewModel.class);

        userManagementViewModel.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                // user data
                currentUser = user;
                userName = user.getNickName();
                userPhoto = user.getPhotoUri();
                userEmail = user.getEmail();
                detailPostBinding.userName.setText(userName);
            }
        });

        Intent intent = getIntent();

        userUid = intent.getStringExtra("userUid");
        itemkey = intent.getStringExtra("itemKey");


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        detailPostBinding.tagRecyclerView.setLayoutManager(layoutManager);
        detailPostBinding.tagRecyclerView.setHasFixedSize(true);

        tagAdapter = new TagAdapter();
        detailPostBinding.tagRecyclerView.setAdapter(tagAdapter);

        userManagementViewModel.getUserInfo(userUid);
        postViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(PostViewModel.class);
        postViewModel.getPostLiveData().observe(this, new Observer<Post>() {
            @Override
            public void onChanged(Post post) {
                // Post(String key, String userUid, String content, String userPhotoUri, ArrayList<String> photoKeys, String firstPhotoUri, ArrayList<String> likes) {
                currentPost = post;

                List<User> users = currentPost.getLikes();


                detailPostBinding.content.setText(currentPost.getContent());
                detailPostBinding.userName.setText(currentPost.getUserUid());

                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    if (i == 0) {
                        Glide.with(DetailPostActivity.this)
                                .load(Uri.parse(user.getPhotoUri()))
                                .into(detailPostBinding.userPhoto1);
                    } else if (i == 1) {
                        Glide.with(DetailPostActivity.this)
                                .load(Uri.parse(user.getPhotoUri()))
                                .into(detailPostBinding.userPhoto2);
                    } else if (i == 2) {
                        Glide.with(DetailPostActivity.this)
                                .load(Uri.parse(user.getPhotoUri()))
                                .into(detailPostBinding.userPhoto2);
                    }
                }

                tagAdapter.setTagList(currentPost.getTags(), true);
                Glide.with(DetailPostActivity.this)
                        .load(Uri.parse(currentPost.getPostPhotoUri()))
                        .into(detailPostBinding.postPhoto);

                if (currentPost.getUserPhotoUri() != null && currentPost.getUserPhotoUri().length() > 0) {
                    Glide.with(DetailPostActivity.this)
                            .load(Uri.parse(currentPost.getUserPhotoUri()))
                            .into(detailPostBinding.userPhoto);
                } else {
                    Glide.with(DetailPostActivity.this)
                            .load(R.drawable.basic_profile_photo)
                            .into(detailPostBinding.userPhoto);
                }

            }
        });
        postViewModel.getPost(userUid, itemkey);


        postViewModel.getLikePressUsers().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                // ????????? ?????? ?????? uid ?????????
            }
        });
        postViewModel.getLikePressUsers(itemkey, userUid);


        // ?????? ???????????? ?????? uid??? ???????????? ???????????? ???????????? ??????
        postViewModel.getLike(itemkey, firebaseAuth.getUid());
        postViewModel.getLike().observe(DetailPostActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                //Log.d(TAG, aBoolean.toString());
                if (aBoolean)
                    detailPostBinding.like.setBackgroundResource(R.drawable.red_heart);
                else
                    detailPostBinding.like.setBackgroundResource(R.drawable.basic_heart);
            }
        });

        detailPostBinding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postViewModel.setLike(userUid, itemkey, currentUser);
            }
        });

        detailPostBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_left);
            }
        });
    }
}