package com.mirror.sns.model;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mirror.sns.classes.Post;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PostRepository {
    public static final String TAG = "PostRepository";

    private Application application;

    private DatabaseReference postsRef;

    private MutableLiveData<List<Post>> postsLiveData;
    private List<Post> posts;

    private MutableLiveData<Post> post;

    public PostRepository(Application application) {
        this.application = application;
        postsRef = FirebaseDatabase.getInstance().getReference("posts");
        postsLiveData = new MutableLiveData<>();
        post = new MutableLiveData<>();
        posts = new ArrayList<>();
    }

    public MutableLiveData<List<Post>> getPostsLiveData() {
        return postsLiveData;
    }

    public void createPost(Post post) {
        String userUid = post.getUserUid();
        String key = postsRef.getKey();
        String content = post.getContent();
        String userPhotoUri = post.getUserPhotoUri();
        ArrayList<String> likes = post.getLikes();
        ArrayList<String> photoKeys = post.getPhotoKeys();

        // first save photo after save data

                /*
        1. 모든 photo를 firebase store에 저장시도
        2. 저장 성공시 uri를 다운받아 tempPhotoKey에 저장
        3. 마지막 Photo를 store에 저장하기를 성공했다면 item Ref / item 저장
         */
        ArrayList<String> tempPhotokeys = new ArrayList<>();
        for (int i = 0; i < photoKeys.size(); i++) {
            String photoKey = postsRef.push().getKey();
            StorageReference storage = FirebaseStorage.getInstance().getReference().child("items/" + photoKey + ".jpg");
            UploadTask uploadTask = storage.putFile(Uri.parse(photoKeys.get(i)));
            int finalI = i;
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            tempPhotokeys.add(uri.toString());

                            if (finalI == photoKeys.size() - 1) {
                                // public Post(String key, String userUid, String content, String userPhotoUri, ArrayList<String> photoKeys, String firstPhotoUri, ArrayList<String> likes) {
                                Post post = new Post(key, userUid, content, userPhotoUri, tempPhotokeys, tempPhotokeys.get(0), likes);
                                postsRef.child(key).setValue(post);
                            }
                        }
                    });

                }
            });

        }

        postsRef.child(userUid).push().setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Create Post");
                } else {

                }
            }
        });
    }

    // key에 해당하는 post 정보 가져오기
    public void getPost(String key) {
        postsRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Post tempPost = snapshot.getValue(Post.class);
                post.setValue(tempPost);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void getPosts() {
        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                posts.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    for (DataSnapshot snapshot2: snapshot1.getChildren()) {
                        Log.d( "post snapshot2 data ", snapshot2.getValue().toString());
                        Post post = snapshot2.getValue(Post.class);
                        posts.add(post);
                    }
                }
                postsLiveData.setValue(posts);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}
