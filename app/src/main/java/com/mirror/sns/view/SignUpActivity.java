package com.mirror.sns.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseUser;
import com.mirror.sns.R;
import com.mirror.sns.classes.User;
import com.mirror.sns.databinding.ActivitySignUpBinding;
import com.mirror.sns.viewmodel.LoginViewModel;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding signUpBinding;

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(signUpBinding.getRoot());

        loginViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);

        loginViewModel.getFirebaseUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                // success signUp
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        signUpBinding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = signUpBinding.userEmail.getText().toString();
                String password = signUpBinding.userPassword.getText().toString();

                loginViewModel.emailSignUp(new User(email, password));
            }
        });
    }
}