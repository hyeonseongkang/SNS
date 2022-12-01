package com.mirror.sns.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.mirror.sns.R;
import com.mirror.sns.adapter.DetailPostItemAdapter;
import com.mirror.sns.classes.Post;
import com.mirror.sns.databinding.ActivityDetailPostBinding;
import com.mirror.sns.viewmodel.PostViewModel;

public class DetailPostActivity extends AppCompatActivity {

    private static final String TAG = "DetailPostActivity";

    private ActivityDetailPostBinding detailPostBinding;

    private PostViewModel postViewModel;

    private String itemkey = null;

    private Post currentPost;


    // adapter
    DetailPostItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailPostBinding = ActivityDetailPostBinding.inflate(getLayoutInflater());
        setContentView(detailPostBinding.getRoot());

        overridePendingTransition(R.anim.fadein_left, R.anim.none);


        Intent intent = getIntent();

        itemkey = intent.getStringExtra("key");


        postViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(PostViewModel.class);
        postViewModel.getPostLiveData().observe(this, new Observer<Post>() {
            @Override
            public void onChanged(Post post) {
                // Post(String key, String userUid, String content, String userPhotoUri, ArrayList<String> photoKeys, String firstPhotoUri, ArrayList<String> likes) {
                currentPost = post;

                detailPostBinding.content.setText(post.getContent());
                detailPostBinding.userName.setText(post.getUserUid());

                String userPhoto = post.getFirstPhotoUri();

                // 판매자 profile 사진이 있으면 가져오고 아니면 기본 이미지
                if (!(userPhoto.equals("null"))) {
                    Glide.with(DetailPostActivity.this)
                            .load(userPhoto)
                            .into(detailPostBinding.userPhoto);
                }

            }
        });
        postViewModel.getPost(itemkey);

        // horizontal recyclerview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        detailPostBinding.recyclerView.setLayoutManager(linearLayoutManager);
        detailPostBinding.recyclerView.setHasFixedSize(true);

        adapter = new DetailPostItemAdapter();
        detailPostBinding.recyclerView.setAdapter(adapter);
    }
}