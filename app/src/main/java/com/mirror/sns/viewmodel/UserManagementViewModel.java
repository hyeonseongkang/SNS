package com.mirror.sns.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mirror.sns.classes.User;
import com.mirror.sns.model.UserManagementRepository;

import java.util.List;

public class UserManagementViewModel extends AndroidViewModel {

    private UserManagementRepository repository;

    private LiveData<User> userLiveData;
    private LiveData<List<User>> userListLiveData;

    public UserManagementViewModel(Application application) {
        super(application);

        repository = new UserManagementRepository(application);
        userLiveData = repository.getUserLiveData();
        userListLiveData = repository.getUserListLiveData();
    }

    public LiveData<User> getUserLiveData() { return userLiveData; }

    public LiveData<List<User>> getUserListLiveData() { return userListLiveData; }

    public void getUserList() { repository.getUserList();}

    public void getUserInfo(String uid) {
        repository.getUserInfo(uid);
    }

    public void setUserInfo(String uid, User userInfo)  {
        repository.setUserInfo(uid, userInfo);
    }
}
