package com.example.findhobbies.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findhobbies.R;
import com.example.findhobbies.db.HobbyDatabase;
import com.example.findhobbies.entity.Event;

import java.util.List;

public class EventSearchActivity extends AppCompatActivity {

    private RecyclerView recyclerEvents;
    private EventAdapter adapter;
    private HobbyDatabase db;
    private EditText etSearchCity;
    private LinearLayout initialLayout;
    private int userId;

    private Button btnSport, btnMusic, btnTech, btnArt, btnSuggested;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_search);


        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        recyclerEvents = findViewById(R.id.recyclerEvents);
        recyclerEvents.setLayoutManager(new LinearLayoutManager(this));
        etSearchCity = findViewById(R.id.etSearchCity);
        initialLayout = findViewById(R.id.initialLayout);

        btnSport = findViewById(R.id.btnSport);
        btnMusic = findViewById(R.id.btnMusic);
        btnTech = findViewById(R.id.btnTech);
        btnArt = findViewById(R.id.btnArt);
        btnSuggested = findViewById(R.id.btnSuggested);

        db = HobbyDatabase.getInstance(this);


        List<Event> events = db.eventDao().getAllEvents();


        if (events.isEmpty()) {
            db.eventDao().insert(new Event(
                    "Concert Jazz",
                    "Sfax",
                    "2025-12-15",
                    "Musique",
                    34.7406,
                    10.7603,
                    "Un concert de jazz pour tous les amateurs."
            ));
            db.eventDao().insert(new Event(
                    "Marathon",
                    "Tunis",
                    "2025-12-20",
                    "Sport",
                    36.8065,
                    10.1815,
                    "Participez au marathon annuel, ouvert à tous niveaux."
            ));
            db.eventDao().insert(new Event(
                    "Exposition d'Art",
                    "Sousse",
                    "2025-12-18",
                    "Art",
                    35.8256,
                    10.6369,
                    "Découvrez des œuvres d'art locales et internationales."
            ));
            db.eventDao().insert(new Event(
                    "Festival Cinéma",
                    "Sfax",
                    "2025-12-25",
                    "Art",
                    34.7406,
                    10.7603,
                    "Un festival pour les passionnés de cinéma."
            ));
            db.eventDao().insert(new Event(
                    "Conférence Tech",
                    "Tunis",
                    "2026-01-05",
                    "Tech",
                    36.8065,
                    10.1815,
                    "Une conférence sur les dernières technologies et innovations."
            ));

            events = db.eventDao().getAllEvents();
        }

        adapter = new EventAdapter(events);
        recyclerEvents.setAdapter(adapter);


        adapter.setOnEventClickListener(new EventAdapter.OnEventClickListener() {
            @Override
            public void onEventClick(Event event) {
                Intent intent = new Intent(EventSearchActivity.this, EventDetailActivity.class);
                intent.putExtra(EventDetailActivity.EXTRA_EVENT, event);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }

            @Override
            public void onJoinEventClick(Event event) {
                Intent intent = new Intent(EventSearchActivity.this, EventRegisterActivity.class);
                intent.putExtra(EventRegisterActivity.EXTRA_EVENT, event);
                intent.putExtra(EventRegisterActivity.EXTRA_USER_ID, userId);
                startActivity(intent);
            }

            @Override
            public void onShowMapClick(Event event) {
                String uri = "geo:" + event.getLatitude() + "," + event.getLongitude() + "?q=" +
                        event.getLatitude() + "," + event.getLongitude() + "(" + event.getName() + ")";
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(EventSearchActivity.this, "Google Maps non installé", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Recherche par ville
        etSearchCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    initialLayout.setVisibility(LinearLayout.VISIBLE);
                    recyclerEvents.setVisibility(RecyclerView.GONE);
                } else {
                    List<Event> filteredEvents = db.eventDao().getEventsByLocation("%" + query + "%");
                    adapter.updateList(filteredEvents);
                    initialLayout.setVisibility(LinearLayout.GONE);
                    recyclerEvents.setVisibility(RecyclerView.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });


        View.OnClickListener categoryClickListener = v -> {
            String category = ((Button)v).getText().toString();
            List<Event> filteredEvents = db.eventDao().getEventsByCategory(category);
            adapter.updateList(filteredEvents);
            initialLayout.setVisibility(LinearLayout.GONE);
            recyclerEvents.setVisibility(RecyclerView.VISIBLE);
        };

        btnSport.setOnClickListener(categoryClickListener);
        btnMusic.setOnClickListener(categoryClickListener);
        btnTech.setOnClickListener(categoryClickListener);
        btnArt.setOnClickListener(categoryClickListener);


        btnSuggested.setOnClickListener(v -> {
            Intent intent = new Intent(EventSearchActivity.this, SuggestedEventsActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
    }
}
