package com.mirror.sns.view.fragment;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mirror.sns.adapter.ChatRoomAdapter;
import com.mirror.sns.databinding.FragmentChatBinding;
import com.mirror.sns.model.ChatRoom;
import com.mirror.sns.model.User;
import com.mirror.sns.view.ChatActivity;
import com.mirror.sns.viewmodel.ChatViewModel;
import com.mirror.sns.viewmodel.UserManagementViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    public static final String TAG = "ChatFragment";

    private FragmentChatBinding chatBinding;

    private UserManagementViewModel userManagementViewModel;
    private ChatViewModel chatViewModel;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private ChatRoomAdapter chatRoomAdapter;

    private List<ChatRoom> currChatRooms;


    public ChatFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        chatBinding = FragmentChatBinding.inflate(inflater, container, false);
        return chatBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        userManagementViewModel = new ViewModelProvider(requireActivity()).get(UserManagementViewModel.class);
        chatViewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);

        chatBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatBinding.recyclerView.setHasFixedSize(true);
        chatRoomAdapter = new ChatRoomAdapter();
        chatBinding.recyclerView.setAdapter(chatRoomAdapter);
        
        chatRoomAdapter.setOnItemClickListener(new ChatRoomAdapter.onItemClickListener() {
            @Override
            public void onItemClick(ChatRoom chatRoom, User user) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("chatRoomKey", chatRoom.getKey());
                intent.putExtra("userUid", user.getUid());
                startActivity(intent);

                Toast.makeText(getActivity(), "ClickEvent", Toast.LENGTH_SHORT).show();
            }
        });

        chatViewModel.getChatRoomListLiveData(firebaseUser.getUid());

        chatViewModel.getChatRoomListLiveData().observe(getActivity(), new Observer<List<ChatRoom>>() {
            @Override
            public void onChanged(List<ChatRoom> chatRooms) {
              //  Log.d(TAG, chatRooms.get(0).getUser1());
                List<String> userUids = new ArrayList<>();

                for (ChatRoom chatRoom: chatRooms) {
                    if (chatRoom.getUser1().equals(firebaseUser.getUid())) {
                        userUids.add(chatRoom.getUser2());
                    } else {
                        userUids.add(chatRoom.getUser1());
                    }
                }

                Log.d(TAG, chatRooms.size() + " ");
                currChatRooms = chatRooms;

                userManagementViewModel.getFriendList(userUids);
             //   chatRoomAdapter.setChatRooms(chatRooms);
            }
        });

        userManagementViewModel.getFriendListLiveData().observe(getActivity(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                for (User user: users) {
                    Log.d(TAG, "gdgd" + user.getUid());
                }

                Log.d(TAG, " " + users.size());
                chatRoomAdapter.setChatRooms(currChatRooms, users);
            }
        });


        userManagementViewModel.getUserInfo(firebaseUser.getUid());
    }

}
