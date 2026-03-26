package com.example.findhobbies.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findhobbies.R;
import com.example.findhobbies.entity.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messages;
    private int currentUserId; // ID de l'utilisateur connecté

    public MessageAdapter(List<Message> messages, int currentUserId) {
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);

        holder.txtMessage.setText(message.getContent());

        // Formater le timestamp
        Date date = new Date(message.getTimestamp());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        holder.txtTime.setText(sdf.format(date));

        // Définir le fond selon l'expéditeur
        if (message.getSenderId() == currentUserId) {
            holder.txtMessage.setBackgroundResource(R.drawable.bg_message_sent);
        } else {
            holder.txtMessage.setBackgroundResource(R.drawable.bg_message_received);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage, txtTime;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtTime = itemView.findViewById(R.id.txtTime);
        }
    }
}
