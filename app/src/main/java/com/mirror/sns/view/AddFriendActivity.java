package com.mirror.sns.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mirror.sns.R;
import com.mirror.sns.adapter.AddFriendAdapter;
import com.mirror.sns.model.User;
import com.mirror.sns.databinding.ActivityAddFriendBinding;
import com.mirror.sns.utils.RxAndroidUtils;
import com.mirror.sns.viewmodel.UserManagementViewModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

public class AddFriendActivity extends AppCompatActivity {

    private static final String TAG = "AddFriendActivity";
    private ActivityAddFriendBinding addFriendBinding;

    private UserManagementViewModel userManagementViewModel;

    private List<User> currUsers;

    private AddFriendAdapter addFriendAdapter;

    private Disposable editTextDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFriendBinding = ActivityAddFriendBinding.inflate(getLayoutInflater());
        setContentView(addFriendBinding.getRoot());
        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        addFriendAdapter = new AddFriendAdapter();
        addFriendBinding.friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addFriendBinding.friendsRecyclerView.setHasFixedSize(true);
        addFriendBinding.friendsRecyclerView.setAdapter(addFriendAdapter);


        addFriendBinding.friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addFriendBinding.friendsRecyclerView.setHasFixedSize(true);

        addFriendAdapter.setOnItemClickListener(new AddFriendAdapter.onItemClickListener() {
            @Override
            public void onItemClick(User user) {
                Intent intent = new Intent(AddFriendActivity.this, DetailUserActivity.class);
                intent.putExtra("userUid", user.getUid()) ;
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_left);
                //userManagementViewModel.friendRequest(user.getUid(), FirebaseAuth.getInstance().getUid());
            }
        });

        userManagementViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(UserManagementViewModel.class);
        userManagementViewModel.getUserList();
        userManagementViewModel.getUserListLiveData().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                currUsers = users;
            }
        });


        userManagementViewModel.getFindUser().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                addFriendAdapter.setUsers(users);
            }
        });

        addFriendBinding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText = addFriendBinding.input.getText().toString();

                userManagementViewModel.getFindUser(inputText);

            }
        });



        addFriendBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_left);
            }
        });

        Observable<String> editTextObservable = RxAndroidUtils.getInstance().getEditTextObservable(addFriendBinding.input);
        editTextDisposable = editTextObservable
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s-> {
                   Log.d(RxAndroidUtils.getInstance().getTag(), s);
                   if (s.length() >= 2) {
                       String inputText = addFriendBinding.input.getText().toString();
                       userManagementViewModel.getFindUser(inputText);
                   }
                   else if (s.length() == 1) {
                       Toast.makeText(this, "두 글자 이상 적어주세요.", Toast.LENGTH_SHORT).show();
                   }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        editTextDisposable.dispose();
    }
}