package com.mirror.sns.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import com.mirror.sns.R;
import com.mirror.sns.databinding.ActivityCreatePostBinding;

public class CreatePostActivity extends AppCompatActivity {

    ActivityCreatePostBinding binding;
    private Uri tempPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postText = binding.postText.getText().toString();

                if (TextUtils.isEmpty(postText))
                    return;

                // save
                /*
                tempPhotoUri
                postText
                // friend Tag
                 */
            }
        });

        binding.tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 친구 리스트 가져오기
            }
        });

        // 아이템 사진 추가
        binding.gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                getPhotoLauncher.launch(intent);
            }
        });
    }

    ActivityResultLauncher<Intent> getPhotoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        tempPhotoUri = intent.getData();

                       if (tempPhotoUri != null) {
                            tempPhotoUri = null;
                        }
                    }
                }
            });
}