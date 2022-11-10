package com.mirror.sns.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mirror.sns.R;
import com.mirror.sns.classes.User;
import com.mirror.sns.databinding.ActivityLoginBinding;
import com.mirror.sns.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    private ActivityLoginBinding loginBinding;
    private LoginViewModel loginViewModel;

    // Google Login
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        loginViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);

        // LoginRepository FirebaseUser 에 값이 들어오면 Main Activity 로 이동
        loginViewModel.getFirebaseUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
//                Log.d(TAG, "Success Firebase Login");
//                Log.d(TAG, "FirebaseUser UID: " + firebaseUser.getUid()) ;
//                Log.d(TAG, "FirebaseUser Email: " + firebaseUser.getEmail());

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
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
        loginViewModel.loginCheck();
    }
}