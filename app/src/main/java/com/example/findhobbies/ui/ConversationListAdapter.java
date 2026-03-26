package com.example.findhobbies.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findhobbies.R;
import com.example.findhobbies.entity.User;

import java.util.List;

public class ConversationListAdapter extends RecyclerView.Adapter<ConversationListAdapter.ViewHolder> {

    private List<User> users;
    private int currentUserId;
    private Context context;
    private OnUserClickListener listener;

    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    public ConversationListAdapter(Context context, List<User> users, int currentUserId, OnUserClickListener listener) {
        this.context = context;
        this.users = users;
        this.currentUserId = currentUserId;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.tvUsername.setText(user.getUsername());

        holder.itemView.setOnClickListener(v -> {
            listener.onUserClick(user);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
        }
    }
}
