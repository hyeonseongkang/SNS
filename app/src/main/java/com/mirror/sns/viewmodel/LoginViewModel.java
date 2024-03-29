package com.mirror.sns.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;
import com.mirror.sns.model.User;
import com.mirror.sns.repository.LoginRepository;

public class LoginViewModel extends AndroidViewModel {

    private LoginRepository repository;
    private MutableLiveData<FirebaseUser> firebaseUser;

    public LoginViewModel(Application application) {
        super(application);
        repository = new LoginRepository(application);
        firebaseUser = repository.getFirebaseUser();
    }

    public LiveData<FirebaseUser> getFirebaseUser() { return firebaseUser;}

    public void emailLogin(User user) { repository.emailLogin(user); }

    public void emailSignUp(User user) { repository.emailSignUp(user); }

    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        repository.firebaseAuthWithGoogle(acct);
    }

    public void logout() { repository.logout(); }

    public void loginCheck() { repository.loginCheck(); }




}
