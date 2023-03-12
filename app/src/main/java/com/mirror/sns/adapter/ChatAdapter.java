package com.mirror.sns.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.mirror.sns.R;
import com.mirror.sns.model.Chat;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    List<Chat> chats = new ArrayList<>();
    private String myUid = new String();

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        // 채팅 보낸 사람에 따라 layout 나눔
        if (viewType == 1) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_my_chat_item, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_user_chat_item, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        // 내가 보낸 채팅이면 1 아니면 2
        if(chats.get(position).getUserUid().equals(myUid)) {
            return 1;
        } else {
            return 2;
        }
    }


    @Override
    public int getItemCount() {
        return chats == null ? 0 : chats.size();
    }

    public void setChats(List<Chat> chats, String myUid) {
        this.chats = chats;
        this.myUid = myUid;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
