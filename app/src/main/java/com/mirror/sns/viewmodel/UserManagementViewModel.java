package com.mirror.sns.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

    public UserManagementViewModel(Application application) {
        super(application);

        repository = new UserManagementRepository(application);
        userLiveData = repository.getUserLiveData();
        userListLiveData = repository.getUserListLiveData();
        updateValid = repository.getUpdateValid();
    }

    public LiveData<User> getUserLiveData() { return userLiveData; }

    public LiveData<List<User>> getUserListLiveData() { return userListLiveData; }

    public MutableLiveData<Boolean> getUpdateValid() { return updateValid; }

    public LiveData<List<User>> getAllFriends() { return allFriends; }

    public LiveData<Boolean> addFriendCheck() { return addFriendCheck; }

    public void getUserList() { repository.getUserList();}

    public void getUserInfo(String uid) {
        repository.getUserInfo(uid);
    }

    public void setUserInfo(String uid, User userInfo)  {
        repository.setUserInfo(uid, userInfo);
    }

    public void updateUserProfile(User user) { repository.updateUserProfile(user) ;}

    public void addFriend(List<User> usersProfile, String uid, String userNickName) { repository.addFriend(usersProfile, uid, userNickName);}


}
