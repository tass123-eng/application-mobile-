package com.example.findhobbies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findhobbies.R;
import com.example.findhobbies.entity.User;
import com.example.findhobbies.repository.MessageRepository;
import com.example.findhobbies.repository.UserRepository;

import java.util.List;

public class ConversationListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ConversationListAdapter adapter;

    private UserRepository userRepo;
    private MessageRepository messageRepo;

    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);

        recyclerView = findViewById(R.id.recyclerConversations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userRepo = new UserRepository(this);
        messageRepo = new MessageRepository(this);

        currentUserId = getIntent().getIntExtra("currentUserId", -1);
        if (currentUserId == -1) {
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadConversations();
    }

    private void loadConversations() {
        // Récupérer la liste des utilisateurs avec qui l'utilisateur courant a échangé des messages
        List<User> users = messageRepo.getUsersWithMessages(currentUserId);

        adapter = new ConversationListAdapter(this, users, currentUserId, user -> {
            Intent intent = new Intent(ConversationListActivity.this, MessageActivity.class);
            intent.putExtra("currentUserId", currentUserId);
            intent.putExtra("friendId", user.getId());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }
}
