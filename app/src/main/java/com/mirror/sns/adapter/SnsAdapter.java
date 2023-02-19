package com.mirror.sns.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mirror.sns.R;
import com.mirror.sns.model.Post;
import com.mirror.sns.model.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SnsAdapter extends RecyclerView.Adapter<SnsAdapter.MyViewHolder>{

    List<Post> posts = new ArrayList<>();
    static public View.OnTouchListener onTouchListener;

    String userUid;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_sns_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.itemView.setTag(position);

        Post currPost = posts.get(position);

        List<User> likePressUsers = currPost.getLikes();

        Glide.with(holder.itemView.getContext())
                .load(R.drawable.basic_profile_photo)
                .into(holder.userPhoto1);

        Glide.with(holder.itemView.getContext())
                .load(R.drawable.basic_profile_photo)
                .into(holder.userPhoto2);

        Glide.with(holder.itemView.getContext())
                .load(R.drawable.basic_profile_photo)
                .into(holder.userPhoto3);


        for (int i = 0; i < likePressUsers.size(); i++) {
            if (i == 0) {
                Glide.with(holder.itemView.getContext())
                        .load(Uri.parse(likePressUsers.get(i).getPhotoUri()))
                        .into(holder.userPhoto1);
            } else if (i == 1) {
                Glide.with(holder.itemView.getContext())
                        .load(Uri.parse(likePressUsers.get(i).getPhotoUri()))
                        .into(holder.userPhoto2);
            } else if (i == 2) {
                Glide.with(holder.itemView.getContext())
                        .load(Uri.parse(likePressUsers.get(i).getPhotoUri()))
                        .into(holder.userPhoto3);
            }
        }


        Glide.with(holder.itemView.getContext())
                .load(R.drawable.basic_heart)
                .into(holder.like);

        Glide.with(holder.itemView.getContext())
                .load(R.drawable.basic_profile_photo)
                .into(holder.userPhoto);

        if (currPost.getUserPhotoUri() != null && currPost.getUserPhotoUri().length() > 0) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(currPost.getUserPhotoUri()))
                    .into(holder.userPhoto);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.basic_profile_photo)
                    .into(holder.userPhoto);
        }

        for (User user: likePressUsers) {
            if (user.getUid().equals(userUid)) {
                Glide.with(holder.itemView.getContext())
                        .load(R.drawable.red_heart)
                        .into(holder.like);
                break;
            }
        }

        holder.userName.setText(currPost.getUserUid());
        holder.postContent.setText(currPost.getContent());

        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(currPost.getPostPhotoUri()))
                .into(holder.postPhoto);
    }

    @Override
    public int getItemCount() { return posts == null ? 0 : posts.size(); }

    public SnsAdapter(View.OnTouchListener onTouchListener, String userUid) {
        this.onTouchListener = onTouchListener;
        this.userUid = userUid;
    }

    public void setSnses(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView userPhoto, userPhoto1, userPhoto2, userPhoto3;
        private TextView userName, postContent;
        private ImageView postPhoto, like;


        public MyViewHolder(View itemView) {
            super(itemView);

            userPhoto = itemView.findViewById(R.id.userPhoto);

            userPhoto1 = itemView.findViewById(R.id.userPhoto1);
            userPhoto2 = itemView.findViewById(R.id.userPhoto2);
            userPhoto3 = itemView.findViewById(R.id.userPhoto3);

            userName = itemView.findViewById(R.id.userName);
            postContent = itemView.findViewById(R.id.postContent);
            postPhoto = itemView.findViewById(R.id.postPhoto);
            like = itemView.findViewById(R.id.like);

            itemView.setClickable(true);
            itemView.setEnabled(true);
            itemView.setOnTouchListener(onTouchListener);

        }
    }

}
