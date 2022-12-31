package com.mirror.sns.adapter;

import android.net.Uri;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mirror.sns.R;
import com.mirror.sns.classes.Post;
import com.mirror.sns.classes.Sns;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SnsAdapter extends RecyclerView.Adapter<SnsAdapter.MyViewHolder>{

    List<Post> posts = new ArrayList<>();
    static public View.OnTouchListener onTouchListener;

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

        holder.userName.setText(currPost.getUserUid());
        holder.postContent.setText(currPost.getContent());

        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(currPost.getPostPhotoUri()))
                .into(holder.postPhoto);
    }

    @Override
    public int getItemCount() { return posts == null ? 0 : posts.size(); }

    public SnsAdapter(View.OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    public void setSnses(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView userPhoto;
        private TextView userName, postContent;
        private ImageView postPhoto;


        public MyViewHolder(View itemView) {
            super(itemView);

            userPhoto = itemView.findViewById(R.id.userPhoto);
            userName = itemView.findViewById(R.id.userName);
            postContent = itemView.findViewById(R.id.postContent);
            postPhoto = itemView.findViewById(R.id.postPhoto);

            itemView.setClickable(true);
            itemView.setEnabled(true);
            itemView.setOnTouchListener(onTouchListener);

        }
    }

}
