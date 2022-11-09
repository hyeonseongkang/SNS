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

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class LoginRepository {

    public final static String TAG = "LoginRepository";
    
    private Application application;

    private DatabaseReference usersRef;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private MutableLiveData<FirebaseUser> firebaseUser;

    private MutableLiveData<Boolean> loginValid;


    public LoginRepository(Application application) {
        this.application = application;
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = new MutableLiveData<>();
        loginValid = new MutableLiveData<>();

    }

    public MutableLiveData<FirebaseUser> getFirebaseUser() { return firebaseUser; }

    public MutableLiveData<Boolean> getLoginValid() { return loginValid; }




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
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    public void login(User user) {
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
                            loginValid.setValue(true);
                            Log.d(TAG, "로그인 성공");
                        } else {
                            // 로그인 실패
                            loginValid.setValue(false);
                            Log.d(TAG, "로그인 실패");
                        }
                    }
                });
    }

    public void signUp(User user) {
        String email = user.getEmail();
        String password = user.getPassword();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(application, "입력사항을 확인해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        int index = email.indexOf("@");
        String emailCheck = email.substring(index);

        if (!(emailCheck.equals("@jbnu.ac.kr"))) {
            Toast.makeText(application, "전북대 메일로만 가입할 수 있습니다.", Toast.LENGTH_SHORT).show();
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
                            // String uid, String email, String password, String nickName, String photoUri
                            loginValid.setValue(true);
                            Log.d(TAG, "회원가입 성공");
                        } else {
                            // 가입 실패
                            Log.d(TAG, "회원가입 실패");
                            loginValid.setValue(false);
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
            loginValid.setValue(true);
        } else {
            loginValid.setValue(false);
        }
    }
}
