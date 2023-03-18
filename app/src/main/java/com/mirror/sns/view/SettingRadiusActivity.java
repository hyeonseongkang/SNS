package com.mirror.sns.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.mirror.sns.R;
import com.mirror.sns.databinding.ActivitySettingRadiusBinding;
import com.mirror.sns.viewmodel.PostViewModel;
import com.mirror.sns.viewmodel.SettingViewModel;

public class SettingRadiusActivity extends AppCompatActivity {

    ActivitySettingRadiusBinding settingRadiusBinding;

    SettingViewModel settingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingRadiusBinding = ActivitySettingRadiusBinding.inflate(getLayoutInflater());
        setContentView(settingRadiusBinding.getRoot());
        overridePendingTransition(R.anim.fadein_left, R.anim.none);
        settingViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(SettingViewModel.class);

        settingViewModel.getSetRadiusResult().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {

                }
            }
        });
        settingRadiusBinding.applyRadius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String radius = settingRadiusBinding.radius.getText().toString();
                settingViewModel.setRadius(radius);
            }
        });

        settingRadiusBinding.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double radius = Double.parseDouble(settingRadiusBinding.radius.getText().toString());
                radius -= 0.5;
                if (radius > 0 ) {
                    settingRadiusBinding.radius.setText(String.valueOf(radius));
                }
            }
        });

        settingRadiusBinding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double radius = Double.parseDouble(settingRadiusBinding.radius.getText().toString());
                radius += 0.5;
                if (radius < 10.1) {
                    settingRadiusBinding.radius.setText(String.valueOf(radius));
                }
            }
        });
    }
}