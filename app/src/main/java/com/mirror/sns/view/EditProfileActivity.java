package com.mirror.sns.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.mirror.sns.R;
import com.mirror.sns.classes.User;
import com.mirror.sns.databinding.ActivityEditProfileBinding;
import com.mirror.sns.model.UserManagementRepository;
import com.mirror.sns.viewmodel.LoginViewModel;
import com.mirror.sns.viewmodel.UserManagementViewModel;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding editProfileBinding;
    public static final String TAG = "EditProfileActivity";
    private UserManagementViewModel userManagementViewModel;

    private FirebaseAuth firebaseAuth;

    private String uid;
    private Uri tempPhotoUri;
    private String nickName;
    private String email;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editProfileBinding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(editProfileBinding.getRoot());

        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        firebaseAuth = FirebaseAuth.getInstance();

        // 카메라 권한
        if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1002);
            }
        }

        userManagementViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(UserManagementViewModel.class);
        userManagementViewModel.updateValid.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Log.d(TAG, "update success user profile");
                    setResult(RESULT_OK);
                    finish();
                    overridePendingTransition(R.anim.none, R.anim.fadeout_left);
                } else {
                    Log.d(TAG, "update fail user profile");
                }
            }
        });
        uid = firebaseAuth.getUid();
        tempPhotoUri = null;
        nickName = null;

        userManagementViewModel.getUserInfo(uid);
        userManagementViewModel.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (!(user.getPhotoUri().equals(""))) {
                    tempPhotoUri = Uri.parse(user.getPhotoUri());
                    Glide.with(EditProfileActivity.this)
                            .load(tempPhotoUri)
                            .into(editProfileBinding.userPhoto);
                }

                if (!(user.getNickName().equals(""))) {
                    nickName = user.getNickName();
                    editProfileBinding.userNickName.setText(user.getNickName());
                }

                currentUser = user;
                email = user.getEmail();
                editProfileBinding.userEmail.setText(user.getEmail());
                editProfileBinding.progress.setVisibility(View.GONE);
            }
        });

        editProfileBinding.userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                getPhotoLauncher.launch(intent);
            }
        });

        editProfileBinding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nickName = editProfileBinding.userNickName.getText().toString();

                if (TextUtils.isEmpty(nickName))
                    return;

                // Update
                //   public User(String uid, String email, String password, String nickName, String photoUri, String posts, String followers, String following) {
                userManagementViewModel.updateUserProfile(new User(uid, email, "null", nickName, tempPhotoUri.toString(), currentUser.getPosts(), currentUser.getFollowers(), currentUser.getFollowing()));

            }
        });


        editProfileBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_left);
            }
        });

    }

    ActivityResultLauncher<Intent> getPhotoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        tempPhotoUri = intent.getData();

                        if (tempPhotoUri != null) {
                            Glide.with(EditProfileActivity.this)
                                    .load(tempPhotoUri)
                                    .into(editProfileBinding.userPhoto);
                        }
                    }
                }
            });
}