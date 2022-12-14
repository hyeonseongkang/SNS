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
import com.mirror.sns.classes.User;

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
    private MutableLiveData<List<String>> likePressUsers;

    public PostRepository(Application application) {
        this.application = application;
        postsRef = FirebaseDatabase.getInstance().getReference("posts");
        postsLiveData = new MutableLiveData<>();
        successCreatePost = new MutableLiveData<>();
        likePressUsers = new MutableLiveData<>();
        post = new MutableLiveData<>();
        posts = new ArrayList<>();
        like = new MutableLiveData<>();
    }

    public MutableLiveData<List<Post>> getPostsLiveData() {
        return postsLiveData;
    }

    public MutableLiveData<Post> getPostLiveData() {
        return post;
    }

    public MutableLiveData<Boolean> getLike() {
        return like;
    }

    public MutableLiveData<Boolean> getSuccessCreatePost() {
        return successCreatePost;
    }

    public MutableLiveData<List<String>> getLikePressUsers() {
        return likePressUsers;
    }

    public void createPost(Post post) {
        String userUid = post.getUserUid();
        String key = postsRef.push().getKey();
        String content = post.getContent();
        String userPhotoUri = post.getUserPhotoUri();
        ArrayList<User> likes = post.getLikes();
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
                                if (task.isSuccessful()) {
                                    successCreatePost.setValue(true);
                                }
                            }
                        });
                    }
                });

            }
        });


    }

    // key??? ???????????? post ?????? ????????????
    public void getPost(String userUid, String key) {
        postsRef.child(userUid).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String content = snapshot.child("content").getValue(String.class);
                String key = snapshot.child("key").getValue(String.class);
                String postPhotoUri = snapshot.child("postPhotoUri").getValue(String.class);
                String userPhotoUri = snapshot.child("userPhotoUri").getValue(String.class);
                String userUid = snapshot.child("userUid").getValue(String.class);

                ArrayList<User> likePressUsers = new ArrayList<>();
                for (DataSnapshot snapshot3: snapshot.child("likes").getChildren()) {
                    User user = snapshot3.getValue(User.class);
                    likePressUsers.add(user);
                }

                ArrayList<Tag> tagList = new ArrayList<>();
                for (DataSnapshot snapshot3: snapshot.child("tag").getChildren()) {
                    Tag tag = snapshot3.getValue(Tag.class);
                    tagList.add(tag);
                }
                //Post post = snapshot2.getValue(Post.class);
                // public Post(String key, String userUid, String content, String userPhotoUri, String postPhotoUri, ArrayList<Tag> tags, ArrayList<List<User>> likes) {
                Post currPost = new Post(key, userUid, content, userPhotoUri, postPhotoUri, tagList, likePressUsers);
                post.setValue(currPost);
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
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                        Log.d("post snapshot2 data ", snapshot2.getValue().toString());
                        String content = snapshot2.child("content").getValue(String.class);
                        String key = snapshot2.child("key").getValue(String.class);
                        String postPhotoUri = snapshot2.child("postPhotoUri").getValue(String.class);
                        String userPhotoUri = snapshot2.child("userPhotoUri").getValue(String.class);
                        String userUid = snapshot2.child("userUid").getValue(String.class);

                        ArrayList<User> likePressUsers = new ArrayList<>();
                        for (DataSnapshot snapshot3: snapshot2.child("likes").getChildren()) {
                            User user = snapshot3.getValue(User.class);
                            likePressUsers.add(user);
                        }

                        ArrayList<Tag> tagList = new ArrayList<>();
                        for (DataSnapshot snapshot3: snapshot2.child("tag").getChildren()) {
                            Tag tag = snapshot3.getValue(Tag.class);
                            tagList.add(tag);
                        }
                        //Post post = snapshot2.getValue(Post.class);
                        // public Post(String key, String userUid, String content, String userPhotoUri, String postPhotoUri, ArrayList<Tag> tags, ArrayList<List<User>> likes) {
                        Post post = new Post(key, userUid, content, userPhotoUri, postPhotoUri, tagList, likePressUsers);
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

/*
    // post ?????????
    public void setLike(String uid, String key, User setUser) {
        // key = item
        // uid = ????????? ?????? ?????? uid

        postsRef.child(uid).child(key).child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
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

                // ?????? ????????? ????????? ???????????? uid??? ????????? ???????????? uid??? ????????? ?????? -> toggle
                if (!(likes.contains(uid))) {
                    likes.add(uid);
                    like.setValue(true);
                } else if (likes.contains(uid)) {
                    likes.remove(uid);
                    like.setValue(false);
                }

                postsRef.child(uid).child(key).child("likes").setValue(likes);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    */

    // post ?????????
    public void setLike(String uid, String key, User setUser) {

        String likePressUser = setUser.getUid();

        postsRef.child(uid).child(key).child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<String> likes = new ArrayList<>();
                ArrayList<User> users = new ArrayList<>() ;
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.getValue() != null) {
                        Log.d(TAG, snapshot1.getValue().toString());
                        User user = snapshot1.getValue(User.class);
                        users.add(user);
                       // String userUid = snapshot1.getValue(String.class);
                        likes.add(user.getUid());
                    }
                }

                // ?????? ????????? ????????? ???????????? uid??? ????????? ???????????? uid??? ????????? ?????? -> toggle
                if (!(likes.contains(likePressUser))) {
                    users.add(setUser);
                    like.setValue(true);
                    postsRef.child(uid).child(key).child("likes").child(setUser.getUid()).setValue(setUser);
                } else if (likes.contains(uid)) {
                    users.remove(setUser);
                    like.setValue(false);
                    postsRef.child(uid).child(key).child("likes").child(setUser.getUid()).removeValue();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    // ????????? ????????????
    public void getLike(String key, String uid) {
        // ??????????????? ????????? uid??? key??? ???????????? ???????????? ???????????? ???????????? ????????? ????????? ????????? ???????????? ?????? ??????
        postsRef.child(uid).child(key).child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<String> likes = new ArrayList<>();
                ArrayList<User> users = new ArrayList<>() ;
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.getValue() != null) {
                      //  String userUid = snapshot1.getValue(String.class);
                        User user = snapshot1.getValue(User.class);
                        users.add(user);
                        likes.add(user.getUid());
                    }
                }

                // like list??? uid user??? ????????? true -> ????????? ?????? ??????
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

    public void getLikePressUsers(String key, String uid) {
        postsRef.child(uid).child(key).child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<String> likes = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.getValue() != null) {
                        // String userUid = snapshot1.getValue(String.class);
                        String userUid = snapshot1.getKey();
                        likes.add(userUid);
                    }
                }

                likePressUsers.setValue(likes);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void getUserPhoto(String uid) {

    }
}
