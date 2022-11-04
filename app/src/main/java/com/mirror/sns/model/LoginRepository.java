package com.mirror.sns.model;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginRepository {

    private Application application;

    private DatabaseReference usersRef;

    public LoginRepository(Application application) {
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        this.application = application;
    }
}
