package com.example.findhobbies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findhobbies.R;
import com.example.findhobbies.db.HobbyDatabase;
import com.example.findhobbies.entity.Event;
import com.example.findhobbies.entity.Hobby;
import com.example.findhobbies.repository.UserHobbyRepository;
import com.example.findhobbies.repository.HobbyRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SuggestedEventsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter adapter;

    private int userId;
    private HobbyDatabase db;
    private UserHobbyRepository userHobbyRepo;
    private HobbyRepository hobbyRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_events);

        recyclerView = findViewById(R.id.recyclerSuggestedEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = HobbyDatabase.getInstance(this);
        userHobbyRepo = new UserHobbyRepository(this);
        hobbyRepo = new HobbyRepository(this);

        userId = getIntent().getIntExtra("userId", -1);
        if(userId == -1){
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadSuggestedEvents();
    }

    private void loadSuggestedEvents() {
        List<Integer> hobbyIds = userHobbyRepo.getHobbiesByUser(userId);
        List<String> categories = new ArrayList<>();


        for(Integer id : hobbyIds){
            Hobby hobby = hobbyRepo.getById(id);
            if(hobby != null){
                categories.add(hobby.getName());
            }
        }


        Set<Event> suggestedEvents = new HashSet<>();
        for(String cat : categories){
            List<Event> events = db.eventDao().getEventsByCategory(cat);
            suggestedEvents.addAll(events);
        }

        adapter = new EventAdapter(new ArrayList<>(suggestedEvents));
        recyclerView.setAdapter(adapter);


        adapter.setOnEventClickListener(new EventAdapter.OnEventClickListener() {
            @Override
            public void onEventClick(Event event) {
                Intent intent = new Intent(SuggestedEventsActivity.this, EventDetailActivity.class);
                intent.putExtra(EventDetailActivity.EXTRA_EVENT, event);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }

            @Override
            public void onJoinEventClick(Event event) {
                Intent intent = new Intent(SuggestedEventsActivity.this, EventRegisterActivity.class);
                intent.putExtra(EventRegisterActivity.EXTRA_EVENT, event);
                intent.putExtra(EventRegisterActivity.EXTRA_USER_ID, userId);
                startActivity(intent);
            }

            @Override
            public void onShowMapClick(Event event) {

            }
        });
    }
}
