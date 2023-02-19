package com.mirror.sns.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mirror.sns.databinding.FragmentChatBinding;
import com.mirror.sns.model.User;
import com.mirror.sns.viewmodel.UserManagementViewModel;

public class ChatFragment extends Fragment {


    private FragmentChatBinding chatBinding;

    private UserManagementViewModel userManagementViewModel;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private String userName;
    private String userPhoto;
    private String userUid;
    private String userEmail;

    public ChatFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        chatBinding = FragmentChatBinding.inflate(inflater, container, false);
        return chatBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        userManagementViewModel = new ViewModelProvider(requireActivity()).get(UserManagementViewModel.class);

        userManagementViewModel.getUserLiveData().observe(getActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                // user data
                userName = user.getNickName();
                userPhoto = user.getPhotoUri();
                userUid = user.getUid();
                userEmail = user.getEmail();
            }
        });
        userManagementViewModel.getUserInfo(firebaseUser.getUid());
    }

}
