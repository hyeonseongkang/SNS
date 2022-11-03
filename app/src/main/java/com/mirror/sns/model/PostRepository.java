package com.mirror.sns.model;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostRepository {
    public static final String TAG = "PostRepository";

    private Application application;

    private DatabaseReference postsRef;

    public PostRepository(Application application) {
        this.application = application;
        postsRef = FirebaseDatabase.getInstance().getReference("posts");
    }
}
