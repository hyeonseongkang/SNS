package com.mirror.sns.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.mirror.sns.R;
import com.mirror.sns.classes.User;
import com.mirror.sns.databinding.ActivityLoginBinding;
import com.mirror.sns.viewmodel.LoginViewModel;
import com.mirror.sns.viewmodel.UserManagementViewModel;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    private ActivityLoginBinding loginBinding;
    private LoginViewModel loginViewModel;
    private UserManagementViewModel userInfoViewModel;
    // Google Login
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    private List<User> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        userList = new ArrayList<>();

        loginViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);
        userInfoViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(UserManagementViewModel.class);


        // LoginRepository FirebaseUser 에 값이 들어오면 Main Activity 로 이동
        loginViewModel.getFirebaseUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
//                Log.d(TAG, "Success Firebase Login");
//                Log.d(TAG, "FirebaseUser UID: " + firebaseUser.getUid()) ;
//                Log.d(TAG, "FirebaseUser Email: " + firebaseUser.getEmail());

                String uid = firebaseUser.getUid();
                String email = firebaseUser.getEmail();
//                boolean alreadyUser = false;
//                for (User user: userList) {
//                    if (user.getUid().equals(uid))
//                        alreadyUser = true;
//                }
//
//                if (!alreadyUser) {
//                    //  User(String uid, String email, String password, String nickName, String photoUri, String posts, String followers, String following) {
//                    User user = new User(uid, email, "", "", "", "", "" , "");
//                    userInfoViewModel.setUserInfo(uid, user);
//                    Log.d(TAG, "save user info");
//                }

               // userInfoViewModel.getUserList();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        userInfoViewModel.getUserListLiveData().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                userList = users;
                Log.d(TAG, "get Users");
                for (User user: users) {
                    Log.d(TAG, user.getUid());
                }
            }
        });




        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        loginBinding.loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });

        loginBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginBinding.userEmail.getText().toString();
                String password = loginBinding.userPassword.getText().toString();

                loginViewModel.emailLogin(new User(email, password));
            }
        });

        loginBinding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }



    private void googleSignIn() {
        Intent googleSignInIntent = mGoogleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(googleSignInIntent);
    }

    ActivityResultLauncher<Intent> googleSignInLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            loginViewModel.firebaseAuthWithGoogle(account);
                            //firebaseAuthWithGoogle(account);
                        } catch (ApiException e) {
                            Log.d("ERROR", e.toString());
                        }
                    }
                }
            });

    @Override
    public void onStart() {
        super.onStart();
        userInfoViewModel.getUserList();
        loginViewModel.loginCheck();
    }
}