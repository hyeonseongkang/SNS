package com.mirror.sns.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mirror.sns.R;

public class CommentActivity extends AppCompatActivity {

    public static String TAG = "CommentActivity";

    // finish();
    //                overridePendingTransition(R.anim.none, R.anim.fadeout_left);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        overridePendingTransition(R.anim.fadein_left, R.anim.none);
    }
}