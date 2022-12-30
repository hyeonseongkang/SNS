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
import com.mirror.sns.classes.Tag;

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
    private MutableLiveData<Boolean> like;
    private MutableLiveData<Boolean> successCreatePost;

    public PostRepository(Application application) {
        this.application = application;
        postsRef = FirebaseDatabase.getInstance().getReference("posts");
        postsLiveData = new MutableLiveData<>();
        successCreatePost = new MutableLiveData<>();
        post = new MutableLiveData<>();
        posts = new ArrayList<>();
        like = new MutableLiveData<>();
    }

    public MutableLiveData<List<Post>> getPostsLiveData() {
        return postsLiveData;
    }

    public MutableLiveData<Post> getPostLiveData() { return post; }

    public MutableLiveData<Boolean> getLike() { return like; }

    public MutableLiveData<Boolean> getSuccessCreatePost() { return successCreatePost;}

    public void createPost(Post post) {
        String userUid = post.getUserUid();
        String key = postsRef.push().getKey();
        String content = post.getContent();
        String userPhotoUri = post.getUserPhotoUri();
        ArrayList<String> likes = post.getLikes();
        String postPhoto = post.getPostPhotoUri();
        ArrayList<Tag> tags = post.getTags();

        String photoKey = postsRef.push().getKey();
        StorageReference storage = FirebaseStorage.getInstance().getReference().child("posts/" + photoKey + ".jpg");
        UploadTask uploadTask = storage.putFile(Uri.parse(postPhoto));
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //  public Post(String key, String userUid, String content, String userPhotoUri, String postPhotoUri, ArrayList<Tag> tags, ArrayList<String> likes)
                        Post post = new Post(key, userUid, content, userPhotoUri, uri.toString(), tags, likes);
                        postsRef.child(userUid).child(key).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    successCreatePost.setValue(true);
                                }
                            }
                        });
                    }
                });

            }
        });


    }

    // key에 해당하는 post 정보 가져오기
    public void getPost(String userUid, String key) {
        postsRef.child(userUid).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
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

    // post 좋아요
    public void setLike(String key, String uid) {
        // key = item
        // uid = 좋아요 누른 사람 uid

        /*
        1. postsRef / key / likes의 값들을 모두 가져온다.
        2. likes에는 해당 아이템의 좋아요를 누른 사람들의 uid가 저장되어있다.
        3. 해당 아이템의 좋아요를 누른 uid 리스트에 인자값으로 넘어온 uid(현재 아이템의 좋아요를 누른사람)가 없다면 해당 아이템에 대해 처음 좋아요를 누른 사람이므로 likes ref list에 uid를 추가한다.
        4. 만약 리스트에 uid가 있다면 좋아요를 취소한 것으로 해당 uid를 삭제한다.
         */
        postsRef.child(key).child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<String> likes = new ArrayList<>();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    if (snapshot1.getValue() != null) {
                        Log.d(TAG, snapshot1.getValue().toString());
                        String userUid = snapshot1.getValue(String.class);
                        likes.add(userUid);
                    }
                }

                // 현재 아이템 좋아요 리스트에 uid가 없다면 추가하고 uid가 있다면 삭제 -> toggle
                if (!(likes.contains(uid))) {
                    likes.add(uid);
                    like.setValue(true);
                } else if (likes.contains(uid)) {
                    likes.remove(uid);
                    like.setValue(false);
                }

                postsRef.child(key).child("likes").setValue(likes);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    // 좋아요 가져오기
    public void getLike(String key, String uid) {
        // 인자값으로 넘어온 uid가 key에 해당하는 아이템의 좋아요를 눌렀다면 화면에 빨간색 하트를 표시하기 위해 사용
        postsRef.child(key).child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<String> likes = new ArrayList<>();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    if (snapshot1.getValue() != null) {
                        String userUid = snapshot1.getValue(String.class);
                        likes.add(userUid);
                    }
                }

                // like list에 uid user가 있다면 true -> 빨간색 하트 표시
                if (likes.contains(uid))
                    like.setValue(true);
                else
                    like.setValue(false);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}
