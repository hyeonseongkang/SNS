package com.mirror.sns.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mirror.sns.R;
import com.mirror.sns.databinding.ActivityDetailUserBinding;

public class DetailUserActivity extends AppCompatActivity {

    public static final String TAG = "DetailUserActivity";

    ActivityDetailUserBinding detailUserBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailUserBinding = ActivityDetailUserBinding.inflate(getLayoutInflater());
        setContentView(detailUserBinding.getRoot());
    }
}