package com.mirror.sns.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.mirror.sns.R;
import com.mirror.sns.databinding.ActivityAddFriendBinding;

public class AddFriendActivity extends AppCompatActivity {

    private ActivityAddFriendBinding addFriendBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFriendBinding = ActivityAddFriendBinding.inflate(getLayoutInflater());
        setContentView(addFriendBinding.getRoot());
        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        addFriendBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_left);
            }
        });
    }
}