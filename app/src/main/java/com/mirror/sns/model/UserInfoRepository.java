package com.mirror.sns.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mirror.sns.classes.Post;
import com.mirror.sns.classes.User;

import org.jetbrains.annotations.NotNull;

public class UserInfoRepository {

    private Application application;

    private DatabaseReference usersRef;

    private MutableLiveData<User> userLiveData;

    public UserInfoRepository(Application application) {
        this.application = application;
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        userLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<User> getUserLiveData() { return userLiveData; }

    public void getUserInfo(String uid) {
        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                userLiveData.setValue(user);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void setUserInfo(String uid, User userInfo) {
        usersRef.child(uid).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // save success
                } else {
                    // save fail
                }
            }
        });
    }
}
