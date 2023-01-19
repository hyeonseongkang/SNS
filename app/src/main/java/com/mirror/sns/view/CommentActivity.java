package com.mirror.sns.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.mirror.sns.R;
import com.mirror.sns.classes.Comment;
import com.mirror.sns.classes.User;
import com.mirror.sns.databinding.ActivityCommentBinding;
import com.mirror.sns.databinding.ActivityDetailPostBinding;
import com.mirror.sns.viewmodel.PostViewModel;
import com.mirror.sns.viewmodel.UserManagementViewModel;

public class CommentActivity extends AppCompatActivity {

    public static String TAG = "CommentActivity";

    private ActivityCommentBinding commentBinding;
    private PostViewModel postViewModel;
    private UserManagementViewModel userManagementViewModel;

    String userUid;
    String itemKey;

    private User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commentBinding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(commentBinding.getRoot());

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

                postViewModel.setComment(itemKey, new Comment(currentUser, "",  comment));
            }
        });

        commentBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_left);
            }
        });
    }
}