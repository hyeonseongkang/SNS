package com.mirror.sns.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mirror.sns.R;
import com.mirror.sns.adapter.FollowingUserAdapter;
import com.mirror.sns.adapter.SnsAdapter;
import com.mirror.sns.model.FollowingUser;
import com.mirror.sns.model.UserLocation;
import com.mirror.sns.model.Post;
import com.mirror.sns.model.User;
import com.mirror.sns.databinding.FragmentHomeBinding;
import com.mirror.sns.view.AddFriendActivity;
import com.mirror.sns.view.CreatePostActivity;
import com.mirror.sns.view.DetailPostActivity;
import com.mirror.sns.view.DetailUserActivity;
import com.mirror.sns.viewmodel.LocationViewModel;
import com.mirror.sns.viewmodel.LoginViewModel;
import com.mirror.sns.viewmodel.PostViewModel;
import com.mirror.sns.viewmodel.UserManagementViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private FragmentHomeBinding homeBinding;

    private FollowingUserAdapter followingUserAdapter;
    private SnsAdapter snsAdapter;

    private LoginViewModel loginViewModel;
    private PostViewModel postViewModel;
    private UserManagementViewModel userManagementViewModel;
    private LocationViewModel locationViewModel;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private String userName;
    private String userPhoto;
    private String userUid;
    private String userEmail;

    private User currentUser;

    Context mContext;
    Activity mActivity;

    List<Post> currentPosts = new ArrayList<>();

    private UserLocation userLocation;


    public HomeFragment() {

    }


    @Override
    public void onAttach(Context context) {
        mContext = context;
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
        super.onAttach(context);
    }


    // detach에서는 변수 clearing 해주기 leak방지
    @Override
    public void onDetach() {
        mActivity = null;
        mContext = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        return homeBinding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        locationViewModel.setLocation(firebaseUser.getUid());
        locationViewModel.getLocation(firebaseUser.getUid());

        locationViewModel.getLocation().observe(getActivity(), new Observer<UserLocation>() {
            @Override
            public void onChanged(UserLocation userLocation) {
                HomeFragment.this.userLocation = userLocation;
                locationViewModel.getNearUsersUid(userLocation);
            }
        });

        locationViewModel.getNearUsersUid().observe(getActivity(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> nearUsersUid) {
                postViewModel.getPosts(nearUsersUid);
            }
        });

        userManagementViewModel = new ViewModelProvider(requireActivity()).get(UserManagementViewModel.class);
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        postViewModel = new ViewModelProvider(requireActivity()).get(PostViewModel.class);

        //loginViewModel.logout();
        userManagementViewModel.getUserInfo(firebaseUser.getUid());
        userManagementViewModel.getUserLiveData().observe((LifecycleOwner) getContext(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                // user data
                currentUser = user;
                userName = user.getNickName();
                userPhoto = user.getPhotoUri();
                userUid = user.getUid();
                userEmail = user.getEmail();

                homeBinding.myNickName.setText(userName);

                if (mActivity != null) {
                    if (userPhoto.length() <= 0 || userPhoto == null) {
                        Log.d(TAG, "photo Url is null");
                        Glide.with(mActivity)
                                .load(R.drawable.basic_profile_photo)
                                .into(homeBinding.userProfile);
                    } else {
                        Log.d(TAG, "photo Url is not null");
                        Glide.with(mActivity)
                                .load(Uri.parse(userPhoto))
                                .into(homeBinding.userProfile);
                    }
                }


            }
        });

        homeBinding.friendRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        homeBinding.friendRecyclerview.setHasFixedSize(true);
        followingUserAdapter = new FollowingUserAdapter();
        homeBinding.friendRecyclerview.setAdapter(followingUserAdapter);


        userManagementViewModel.getFollowingUsers(firebaseUser.getUid());
        userManagementViewModel.getFollowingUsers().observe((LifecycleOwner) getContext(), new Observer<List<FollowingUser>>() {
            @Override
            public void onChanged(List<FollowingUser> followingUsers) {
                if (followingUsers != null)
                    followingUserAdapter.setFollowingUsers(followingUsers);
            }
        });




        homeBinding.snsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        homeBinding.snsRecyclerView.setHasFixedSize(true);
        snsAdapter = new SnsAdapter(new View.OnTouchListener() {
            int position;
            GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    Post post = currentPosts.get(position);
                    Intent intent = new Intent(getActivity(), DetailPostActivity.class);
                    intent.putExtra("userUid", post.getUserUid());
                    intent.putExtra("itemKey", post.getKey());
                    startActivity(intent);
                    return super.onSingleTapConfirmed(e);
                }


                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    Post post = currentPosts.get(position);
                    postViewModel.setLike(post.getUserUid(), post.getKey(), currentUser);
                    return super.onDoubleTap(e);
                }
            });

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Object obj = view.getTag();

                if (obj != null) {
                    position = (int) obj;
                    gestureDetector.onTouchEvent(motionEvent);
                }

                return false;
            }
        }, firebaseUser.getUid());
        homeBinding.snsRecyclerView.setAdapter(snsAdapter);

        postViewModel.getPostsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                currentPosts = posts;
                snsAdapter.setSnses(posts);
            }
        });


        followingUserAdapter.setOnItemClickListener(new FollowingUserAdapter.onItemClickListener() {
            @Override
            public void onItemClick(FollowingUser followingUser, int position) {
                Intent intent = new Intent(getActivity(), DetailUserActivity.class);
                intent.putExtra("userUid", followingUser.getUser().getUid());
                startActivity(intent);
            }
        });

        homeBinding.addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddFriendActivity.class);
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
