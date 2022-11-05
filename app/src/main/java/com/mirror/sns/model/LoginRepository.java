package com.mirror.sns.model;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mirror.sns.classes.User;

import org.jetbrains.annotations.NotNull;

public class LoginRepository {

    public final static String TAG = "LoginRepository";
    
    private Application application;

    private DatabaseReference usersRef;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private MutableLiveData<FirebaseUser> firebaseUser;

    public LoginRepository(Application application) {
        this.application = application;
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = new MutableLiveData<>();
    }


    public MutableLiveData<FirebaseUser> getFirebaseUser() { return firebaseUser; }

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
                            Log.d(TAG, "로그인 성공");
                        } else {
                            // 로그인 실패
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
                            Log.d(TAG, "회원가입 성공");
                        } else {
                            // 가입 실패
                            Log.d(TAG, "회원가입 실패");
                        }
                    }
                });
    }
}
