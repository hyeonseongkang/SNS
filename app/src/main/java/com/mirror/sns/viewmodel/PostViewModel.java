package com.mirror.sns.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mirror.sns.classes.Post;
import com.mirror.sns.model.PostRepository;

import java.util.List;

public class PostViewModel extends AndroidViewModel {

    private PostRepository repository;

    private LiveData<List<Post>> postsLiveData;

    private LiveData<Post> postLiveData;

    public PostViewModel(Application application) {
        super(application);

        repository = new PostRepository(application);
        postsLiveData = repository.getPostsLiveData();
        postLiveData = repository.getPostLiveData();
    }

    public LiveData<List<Post>> getPostsLiveData() {
        return postsLiveData;
    }

    public LiveData<Post> getPostLiveData() { return postLiveData; }

    public void createPost(Post post) {
        repository.createPost(post);
    }

    public void getPosts() { repository.getPosts();}

    public void getPost(String key) {repository.getPost(key);}
}
