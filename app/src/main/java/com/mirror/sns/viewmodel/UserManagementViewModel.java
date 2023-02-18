package com.mirror.sns.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mirror.sns.classes.FollowerUser;
import com.mirror.sns.classes.FollowingUser;
import com.mirror.sns.classes.RequestFriend;
import com.mirror.sns.classes.User;
import com.mirror.sns.model.UserManagementRepository;

import java.util.List;

public class UserManagementViewModel extends AndroidViewModel {

    private UserManagementRepository repository;

    private LiveData<User> userLiveData;
    private LiveData<List<User>> userListLiveData;

    public MutableLiveData<Boolean> updateValid;

    private LiveData<List<User>> allFriends;

    private LiveData<Boolean> addFriendCheck;

    private LiveData<List<User>> findUser;

    private LiveData<Boolean> followRequest;
    private LiveData<Boolean> unFollowRequest;

    private LiveData<List<RequestFriend>> friends;

    private LiveData<List<FollowingUser>> followingUsers;

    private LiveData<List<FollowerUser>> followerUsers;

    private LiveData<Boolean> followCheck;

    public UserManagementViewModel(Application application) {
        super(application);

        repository = new UserManagementRepository(application);
        userLiveData = repository.getUserLiveData();
        userListLiveData = repository.getUserListLiveData();
        updateValid = repository.getUpdateValid();
        findUser = repository.getFindUser();
        followRequest = repository.getFollowRequest();
        unFollowRequest = repository.getUnFollowRequest();
        friends = repository.getFriends();
        followingUsers = repository.getFollowingUsers();
        followerUsers = repository.getFollowerUsers();
        followCheck = repository.getFollowCheck();
    }

    public LiveData<User> getUserLiveData() { return userLiveData; }

    public LiveData<List<User>> getUserListLiveData() { return userListLiveData; }

    public MutableLiveData<Boolean> getUpdateValid() { return updateValid; }

    public LiveData<List<User>> getAllFriends() { return allFriends; }

    public LiveData<Boolean> addFriendCheck() { return addFriendCheck; }

    public LiveData<List<User>> getFindUser() { return findUser; }

    public LiveData<Boolean> getFollowRequest() { return followRequest; }

    public LiveData<Boolean> getUnFollowRequest() { return unFollowRequest; }

    public LiveData<List<RequestFriend>> getFriends() { return friends; }

    public LiveData<List<FollowingUser>> getFollowingUsers() { return followingUsers; }

    public LiveData<List<FollowerUser>> getFollowerUsers() { return followerUsers; }

    public LiveData<Boolean> getFollowCheck() { return followCheck; }

    public void getUserList() { repository.getUserList();}

    public void getUserInfo(String uid) {
        repository.getUserInfo(uid);
    }

    public void setUserInfo(String uid, User userInfo)  {
        repository.setUserInfo(uid, userInfo);
    }

    public void updateUserProfile(User user) { repository.updateUserProfile(user) ;}

    public void addFriend(List<User> usersProfile, String uid, String userNickName) { repository.addFriend(usersProfile, uid, userNickName);}

    public void getFindUser(String userNickName) { repository.getFindUser(userNickName);}

    public void getFriends(String uid) {
        repository.getFriends(uid);
    }

    public void follow(User responseUser, User requestUser) {
        repository.follow(responseUser, requestUser);
    }

    public void unFollow(User responseUser, User requestUser) {
        repository.unFollow(responseUser, requestUser);
    }

    public void getFollowingUsers(String uid) {
        repository.getFollowingUsers(uid);
    }

    public void getFollowerUsers(String uid) { repository.getFollowerUsers(uid); }

    public void getFollowCheck(String responseUserUid, String requestUserUid) {
        repository.getFollowCheck(responseUserUid, requestUserUid);
    }
}
