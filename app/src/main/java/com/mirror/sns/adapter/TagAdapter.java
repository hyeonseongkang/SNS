package com.mirror.sns.adapter;

import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mirror.sns.R;
import com.mirror.sns.classes.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.MyViewHolder>{

    List<Tag> tagList = new ArrayList<>();

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
    }

    @Override
    public int getItemCount() { return tagList == null ? 0 : tagList.size(); }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tagText;
        public MyViewHolder(View itemView) {
            super(itemView);
            tagText = itemView.findViewById(R.id.tagText);
            tagText.setPaintFlags(tagText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }
}
