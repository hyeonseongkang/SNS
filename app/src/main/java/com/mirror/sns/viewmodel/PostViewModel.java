package com.mirror.sns.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mirror.sns.classes.Post;
import com.mirror.sns.model.PostRepository;

import java.util.List;

public class PostViewModel extends AndroidViewModel {

    private PostRepository repository;

    private LiveData<List<Post>> postsLiveData;

    private LiveData<Post> postLiveData;

    private MutableLiveData<Boolean> like;

    private MutableLiveData<Boolean> successCreatePost;

    public PostViewModel(Application application) {
        super(application);

        repository = new PostRepository(application);
        successCreatePost = repository.getSuccessCreatePost();
        postsLiveData = repository.getPostsLiveData();
        postLiveData = repository.getPostLiveData();
        like = repository.getLike();
    }

    public LiveData<List<Post>> getPostsLiveData() {
        return postsLiveData;
    }

    public LiveData<Post> getPostLiveData() { return postLiveData; }

    public MutableLiveData<Boolean> getSuccessCreatePost() { return successCreatePost;}

    public void createPost(Post post) {
        repository.createPost(post);
    }

    public void getPosts() { repository.getPosts();}

    public void getPost(String key) {repository.getPost(key);}

    public MutableLiveData<Boolean> getLike() { return like; }

    public void getLike(String key, String uid) { repository.getLike(key, uid);}

    public void setLike(String key, String uid) { repository.setLike(key, uid);}
}
