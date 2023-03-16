package com.mirror.sns.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mirror.sns.R;
import com.mirror.sns.databinding.ActivitySettingRadiusBinding;

public class SettingRadiusActivity extends AppCompatActivity {

    ActivitySettingRadiusBinding settingRadiusBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingRadiusBinding = ActivitySettingRadiusBinding.inflate(getLayoutInflater());
        setContentView(settingRadiusBinding.getRoot());
    }
}