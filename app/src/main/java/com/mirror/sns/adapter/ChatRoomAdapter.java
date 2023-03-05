package com.mirror.sns.adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mirror.sns.R;
import com.mirror.sns.model.ChatMetaData;
import com.mirror.sns.model.ChatRoom;
import com.mirror.sns.model.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.MyViewHolder> {

    List<ChatRoom> chatRooms = new ArrayList<>();
    List<User> users = new ArrayList<>();

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_chat_room_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ChatRoom chatRoom = chatRooms.get(position);
        User user = users.get(position);

        ChatMetaData chatMetaData = chatRoom.getChatMetaData();

        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(user.getPhotoUri()))
                .into(holder.userPhoto);

        holder.userNickName.setText(user.getNickName());

        if (chatMetaData != null) {
            holder.lastMessage.setText(chatRoom.getChatMetaData().getLastMessage());
            holder.lastMessageDate.setText(chatRoom.getChatMetaData().getLastMessageDate());
            holder.unReadChatCount.setText(chatRoom.getChatMetaData().getUnReadChatCount());

        }
    }

    @Override
    public int getItemCount() {
        Log.d("Adapter2", chatRooms == null ? "0" : chatRooms.size() + "");
        return chatRooms == null ? 0 : chatRooms.size();
    }

    public void setChatRooms(List<ChatRoom> chatRooms, List<User> users) {
        this.chatRooms = chatRooms;
        this.users = users;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userPhoto;
        TextView userNickName;
        TextView lastMessage;
        TextView lastMessageDate;
        TextView unReadChatCount;

        public MyViewHolder(View itemView) {
            super(itemView);

            userPhoto = itemView.findViewById(R.id.userPhoto);
            userNickName = itemView.findViewById(R.id.userNickName);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            lastMessageDate = itemView.findViewById(R.id.lastMessageDate);
            unReadChatCount = itemView.findViewById(R.id.unReadChatCount);
        }
    }

}
