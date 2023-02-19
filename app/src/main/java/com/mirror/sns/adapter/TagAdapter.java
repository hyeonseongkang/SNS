package com.mirror.sns.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mirror.sns.R;
import com.mirror.sns.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.MyViewHolder>{

    List<Tag> tagList = new ArrayList<>();
    private onItemClickListener listener;
    boolean state;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_tag_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TagAdapter.MyViewHolder holder, int position) {
        Tag tag = tagList.get(position);
        holder.tagText.setText("#" + tag.getTag());

        holder.deleteTag.setVisibility(View.VISIBLE);
        if (state) {
            holder.deleteTag.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() { return tagList == null ? 0 : tagList.size(); }

    public void setTagList(List<Tag> tagList, boolean state) {
        this.tagList = tagList;
        this.state = state;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tagText;
        ImageView deleteTag;

        public MyViewHolder(View itemView) {
            super(itemView);
            tagText = itemView.findViewById(R.id.tagText);
            tagText.setPaintFlags(tagText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            deleteTag = itemView.findViewById(R.id.deleteTag);
            deleteTag.setOnClickListener(new View.OnClickListener() {
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

    public void setOnItemClickListener(onItemClickListener listener ) {this.listener= listener;}
}
