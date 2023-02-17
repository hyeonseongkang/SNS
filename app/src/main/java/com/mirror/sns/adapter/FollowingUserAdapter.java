package com.mirror.sns.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mirror.sns.R;
import com.mirror.sns.classes.FollowingUser;
import com.mirror.sns.classes.Profile;
import com.mirror.sns.classes.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowingUserAdapter extends RecyclerView.Adapter<FollowingUserAdapter.MyViewHolder> {

    public List<FollowingUser> followingUsers = new ArrayList<>();
    private onItemClickListener listener;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_friend_profile_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FollowingUser followingUser = followingUsers.get(position);

        holder.userNickName.setText(followingUser.getUser().getNickName());

        if (followingUser.getUser().getPhotoUri().length() > 0) {
            Glide.with(holder.itemView)
                    .load(Uri.parse(followingUser.getUser().getPhotoUri()))
                    .into(holder.userPhoto);
        }


    }

    @Override
    public int getItemCount() { return followingUsers == null ? 0 : followingUsers.size(); }

    public void setFollowingUsers(List<FollowingUser> followingUsers) {
        this.followingUsers = followingUsers;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView userPhoto;
        private TextView userNickName;

        public MyViewHolder(View itemView) {
            super(itemView);

            userPhoto = itemView.findViewById(R.id.userPhoto);
            userNickName = itemView.findViewById(R.id.userNickName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(followingUsers.get(position), position);
                    }
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(FollowingUser followingUser, int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) { this.listener = listener;}
}
