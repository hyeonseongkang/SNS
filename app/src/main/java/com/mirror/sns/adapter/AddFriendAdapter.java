package com.mirror.sns.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mirror.sns.R;
import com.mirror.sns.model.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.MyViewHolder>{

    List<User> users = new ArrayList<>();
    private onItemClickListener listener;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_add_friend_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User user = users.get(position);

        holder.userNickName.setText(user.getNickName());

        Glide.with(holder.itemView.getContext())
                .load(R.drawable.basic_profile_photo)
                .into(holder.userPhoto);

        if (user.getPhotoUri() != null && user.getPhotoUri().length() > 0) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(user.getPhotoUri()))
                    .into(holder.userPhoto);
        }
    }

    @Override
    public int getItemCount() { return users == null ? 0 : users.size(); }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView userPhoto;
        private TextView userNickName;
        private ImageButton addFriendButton;

        public MyViewHolder(View itemView) {
            super(itemView);

            userPhoto = itemView.findViewById(R.id.userPhoto);
            userNickName = itemView.findViewById(R.id.userNickName);
            addFriendButton = itemView.findViewById(R.id.addFriendButton);

            addFriendButton.setEnabled(true);
            addFriendButton.setClickable(true);
            addFriendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(users.get(position));
                    }
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(User user);
    }

    public void setOnItemClickListener(onItemClickListener listener) { this.listener = listener; }
}
