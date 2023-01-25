package com.mirror.sns.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.mirror.sns.R;
import com.mirror.sns.adapter.CommentAdapter;
import com.mirror.sns.classes.Comment;
import com.mirror.sns.classes.User;
import com.mirror.sns.databinding.ActivityCommentBinding;
import com.mirror.sns.databinding.ActivityDetailPostBinding;
import com.mirror.sns.viewmodel.PostViewModel;
import com.mirror.sns.viewmodel.UserManagementViewModel;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    public static String TAG = "CommentActivity";

    private ActivityCommentBinding commentBinding;
    private PostViewModel postViewModel;
    private UserManagementViewModel userManagementViewModel;

    String userUid;
    String itemKey;

    private CommentAdapter commentAdapter;

    private User currentUser;
    private List<Comment> currComments;
    private Comment currComment;
    private Boolean commentLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commentBinding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(commentBinding.getRoot());
        currComments = new ArrayList<>();
        Intent intent = getIntent();
        userUid = intent.getStringExtra("userUid");
        itemKey = intent.getStringExtra("itemKey");

        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        postViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(PostViewModel.class);
        userManagementViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(UserManagementViewModel.class);

        userManagementViewModel.getUserInfo(userUid);
        userManagementViewModel.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                currentUser = user;

                if (currentUser.getPhotoUri() != null && currentUser.getPhotoUri().length() > 0) {
                    Glide.with(CommentActivity.this)
                            .load(Uri.parse(currentUser.getPhotoUri()))
                            .into(commentBinding.userPhoto);
                } else {
                    Glide.with(CommentActivity.this)
                            .load(R.drawable.basic_profile_photo)
                            .into(commentBinding.userPhoto);
                }
            }
        });
        commentBinding.sendComment.setEnabled(true);
        commentBinding.sendComment.setClickable(true);
        commentBinding.sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = commentBinding.commentText.getText().toString();

                if (TextUtils.isEmpty(comment))
                    return;

                postViewModel.setComment(itemKey, new Comment(currentUser, "",  comment, ""));
                commentBinding.commentText.setText("");
            }
        });


        commentAdapter = new CommentAdapter();
        commentAdapter.setOnItemClickListener(new CommentAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Comment curr = currComments.get(position);
                currComment = curr;
                postViewModel.getCommentLikeUser(itemKey, curr.getKey());

            }
        });

        postViewModel.getCommentLikeUser().observe(CommentActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    postViewModel.setCommentLikeUser(itemKey, currComment.getKey(), currentUser.getUid());
                }
            }
        });

        commentBinding.commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentBinding.commentRecyclerView.setHasFixedSize(true);
        commentBinding.commentRecyclerView.setAdapter(commentAdapter);

        postViewModel.getComments(itemKey);
        postViewModel.getComments().observe(this, new Observer<List<Comment>>() {
            @Override
            public void onChanged(List<Comment> comments) {
                currComments = comments;
                commentAdapter.setComments(comments);
            }
        });

//        List<Comment> comments = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            comments.add(new Comment(new User(), "", "댓글 테스트 " + i));
//        }
//        commentAdapter.setComments(comments);

        commentBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_left);
            }
        });
    }
}