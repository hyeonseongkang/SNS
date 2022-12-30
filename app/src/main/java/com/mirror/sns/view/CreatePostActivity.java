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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.mirror.sns.R;
import com.mirror.sns.adapter.SnsPhotoItemAdapter;
import com.mirror.sns.adapter.TagAdapter;
import com.mirror.sns.classes.Post;
import com.mirror.sns.classes.Sns;
import com.mirror.sns.classes.Tag;
import com.mirror.sns.classes.User;
import com.mirror.sns.databinding.ActivityCreatePostBinding;
import com.mirror.sns.model.PostRepository;
import com.mirror.sns.viewmodel.LoginViewModel;
import com.mirror.sns.viewmodel.PostViewModel;
import com.mirror.sns.viewmodel.UserManagementViewModel;

import java.util.ArrayList;

public class CreatePostActivity extends AppCompatActivity {

    ActivityCreatePostBinding createPostBinding;

    private FirebaseAuth firebaseAuth;
    private PostViewModel postViewModel;
    private UserManagementViewModel userManagementViewModel;
    private Uri tempPhotoUri;


    // photo
    private String postPhoto;
    private String userPhotoUri;

    // tags
    private ArrayList<Tag> tags;

    SnsPhotoItemAdapter adapter;
    TagAdapter tagAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createPostBinding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(createPostBinding.getRoot());
        overridePendingTransition(R.anim.fadein_left, R.anim.none);
        firebaseAuth = FirebaseAuth.getInstance();
        tags = new ArrayList<>();

        postViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(PostViewModel.class);
        postViewModel.getSuccessCreatePost().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    finish();
                    overridePendingTransition(R.anim.none, R.anim.fadeout_left);
                }
            }
        });

        userManagementViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(UserManagementViewModel.class);

        userManagementViewModel.getUserInfo(firebaseAuth.getUid());
        userManagementViewModel.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                userPhotoUri = user.getPhotoUri();
            }
        });



        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        createPostBinding.tagRecyclerView.setLayoutManager(layoutManager1);
        createPostBinding.tagRecyclerView.setHasFixedSize(true);

        tagAdapter = new TagAdapter();
        createPostBinding.tagRecyclerView.setAdapter(tagAdapter);



        tagAdapter.setOnItemClickListener(new TagAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                tags.remove(position);
                tagAdapter.notifyItemRemoved(position);
                tagAdapter.notifyItemChanged(position, tags.size());
            }
        });

        createPostBinding.tagText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    // 1. recyclerview에 tag추가
                    String tagT = createPostBinding.tagText.getText().toString();
                    tags.add(new Tag(tagT));
                    tagAdapter.setTagList(tags, false);
                    // 2. tag text 지우기
                    createPostBinding.tagText.setText("");
                }
                return false;
            }
        });


        createPostBinding.createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postText = createPostBinding.postText.getText().toString();

                if (TextUtils.isEmpty(postText))
                    return;

                String userUid = firebaseAuth.getUid();
                String tempUserPhotoUri;
                
                if (userPhotoUri.length() < 0 ) {
                    tempUserPhotoUri = "";
                }  else {
                    tempUserPhotoUri = userPhotoUri;
                }




                if (postPhoto.length() <= 0) return;

                //  public Post(String key, String userUid, String content, String userPhotoUri, String postPhotoUri, ArrayList<Tag> tags, ArrayList<String> likes)
                Post post = new Post("", userUid, postText, tempUserPhotoUri, postPhoto, tags, new ArrayList<>());
                postViewModel.createPost(post);

                // save
                /*
                tempPhotoUri
                postText
                // friend Tag
                 */
            }
        });

        createPostBinding.tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 친구 리스트 가져오기
            }
        });

        createPostBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_left);
            }
        });

        // 아이템 사진 추가
        createPostBinding.gallery.setOnClickListener(new View.OnClickListener() {
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
                            postPhoto = tempPhotoUri.toString();

                            Glide.with(getApplication())
                                    .load(Uri.parse(postPhoto))
                                    .into(createPostBinding.postPhoto);

                            tempPhotoUri = null;
                        }
                    }
                }
            });
}