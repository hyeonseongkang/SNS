package com.mirror.sns.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mirror.sns.classes.Comment;
import com.mirror.sns.classes.Post;
import com.mirror.sns.classes.User;
import com.mirror.sns.model.PostRepository;

import java.util.List;

public class PostViewModel extends AndroidViewModel {

    private PostRepository repository;

    private LiveData<List<Post>> postsLiveData;

    private LiveData<Post> postLiveData;

    private MutableLiveData<Boolean> like;

    private MutableLiveData<Boolean> successCreatePost;

    private MutableLiveData<List<String>> likePressUsers;

    private MutableLiveData<List<Comment>> comments;

    public PostViewModel(Application application) {
        super(application);

        repository = new PostRepository(application);
        successCreatePost = repository.getSuccessCreatePost();
        likePressUsers = repository.getLikePressUsers();
        postsLiveData = repository.getPostsLiveData();
        postLiveData = repository.getPostLiveData();
        like = repository.getLike();
        comments = repository.getComments();
    }

    public LiveData<List<Post>> getPostsLiveData() {
        return postsLiveData;
    }

    public LiveData<Post> getPostLiveData() { return postLiveData; }

    public MutableLiveData<Boolean> getSuccessCreatePost() { return successCreatePost;}

    public MutableLiveData<List<String>> getLikePressUsers() { return likePressUsers; }

    public MutableLiveData<List<Comment>> getComments() { return comments; }

    public void createPost(Post post) {
        repository.createPost(post);
    }

    public void getPosts() { repository.getPosts();}

    public void getPost(String userUid, String key) {repository.getPost(userUid, key);}

    public MutableLiveData<Boolean> getLike() { return like; }

    public void getLike(String key, String uid) { repository.getLike(key, uid);}

    public void setLike(String uid, String key, User setUser) { repository.setLike(uid, key, setUser);}

    public void getLikePressUsers(String key,  String uid) { repository.getLikePressUsers(key, uid);}

    public void setComment(String itemKey, Comment comment) { repository.setComment(itemKey, comment);}

    public void getComments(String itemKey) { repository.getComments(itemKey); }

    public void setCommentLike(String itemKey, String commentKey, String uid) { repository.setCommentLike(itemKey, commentKey, uid); }

}
