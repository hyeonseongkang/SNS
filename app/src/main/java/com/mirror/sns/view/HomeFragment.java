package com.mirror.sns.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mirror.sns.R;
import com.mirror.sns.adapter.ProfileAdapter;
import com.mirror.sns.adapter.SnsAdapter;
import com.mirror.sns.classes.Post;
import com.mirror.sns.classes.Profile;
import com.mirror.sns.classes.Sns;
import com.mirror.sns.classes.User;
import com.mirror.sns.databinding.FragmentHomeBinding;
import com.mirror.sns.viewmodel.LoginViewModel;
import com.mirror.sns.viewmodel.PostViewModel;
import com.mirror.sns.viewmodel.UserManagementViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";


    private FragmentHomeBinding homeBinding;

    private ProfileAdapter profileAdapter;
    private SnsAdapter snsAdapter;

    private LoginViewModel loginViewModel;
    private PostViewModel postViewModel;
    private UserManagementViewModel userManagementViewModel;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private String userName;
    private String userPhoto;
    private String userUid;
    private String userEmail;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        return homeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser.getPhotoUrl() == null) {
            Log.d(TAG, "photo Url is null");
            Glide.with(this)
                    .load(R.drawable.basic_profile_photo)
                    .into(homeBinding.userProfile);
        } else {
            Log.d(TAG, "photo Url is not null");
            Glide.with(this)
                    .load(firebaseUser.getPhotoUrl())
                    .into(homeBinding.userProfile);
        }

        homeBinding.myNickName.setText(firebaseUser.getEmail());

        userManagementViewModel = new ViewModelProvider(requireActivity()).get(UserManagementViewModel.class);
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        postViewModel = new ViewModelProvider(requireActivity()).get(PostViewModel.class);

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

        postViewModel.getPosts();


        homeBinding.friendRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        homeBinding.friendRecyclerview.setHasFixedSize(true);
        profileAdapter = new ProfileAdapter();
        homeBinding.friendRecyclerview.setAdapter(profileAdapter);

        homeBinding.snsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        homeBinding.snsRecyclerView.setHasFixedSize(true);
        snsAdapter = new SnsAdapter();
        homeBinding.snsRecyclerView.setAdapter(snsAdapter);

        postViewModel.getPostsLiveData().observe(getActivity(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                snsAdapter.setSnses(posts);
            }
        });

        /*
        friend profile test
         */

        List<Profile> profileList = new ArrayList<>();
        profileList.add(new Profile("", "", "user1" ,""));
        profileList.add(new Profile("", "", "user2" ,""));
        profileList.add(new Profile("", "", "user3" ,""));
        profileList.add(new Profile("", "", "user4" ,""));
        profileList.add(new Profile("", "", "user5" ,""));
        profileList.add(new Profile("", "", "user6" ,""));
        profileList.add(new Profile("", "", "user7" ,""));

        profileAdapter.setProfiles(profileList);

        profileAdapter.setOnItemClickListener(new ProfileAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Profile profile, int position) {

            }
        });

        snsAdapter.setOnItemClickListener(new SnsAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Post post, int position) {
                Intent intent = new Intent(getActivity(), DetailPostActivity.class);
                intent.putExtra("userUid", post.getUserUid());
                intent.putExtra("itemKey", post.getKey());
                startActivity(intent);
            }
        });

        homeBinding.createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreatePostActivity.class);
                startActivity(intent);
            }
        });

        homeBinding.dm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginViewModel.logout();
            }
        });
    }


}
