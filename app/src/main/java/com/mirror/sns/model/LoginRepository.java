package com.mirror.sns.model;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mirror.sns.R;
import com.mirror.sns.classes.User;
import com.mirror.sns.view.LoginActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class LoginRepository {

    public final static String TAG = "LoginRepository";
    
    private Application application;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private MutableLiveData<FirebaseUser> firebaseUser;
    private DatabaseReference usersRef;

    public LoginRepository(Application application) {
        this.application = application;
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = new MutableLiveData<>();
        usersRef = FirebaseDatabase.getInstance().getReference("users");
    }

    public MutableLiveData<FirebaseUser> getFirebaseUser() { return firebaseUser; }

    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            firebaseUser.setValue(user);
                            // User(String uid, String email, String password, String nickName, String photoUri, String posts, String followers, String following)
                            setUserInfo(user.getUid(), new User(mUser.getUid(), user.getEmail(), "", "", "", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    // email login
    public void emailLogin(User user) {
        String email = user.getEmail();
        String password = user.getPassword();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(application, "입력사항을 확인해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 이메일 로그인 요청
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            /*
                            로그인 성공
                             */
                            mUser = mAuth.getCurrentUser();
                            firebaseUser.setValue(mUser);
                            Log.d(TAG, "success email login");
                        } else {
                            // 로그인 실패
                            Log.d(TAG, "fail email login");
                        }
                    }
                });
    }

    // email signin
    public void emailSignUp(User user) {
        String email = user.getEmail();
        String password = user.getPassword();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(application, "입력사항을 확인해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 이메일 회원가입 요청
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 가입 성공
                            mUser = mAuth.getCurrentUser();
                            firebaseUser.setValue(mUser);
                            // User(String uid, String email, String password, String nickName, String photoUri, String posts, String followers, String following)
                            setUserInfo(mUser.getUid(), new User(mUser.getUid(), mUser.getEmail(), "", "", "", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
                            // String uid, String email, String password, String nickName, String photoUri
                            Log.d(TAG, "success email signIn");
                        } else {
                            // 가입 실패
                            Log.d(TAG, "fail email signIn");
                        }
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

    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    public void loginCheck() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            firebaseUser.setValue(currentUser);
        }
    }
}
