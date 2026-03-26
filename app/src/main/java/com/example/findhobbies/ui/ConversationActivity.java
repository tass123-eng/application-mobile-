package com.example.findhobbies.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findhobbies.R;
import com.example.findhobbies.entity.Message;
import com.example.findhobbies.repository.MessageRepository;

import java.util.List;

public class ConversationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText edtMessage;
    private Button btnSend;
    private MessageAdapter adapter;

    private MessageRepository repo;

    private int currentUserId;
    private int friendId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        recyclerView = findViewById(R.id.recyclerMessages);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        repo = new MessageRepository(this);

        // Récupérer les IDs depuis l'intent
        currentUserId = getIntent().getIntExtra("currentUserId", -1);
        friendId = getIntent().getIntExtra("friendId", -1);

        if (currentUserId == -1 || friendId == -1) {
            Toast.makeText(this, "Utilisateur invalide", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadConversation();

        // Envoyer un message
        btnSend.setOnClickListener(v -> {
            String content = edtMessage.getText().toString().trim();
            if (!content.isEmpty()) {
                Message message = new Message(currentUserId, friendId, content, System.currentTimeMillis());
                repo.sendMessage(message);
                edtMessage.setText("");
                loadConversation();
            }
        });
    }

    private void loadConversation() {
        List<Message> messages = repo.getConversation(currentUserId, friendId);
        adapter = new MessageAdapter(messages, currentUserId);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(messages.size() - 1);
    }
}
