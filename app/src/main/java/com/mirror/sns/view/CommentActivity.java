package com.mirror.sns.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mirror.sns.R;
import com.mirror.sns.databinding.ActivityCommentBinding;
import com.mirror.sns.databinding.ActivityDetailPostBinding;

public class CommentActivity extends AppCompatActivity {

    public static String TAG = "CommentActivity";

    // finish();
    //                overridePendingTransition(R.anim.none, R.anim.fadeout_left);

    private ActivityCommentBinding commentBinding;

    String userUid;
    String itemKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commentBinding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(commentBinding.getRoot());

        Intent intent = getIntent();
        userUid = intent.getStringExtra("userUid");
        itemKey = intent.getStringExtra("itemKey");

        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        commentBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_left);
            }
        });
    }
}