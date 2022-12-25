package com.mirror.sns.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.mirror.sns.R;
import com.mirror.sns.adapter.DetailPostItemAdapter;
import com.mirror.sns.classes.Post;
import com.mirror.sns.databinding.ActivityDetailPostBinding;
import com.mirror.sns.viewmodel.PostViewModel;

public class DetailPostActivity extends AppCompatActivity {

    private static final String TAG = "DetailPostActivity";
    private FirebaseAuth firebaseAuth;
    private ActivityDetailPostBinding detailPostBinding;

    private PostViewModel postViewModel;

    private String userUid = null;
    private String itemkey = null;

    private Post currentPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailPostBinding = ActivityDetailPostBinding.inflate(getLayoutInflater());
        setContentView(detailPostBinding.getRoot());

        overridePendingTransition(R.anim.fadein_left, R.anim.none);
        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();

        userUid = intent.getStringExtra("userUid");
        itemkey = intent.getStringExtra("itemKey");


        postViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(PostViewModel.class);
        postViewModel.getPostLiveData().observe(this, new Observer<Post>() {
            @Override
            public void onChanged(Post post) {
                // Post(String key, String userUid, String content, String userPhotoUri, ArrayList<String> photoKeys, String firstPhotoUri, ArrayList<String> likes) {
                currentPost = post;

                detailPostBinding.content.setText(currentPost.getContent());
                detailPostBinding.userName.setText(currentPost.getUserUid());

                Glide.with(DetailPostActivity.this)
                        .load(Uri.parse(currentPost.getPostPhotoUri()))
                        .into(detailPostBinding.postPhoto);

                if (currentPost.getUserPhotoUri() != null && currentPost.getUserPhotoUri().length() > 0) {
                    Glide.with(DetailPostActivity.this)
                            .load(Uri.parse(currentPost.getUserPhotoUri()))
                            .into(detailPostBinding.userPhoto);
                } else {
                    Glide.with(DetailPostActivity.this)
                            .load(R.drawable.basic_profile_photo)
                            .into(detailPostBinding.userPhoto);
                }

            }
        });
        postViewModel.getPost(userUid, itemkey);


        // 현재 아이템을 누른 uid가 아이템의 좋아요를 눌렀는지 확인
        postViewModel.getLike(itemkey, firebaseAuth.getUid());
        postViewModel.getLike().observe(DetailPostActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                //Log.d(TAG, aBoolean.toString());
                if (aBoolean)
                    detailPostBinding.like.setBackgroundResource(R.drawable.red_heart);
                else
                    detailPostBinding.like.setBackgroundResource(R.drawable.basic_heart);
            }
        });

        detailPostBinding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postViewModel.setLike(itemkey, firebaseAuth.getUid());
            }
        });

        detailPostBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_left);
            }
        });
    }
}