package com.mirror.sns.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mirror.sns.databinding.FragmentLikeBinding;
import com.mirror.sns.databinding.FragmentPostBinding;

public class LikeFragment extends Fragment {


    private FragmentLikeBinding likeBinding;

    public LikeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        likeBinding = FragmentLikeBinding.inflate(inflater, container, false);
        return likeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
