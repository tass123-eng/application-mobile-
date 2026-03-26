package com.example.findhobbies.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findhobbies.R;
import com.example.findhobbies.entity.User;
import com.example.findhobbies.repository.UserHobbyRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private List<User> friendsList;
    private int currentUserId;
    private Context context;
    private UserHobbyRepository userHobbyRepo;
    private OnMessageClickListener listener;

    public interface OnMessageClickListener {
        void onMessageClick(User user);
    }

    public FriendAdapter(Context context, List<User> friendsList, int currentUserId,
                         UserHobbyRepository repo, OnMessageClickListener listener) {
        this.friendsList = friendsList;
        this.currentUserId = currentUserId;
        this.context = context;
        this.userHobbyRepo = repo;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        User user = friendsList.get(position);
        holder.tvUsername.setText(user.getUsername());

        // 🔹 Récupérer le hobby commun
        int commonHobbyId = getCommonHobbyWithCurrentUser(user.getId());
        if (commonHobbyId != -1) {
            String hobbyName = userHobbyRepo.getHobbyNameById(commonHobbyId);
            holder.tvHobbyCommon.setText("Hobby commun : " + hobbyName);
        } else {
            holder.tvHobbyCommon.setText("Aucun hobby commun");
        }

        // 🔹 Clic sur le bouton Message
        holder.btnMessage.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMessageClick(user);
            }
        });

        // 🔹 Image de profil placeholder
        holder.imgUser.setImageResource(R.drawable.ic_profile);
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    // 🔹 Trouver un hobby commun
    private int getCommonHobbyWithCurrentUser(int otherUserId) {
        List<Integer> myHobbies = userHobbyRepo.getHobbiesByUser(currentUserId);
        List<Integer> otherHobbies = userHobbyRepo.getHobbiesByUser(otherUserId);

        Set<Integer> set = new HashSet<>(myHobbies);
        for (int h : otherHobbies) {
            if (set.contains(h)) {
                return h;
            }
        }
        return -1;
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        ImageView imgUser;
        TextView tvUsername, tvHobbyCommon;
        Button btnMessage;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.imgUser);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvHobbyCommon = itemView.findViewById(R.id.tvHobbyCommon);
            btnMessage = itemView.findViewById(R.id.btnMessage);
        }
    }
}
