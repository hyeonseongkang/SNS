package com.mirror.sns.repository;

import android.annotation.SuppressLint;
import android.app.Application;
import android.net.Uri;
import android.text.TextUtils;
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
import com.mirror.sns.model.FollowerUser;
import com.mirror.sns.model.FollowingUser;
import com.mirror.sns.model.Post;
import com.mirror.sns.model.RequestFriend;
import com.mirror.sns.model.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UserManagementRepository {

    public static final String TAG = "UserManagementRepository";

    private Application application;

    private DatabaseReference usersRef;
    private DatabaseReference friendsRef;

    private MutableLiveData<User> userLiveData;
    private MutableLiveData<List<User>> userListLiveData;

    private MutableLiveData<List<User>> allFriends;
    private MutableLiveData<Boolean> addFriendCheck;

    private MutableLiveData<Boolean> updateValid;

    private MutableLiveData<List<User>> findUser;

    private MutableLiveData<Boolean> followRequest;
    private MutableLiveData<Boolean> unFollowRequest;

    private MutableLiveData<List<RequestFriend>> friends;

    private MutableLiveData<List<FollowingUser>> followingUsers;
    private MutableLiveData<List<FollowerUser>> followerUsers;

    private MutableLiveData<Boolean> followCheck;

    private MutableLiveData<List<User>> friendListLiveData;
    List<User> userList;

    public UserManagementRepository(Application application) {
        this.application = application;
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        userLiveData = new MutableLiveData<>();
        userListLiveData = new MutableLiveData<>();
        updateValid = new MutableLiveData<>();

        allFriends = new MutableLiveData<>();
        addFriendCheck = new MutableLiveData<>();
        findUser = new MutableLiveData<>();
        followRequest = new MutableLiveData<>();
        unFollowRequest = new MutableLiveData<>();
        friends = new MutableLiveData<>();

        followingUsers = new MutableLiveData<>();
        followerUsers = new MutableLiveData<>();

        followCheck = new MutableLiveData<>();

        friendListLiveData = new MutableLiveData<>();
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

    public MutableLiveData<List<User>> getFindUser() { return findUser; }

    public MutableLiveData<Boolean> getFollowRequest() { return followRequest; }

    public MutableLiveData<Boolean> getUnFollowRequest() { return unFollowRequest; }

    public MutableLiveData<List<RequestFriend>> getFriends() { return friends; }

    public MutableLiveData<List<FollowingUser>> getFollowingUsers() { return followingUsers; }

    public MutableLiveData<List<FollowerUser>> getFollowerUsers() { return followerUsers; }

    public MutableLiveData<Boolean> getFollowCheck() { return followCheck; }

    public MutableLiveData<List<User>> getFriendListLiveData() { return friendListLiveData; }

    public void getUserInfo(String uid) {
        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String uid = snapshot.child("uid").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String password = snapshot.child("password").getValue(String.class);
                String nickName = snapshot.child("nickName").getValue(String.class);
                String photoUri = snapshot.child("photoUri").getValue(String.class);

                ArrayList<Post> posts = new ArrayList<>();
                for (DataSnapshot snapshot1: snapshot.child("posts").getChildren()) {
                    Post post = snapshot1.getValue(Post.class);
                    posts.add(post);
                }

                ArrayList<FollowingUser> followingUsers = new ArrayList<>();
                for (DataSnapshot snapshot1: snapshot.child("following").getChildren()) {
                    FollowingUser followingUser = snapshot1.getValue(FollowingUser.class);
                    followingUsers.add(followingUser);
                }

                ArrayList<FollowerUser> followerUsers = new ArrayList<>();
                for (DataSnapshot snapshot1: snapshot.child("follower").getChildren()) {
                    FollowerUser followerUser = snapshot1.getValue(FollowerUser.class);
                    followerUsers.add(followerUser);
                }
                //  public User(String uid, String email, String password, String nickName, String photoUri, ArrayList<Post> posts, ArrayList<FollowerUser> followerUsers, ArrayList<FollowingUser> followingUsers) {
                User user = new User(uid, email, password, nickName, photoUri, posts, followerUsers, followingUsers);
                userLiveData.setValue(user);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void getUserList() {
        userList = new ArrayList<>();
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

    // user profile 업데이트
    public void updateUserProfile(User user) {
        String uid = user.getUid();
        /*
        1. 인자값으로 넘어온 user의 getPhotoUri를 통해 Firebase Store에 사진을 먼저 저장한다.
        2. 사진 저장이 완료 됐다면 저장된 사진의 uri를 다운받은 뒤 해당 uri 값을 인자값으로 넘어온 user객체의 photoUri에 할당한다.
        3. users Ref 아래 자신의 uid 아래에 2번까지 완료한 user 객체를 저장한다.
        4. 값이 성공적으로 업데이트 됐다면 updateValid에 true를 할당하여 update가 완료됐음을 view에서 확인한다.
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

    // 친구 추가 메서드
    public void addFriend(List<User> usersProfile, String uid, String userNickName) {
        for (User userProfile : usersProfile) {

            // user목록에 인자 값으로 넘어오는 user가 있다면
            if (userProfile.getNickName().equals(userNickName)) {

                // 친구가 이미 되어 있는지 중복 체크
                usersRef.child(uid).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        boolean overlapCheck = true;
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            User profile = snapshot1.getValue(User.class);
                            if (profile.getNickName().equals(userNickName))
                                overlapCheck = false;
                        }

                        // 중복이 아니라면 친구 추가
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

    @SuppressLint("LongLogTag")
    public void getFindUser(String userNickName) {

        if (TextUtils.isEmpty(userNickName))
            return;

        ArrayList<User> users = new ArrayList<>();
        for (User user: userList) {
            if (user.getNickName().equals(userNickName)) {
                // find user
                Log.d(TAG, user.getNickName());
                users.add(user);
            }
        }
        findUser.setValue(users);
    }

    public void getFriends(String uid) {
        usersRef.child(uid).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                List<RequestFriend> requestFriends = new ArrayList<>();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    RequestFriend requestFriend = snapshot1.getValue(RequestFriend.class);
                    requestFriends.add(requestFriend);
                }
                friends.setValue(requestFriends);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void getFollowCheck(String responseUserUid, String requestUserUid) {
        if (responseUserUid.equals(requestUserUid)) return;

        usersRef.child(responseUserUid).child("follower").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                boolean localFollowCheck = false;
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    FollowerUser followerUser = dataSnapshot.getValue(FollowerUser.class);

                    if (followerUser.getUser().getUid().equals(requestUserUid)) {
                        localFollowCheck = true;
                        break;
                    }
                }

                if (localFollowCheck) {
                    followCheck.setValue(true);
                } else {
                    followCheck.setValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void follow(User responseUser, User requestUser) {
        if (responseUser.getUid().equals(requestUser.getUid())) return;

        usersRef.child(responseUser.getUid()).child("follower").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                boolean overlapCheck = false;
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    FollowerUser followerUser = dataSnapshot.getValue(FollowerUser.class);

                    if (followerUser.getUser().getUid().equals(requestUser.getUid())) {
                        overlapCheck = true;
                        break;
                    }
                }

                if (overlapCheck) {
                    followRequest.setValue(false);
                    // getRequestFriend().setValue(false);
                    return;
                } else {
                    String key = usersRef.push().getKey();
                    usersRef.child(responseUser.getUid()).child("follower").child(key).setValue(new FollowerUser(key, requestUser)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                usersRef.child(requestUser.getUid()).child("following").child(key).setValue(new FollowerUser(key, responseUser)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            followRequest.setValue(true);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void unFollow(User responseUser, User requestUser) {
        if (responseUser.getUid().equals(requestUser.getUid())) return;

        usersRef.child(responseUser.getUid()).child("follower").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String key = null;
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    FollowerUser followerUser = dataSnapshot.getValue(FollowerUser.class);

                    if (followerUser.getUser().getUid().equals(requestUser.getUid())) {
                        key = followerUser.getKey();
                        break;
                    }
                }

                if (key == null) {
                    unFollowRequest.setValue(false);
                    return;
                } else {
                    usersRef.child(responseUser.getUid()).child("follower").child(key).removeValue();
                    usersRef.child(requestUser.getUid()).child("following").child(key).removeValue();
                    unFollowRequest.setValue(true);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void getFollowingUsers(String uid) {
        usersRef.child(uid).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<FollowingUser> tempFollowingUsers = new ArrayList<>();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    FollowingUser followingUser = snapshot1.getValue(FollowingUser.class);
                    tempFollowingUsers.add(followingUser);
                }
                followingUsers.setValue(tempFollowingUsers);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void getFollowerUsers(String uid) {
        usersRef.child(uid).child("follower").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<FollowerUser> tempFollowerUsers = new ArrayList<>();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    FollowerUser followerUser = snapshot1.getValue(FollowerUser.class);
                    tempFollowerUsers.add(followerUser);
                }
                followerUsers.setValue(tempFollowerUsers);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void getFriendList(List<String> userUids) {

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                List<User> users = new ArrayList<>();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    User user = snapshot1.getValue(User.class);

                    if (userUids.contains(user.getUid())) {
                        users.add(user);
                    }

                }
                friendListLiveData.setValue(users);
            }


            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

}
