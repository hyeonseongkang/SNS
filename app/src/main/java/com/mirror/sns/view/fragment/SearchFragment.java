package com.mirror.sns.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mirror.sns.adapter.PostAdapter;
import com.mirror.sns.adapter.TagAdapter;
import com.mirror.sns.model.User;
import com.mirror.sns.databinding.FragmentSearchBinding;
import com.mirror.sns.utils.RxAndroidUtils;
import com.mirror.sns.viewmodel.LoginViewModel;
import com.mirror.sns.viewmodel.PostViewModel;
import com.mirror.sns.viewmodel.UserManagementViewModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";


    private FragmentSearchBinding searchBinding;

    private LoginViewModel loginViewModel;
    private PostViewModel postViewModel;
    private UserManagementViewModel userManagementViewModel;

    private PostAdapter postAdapter;
    private TagAdapter tagAdapter;


    private Disposable editTextDisposable;

    public SearchFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        searchBinding = FragmentSearchBinding.inflate(inflater, container, false);
        return searchBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        postViewModel = new ViewModelProvider(requireActivity()).get(PostViewModel.class);
        userManagementViewModel = new ViewModelProvider(requireActivity()).get(UserManagementViewModel.class);

        userManagementViewModel.getUserList();
        userManagementViewModel.getUserListLiveData().observe(getActivity(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                for (User user: users) {
                    Log.d(TAG, user.getUid() + " " + user.getEmail());
                }
            }
        });

        searchBinding.postsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        searchBinding.postsRecyclerView.setHasFixedSize(true);
        postAdapter = new PostAdapter();
        searchBinding.postsRecyclerView.setAdapter(postAdapter);

        searchBinding.tagsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        searchBinding.tagsRecyclerView.setHasFixedSize(true);
        tagAdapter = new TagAdapter();
        searchBinding.tagsRecyclerView.setAdapter(tagAdapter);


        Observable<String> editTextObservable = RxAndroidUtils.getInstance().getEditTextObservable(searchBinding.search);
        editTextDisposable = editTextObservable
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s-> {
                    Log.d(RxAndroidUtils.getInstance().getTag(), s);
                    if (s.length() >= 2) {
                        String inputText = searchBinding.search.getText().toString();
                       // Log.d(TAG, inpuText);
                    }
                    else if (s.length() == 1) {
                        Toast.makeText(getActivity(), "두 글자 이상 적어주세요.", Toast.LENGTH_SHORT).show();
                    }
                });



    }


}
