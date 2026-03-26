package com.example.findhobbies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findhobbies.R;
import com.example.findhobbies.entity.User;
import com.example.findhobbies.repository.UserHobbyRepository;
import com.example.findhobbies.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FriendAdapter adapter;
    private Button btnOpenMessages;

    private UserRepository userRepo;
    private UserHobbyRepository userHobbyRepo;

    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        recyclerView = findViewById(R.id.recyclerFriends);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnOpenMessages = findViewById(R.id.btnOpenMessages);

        userRepo = new UserRepository(this);
        userHobbyRepo = new UserHobbyRepository(this);

        currentUserId = getIntent().getIntExtra("userId", -1);
        if (currentUserId == -1) {
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadFriends();

        // 🔹 Ouvrir la liste des conversations
        btnOpenMessages.setOnClickListener(v -> {
            Intent intent = new Intent(FriendsActivity.this, ConversationListActivity.class);
            intent.putExtra("currentUserId", currentUserId);
            startActivity(intent);
        });
    }

    private void loadFriends() {
        List<Integer> myHobbyIds = userHobbyRepo.getHobbiesByUser(currentUserId);

        Set<User> potentialFriends = new HashSet<>();
        for (int hobbyId : myHobbyIds) {
            List<User> usersWithHobby = userHobbyRepo.getUsersByHobby(hobbyId);
            for (User u : usersWithHobby) {
                if (u.getId() != currentUserId) {
                    potentialFriends.add(u);
                }
            }
        }

        List<User> friendsList = new ArrayList<>(potentialFriends);

        adapter = new FriendAdapter(this, friendsList, currentUserId, userHobbyRepo,
                user -> {
                    // 🔹 Ouvrir MessageActivity pour un ami spécifique
                    Intent intent = new Intent(FriendsActivity.this, MessageActivity.class);
                    intent.putExtra("currentUserId", currentUserId);
                    intent.putExtra("friendId", user.getId());
                    startActivity(intent);
                }
        );
        recyclerView.setAdapter(adapter);
    }
}
