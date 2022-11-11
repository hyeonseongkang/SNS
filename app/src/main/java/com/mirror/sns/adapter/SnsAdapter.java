package com.mirror.sns.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.mirror.sns.R;
import com.mirror.sns.classes.Post;
import com.mirror.sns.classes.Sns;

import java.util.ArrayList;
import java.util.List;

public class SnsAdapter extends RecyclerView.Adapter<SnsAdapter.MyViewHolder>{

    List<Post> snsList = new ArrayList<>();
    private onItemClickListener listener;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_sns_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() { return snsList == null ? 0 : snsList.size(); }

    public void setSnses(List<Post> snsList) {
        this.snsList = snsList;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(snsList.get(position), position);
                    }
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(Post sns, int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) { this.listener = listener; }

}
