package com.mirror.sns.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mirror.sns.classes.Post;

import org.jetbrains.annotations.NotNull;

public class PostRepository {
    public static final String TAG = "PostRepository";

    private Application application;

    private DatabaseReference postsRef;

    public PostRepository(Application application) {
        this.application = application;
        postsRef = FirebaseDatabase.getInstance().getReference("posts");
    }

    public void createPost(Post post) {
        String userUid = post.getUserUid();
        String key = postsRef.getKey();
        post.setKey(key);

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
}
