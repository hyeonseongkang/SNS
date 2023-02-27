package com.mirror.sns.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mirror.sns.R;
import com.mirror.sns.model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder>{

    List<Post> posts = new ArrayList<>();
    private onItemClickListener listener;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_post_item, parent, false);

        return  new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(PostAdapter.MyViewHolder holder, int position) {
        Post post = posts.get(position);

        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(post.getPostPhotoUri()))
                .into(holder.mainPhoto);
    }

    @Override
    public int getItemCount() { return posts == null ? 0 : posts.size(); }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView mainPhoto;

        public MyViewHolder(View itemView) {
            super(itemView);
            mainPhoto = itemView.findViewById(R.id.mainPhoto);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(posts.get(position));
                    }
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(Post post);
    }

    public void setOnItemClickListener(PostAdapter.onItemClickListener listener) { this.listener = listener; }

}
