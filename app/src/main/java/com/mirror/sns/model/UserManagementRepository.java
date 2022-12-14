package com.mirror.sns.model;

import android.app.Application;
import android.net.Uri;

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
import com.mirror.sns.classes.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UserManagementRepository {

    private Application application;

    private DatabaseReference usersRef;

    private MutableLiveData<User> userLiveData;
    private MutableLiveData<List<User>> userListLiveData;

    private MutableLiveData<List<User>> allFriends;
    private MutableLiveData<Boolean> addFriendCheck;

    private MutableLiveData<Boolean> updateValid;

    public UserManagementRepository(Application application) {
        this.application = application;
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        userLiveData = new MutableLiveData<>();
        userListLiveData = new MutableLiveData<>();
        updateValid = new MutableLiveData<>();

        allFriends = new MutableLiveData<>();
        addFriendCheck = new MutableLiveData<>();
    }

    public MutableLiveData<User> getUserLiveData() { return userLiveData; }

    public MutableLiveData<List<User>> getUserListLiveData() { return userListLiveData; }

    public MutableLiveData<Boolean> getUpdateValid() { return updateValid; }

    public MutableLiveData<List<User>> getAllFriends() {
        return allFriends;
    }

    public LiveData<Boolean> addFirendCheck() {
        return addFriendCheck;
    }

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

    public void getUserList() {
        List<User> userList = new ArrayList<>();
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    User user = snapshot1.getValue(User.class);
                    userList.add(user);
                }
                userListLiveData.setValue(userList);
              //  userList = userListLiveData.getValue();
              //  userList.add(user);
               // userListLiveData.setValue(userList);
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

    // user profile ????????????
    public void updateUserProfile(User user) {
        String uid = user.getUid();
        /*
        1. ??????????????? ????????? user??? getPhotoUri??? ?????? Firebase Store??? ????????? ?????? ????????????.
        2. ?????? ????????? ?????? ????????? ????????? ????????? uri??? ???????????? ??? ?????? uri ?????? ??????????????? ????????? user????????? photoUri??? ????????????.
        3. users Ref ?????? ????????? uid ????????? 2????????? ????????? user ????????? ????????????.
        4. ?????? ??????????????? ???????????? ????????? updateValid??? true??? ???????????? update??? ??????????????? view?????? ????????????.
         */
        StorageReference storage = FirebaseStorage.getInstance().getReference().child("profiles/" + uid + ".jpg");
        UploadTask uploadTask = storage.putFile(Uri.parse(user.getPhotoUri()));
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        user.setPhotoUri(uri.toString());
                        usersRef.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    updateValid.setValue(true);
                                } else {
                                    updateValid.setValue(false);
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    // ?????? ?????? ?????????
    public void addFriend(List<User> usersProfile, String uid, String userNickName) {
        for (User userProfile : usersProfile) {

            // user????????? ?????? ????????? ???????????? user??? ?????????
            if (userProfile.getNickName().equals(userNickName)) {

                // ????????? ?????? ?????? ????????? ?????? ??????
                usersRef.child(uid).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        boolean overlapCheck = true;
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            User profile = snapshot1.getValue(User.class);
                            if (profile.getNickName().equals(userNickName))
                                overlapCheck = false;
                        }

                        // ????????? ???????????? ?????? ??????
                        if (overlapCheck) {
                            usersRef.child(uid).child("friends").push().setValue(userProfile)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if (task.isSuccessful())
                                                addFriendCheck.setValue(true);
                                        }
                                    });
                        } else {
                            addFriendCheck.setValue(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        }
    }

    public void getUserPhoto(String uid) {
        usersRef.child(uid).child("photoUri").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String photoUri = snapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}
