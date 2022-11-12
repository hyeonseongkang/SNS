package com.mirror.sns.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.mirror.sns.adapter.PostAdapter;
import com.mirror.sns.classes.Sns;
import com.mirror.sns.databinding.FragmentHomeBinding;
import com.mirror.sns.databinding.FragmentMypageBinding;

import java.util.ArrayList;
import java.util.List;

public class MyPageFragment extends Fragment {

    private static final String TAG = "MyPageFragment";


    private FirebaseAuth firebaseAuth;

    private FragmentMypageBinding mypageBinding;

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

        mypageBinding.postsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mypageBinding.postsRecyclerView.setHasFixedSize(true);
        postAdapter = new PostAdapter();
        mypageBinding.postsRecyclerView.setAdapter(postAdapter);

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
