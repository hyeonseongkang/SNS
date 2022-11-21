package com.mirror.sns.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.mirror.sns.R;
import com.mirror.sns.adapter.SnsPhotoItemAdapter;
import com.mirror.sns.classes.Post;
import com.mirror.sns.classes.User;
import com.mirror.sns.databinding.ActivityCreatePostBinding;
import com.mirror.sns.model.PostRepository;
import com.mirror.sns.viewmodel.LoginViewModel;
import com.mirror.sns.viewmodel.PostViewModel;
import com.mirror.sns.viewmodel.UserManagementViewModel;

import java.util.ArrayList;

public class CreatePostActivity extends AppCompatActivity {

    ActivityCreatePostBinding binding;

    private FirebaseAuth firebaseAuth;
    private PostViewModel postViewModel;
    private UserManagementViewModel userManagementViewModel;
    private Uri tempPhotoUri;


    // photo
    private ArrayList<String> itemPhotos;
    private String userPhotoUri;

    SnsPhotoItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        overridePendingTransition(R.anim.fadein_left, R.anim.none);
        firebaseAuth = FirebaseAuth.getInstance();
        itemPhotos = new ArrayList<>();
        postViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(PostViewModel.class);
        userManagementViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(UserManagementViewModel.class);

        userManagementViewModel.getUserInfo(firebaseAuth.getUid());
        userManagementViewModel.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                userPhotoUri = user.getPhotoUri();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.photoItemRecyclerView.setLayoutManager(layoutManager);
        binding.photoItemRecyclerView.setHasFixedSize(true);

        adapter = new SnsPhotoItemAdapter();
        binding.photoItemRecyclerView.setAdapter(adapter);

        // 아이템 사진 추가 했다가 취소
        adapter.setOnItemClickListener(new SnsPhotoItemAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                itemPhotos.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, itemPhotos.size());
                binding.photoCount.setText(String.valueOf(itemPhotos.size()));
            }
        });


        binding.createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postText = binding.postText.getText().toString();

                if (TextUtils.isEmpty(postText))
                    return;

                String userUid = firebaseAuth.getUid();

                postViewModel.createPost(new Post(null,  userPhotoUri, String.valueOf(tempPhotoUri), userUid, postText));
                overridePendingTransition(R.anim.none, R.anim.fadeout_left);
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

                        // 갤러리에서 사진 데이터를 가져와서 null check 후 값이 있다면 itemPhotos에 담음 후에 아이템 생성할 때 itemPhotos를 photo key로 바꿔 저장함
                        if (tempPhotoUri != null) {
                            itemPhotos.add(tempPhotoUri.toString());
                            adapter.setPhotoUris(itemPhotos);
                            tempPhotoUri = null;
                            binding.photoCount.setText(String.valueOf(itemPhotos.size()));
                        }
                    }
                }
            });
}