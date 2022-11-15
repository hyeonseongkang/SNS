package com.mirror.sns.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.mirror.sns.adapter.PostAdapter;
import com.mirror.sns.classes.Sns;
import com.mirror.sns.classes.User;
import com.mirror.sns.databinding.FragmentMypageBinding;
import com.mirror.sns.viewmodel.UserManagementViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyPageFragment extends Fragment {

    private static final String TAG = "MyPageFragment";


    private FirebaseAuth firebaseAuth;

    private FragmentMypageBinding mypageBinding;
    private UserManagementViewModel userInfoViewModel;

    private PostAdapter postAdapter;

    public MyPageFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mypageBinding = FragmentMypageBinding.inflate(inflater, container, false);
        return mypageBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        userInfoViewModel = new ViewModelProvider(requireActivity()).get(UserManagementViewModel.class);

        userInfoViewModel.getUserInfo(firebaseAuth.getUid());
        userInfoViewModel.getUserLiveData().observe(getActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                // User (String uid, String email, String password, String nickName, String photoUri, String posts, String followers, String following)
                String uid = user.getUid();
                String email = user.getEmail();
                String password = user.getPassword();
                String nickName = user.getNickName();
                String photoUri = user.getPhotoUri();
                String posts = user.getPosts();
                String followers = user.getFollowers();
                String following = user.getFollowing();

                mypageBinding.userNickName.setText(nickName);
                mypageBinding.userEmail.setText(email);
                mypageBinding.userPosts.setText(posts);
                mypageBinding.userFollowers.setText(followers);
                mypageBinding.userFollowing.setText(following);
            }
        });

        mypageBinding.postsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mypageBinding.postsRecyclerView.setHasFixedSize(true);
        postAdapter = new PostAdapter();
        mypageBinding.postsRecyclerView.setAdapter(postAdapter);

        mypageBinding.editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        List<Sns> snsList = new ArrayList<>();
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        snsList.add(new Sns("", "", ""));
        postAdapter.setSnses(snsList);
    }


}
