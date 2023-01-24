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
import com.mirror.sns.classes.Comment;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder>{

    List<Comment> comments = new ArrayList<>();
    private onItemClickListener listener;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_comment_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Comment comment = comments.get(position);

        holder.comment.setText(comment.getComment());
        holder.userName.setText(comment.getUser().getNickName());
        Glide.with(holder.itemView.getContext())
                .load(R.drawable.basic_profile_photo)
                .into(holder.userPhoto);

        Glide.with(holder.itemView.getContext())
                .load(R.drawable.basic_heart)
                .into(holder.likeButton);

        if (comment.getLike().length() > 0) {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.red_heart)
                    .into(holder.likeButton);
        }
        if (comment.getUser().getPhotoUri() != null && comment.getUser().getPhotoUri().length() > 0) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(comment.getUser().getPhotoUri()))
                    .into(holder.userPhoto);
        }
    }

    @Override
    public int getItemCount()
    {
        return comments == null ? 0 : comments.size();
    }

    public void setComments(List<Comment> comments ) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView userPhoto;
        private TextView comment, userName;
        private ImageView likeButton;

        public MyViewHolder(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userName);
            userPhoto = itemView.findViewById(R.id.userPhoto);
            comment = itemView.findViewById(R.id.comment);

            likeButton = itemView.findViewById(R.id.likeButton);
            likeButton.setEnabled(true);
            likeButton.setClickable(true);
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });

        }
    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

}
