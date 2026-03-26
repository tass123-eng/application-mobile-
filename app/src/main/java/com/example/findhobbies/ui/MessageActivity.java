package com.example.findhobbies.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.findhobbies.R;
import com.example.findhobbies.entity.Message;
import com.example.findhobbies.repository.MessageRepository;

import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText edtMessage;
    private Button btnSend;
    private MessageAdapter adapter;
    private MessageRepository repo;

    private int currentUserId;
    private int otherUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        recyclerView = findViewById(R.id.recyclerMessages);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);

        currentUserId = getIntent().getIntExtra("userId", -1);
        otherUserId = getIntent().getIntExtra("friendId", -1);

        repo = new MessageRepository(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadConversation();

        btnSend.setOnClickListener(v -> {
            String content = edtMessage.getText().toString().trim();
            if(!content.isEmpty()){
                Message msg = new Message(currentUserId, otherUserId, content, System.currentTimeMillis());
                repo.sendMessage(msg);
                edtMessage.setText("");
                loadConversation(); // actualiser la conversation
            }
        });
    }

    private void loadConversation(){
        List<Message> messages = repo.getConversation(currentUserId, otherUserId);
        adapter = new MessageAdapter(messages, currentUserId);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(messages.size() - 1);
    }
}
