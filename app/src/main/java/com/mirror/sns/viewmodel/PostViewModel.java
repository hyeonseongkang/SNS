package com.mirror.sns.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.mirror.sns.model.PostRepository;

public class PostViewModel extends AndroidViewModel {

    private PostRepository repository;

    public PostViewModel(Application application) {
        super(application);

        repository = new PostRepository(application);
    }
}
