package com.mirror.sns.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mirror.sns.classes.User;
import com.mirror.sns.model.LoginRepository;

import org.jetbrains.annotations.NotNull;

public class LoginViewModel extends AndroidViewModel {

    private LoginRepository repository;
    private MutableLiveData<FirebaseUser> firebaseUser;
    private MutableLiveData<Boolean> loginValid;

    public LoginViewModel(Application application) {
        super(application);
        repository = new LoginRepository(application);
        firebaseUser = repository.getFirebaseUser();
        loginValid = repository.getLoginValid();
    }

    public LiveData<FirebaseUser> getFirebaseUser() { return firebaseUser;}

    public LiveData<Boolean> getLoginValid() { return loginValid; }

    public void login(User user) { repository.login(user); }

    public void signUp(User user) { repository.signUp(user); }

    public void logout() { repository.logout(); }

    public void loginCheck() { repository.loginCheck(); }

}
