package com.mirror.sns.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.mirror.sns.model.LoginRepository;

public class LoginViewModel extends AndroidViewModel {

    private LoginRepository repository;

    public LoginViewModel(Application application) {
        super(application);

        repository = new LoginRepository(application);

    }
}
