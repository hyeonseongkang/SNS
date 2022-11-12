package com.mirror.sns.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mirror.sns.classes.User;
import com.mirror.sns.model.UserInfoRepository;

public class UserInfoViewModel extends AndroidViewModel {

    private UserInfoRepository repository;

    private LiveData<User> userLiveData;

    public UserInfoViewModel(Application application) {
        super(application);

        repository = new UserInfoRepository(application);
        userLiveData = repository.getUserLiveData();
    }

    public LiveData<User> getUserLiveData() { return userLiveData; }

    public void getUserInfo(String uid) {
        repository.getUserInfo(uid);
    }

    public void setUserInfo(String uid, User userInfo)  {
        repository.setUserInfo(uid, userInfo);
    }
}
