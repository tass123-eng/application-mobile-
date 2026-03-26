package com.example.findhobbies.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.findhobbies.R;
import com.example.findhobbies.db.HobbyDatabase;
import com.example.findhobbies.entity.Event;
import com.example.findhobbies.entity.EventRegistration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private LinearLayout layoutMarkers; // Contiendra la liste des dates réservées avec ligne
    private TextView tvEvents; // Événements du jour sélectionné
    private int userId;
    private HobbyDatabase db;
    private List<EventRegistrationWithName> allReservations;
    private Set<String> eventDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarView);
        layoutMarkers = findViewById(R.id.layoutMarkers);
        tvEvents = findViewById(R.id.tvEvents);

        db = HobbyDatabase.getInstance(this);

        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadUserReservations();

        // Stocker toutes les dates des événements réservés
        eventDates = new HashSet<>();
        for (EventRegistrationWithName item : allReservations) {
            Event event = db.eventDao().getEventById(item.registration.getEventId());
            if (event != null && event.getDate() != null) {
                eventDates.add(event.getDate());
            }
        }

        // Afficher les dates réservées avec un marqueur et une ligne
        displayEventDates();

        // Afficher événements du jour sélectionné (au lancement, aujourd'hui)
        long todayMillis = calendarView.getDate();
        showEventsForDate(todayMillis);

        // Listener sur le calendrier
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String clickedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
            showEventsForDate(clickedDate);
        });
    }

    private void loadUserReservations() {
        List<EventRegistration> registrations = db.eventRegistrationDao().getRegistrationsForUser(userId);
        allReservations = new ArrayList<>();
        if (registrations != null) {
            for (EventRegistration reg : registrations) {
                Event event = db.eventDao().getEventById(reg.getEventId());
                String eventName = event != null ? event.getName() : "Événement";
                allReservations.add(new EventRegistrationWithName(reg, eventName));
            }
        }
    }

    private void displayEventDates() {
        layoutMarkers.removeAllViews();

        for (String date : eventDates) {
            // TextView pour la date
            TextView tv = new TextView(this);
            tv.setText("★ " + date); // ★ = symbole pour indiquer la date réservée
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(16f);
            tv.setPadding(0, 8, 0, 4);
            layoutMarkers.addView(tv);

            // Ligne horizontale pour meilleure visibilité
            View line = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 2
            );
            line.setLayoutParams(params);
            line.setBackgroundColor(Color.WHITE);
            layoutMarkers.addView(line);
        }
    }

    private void showEventsForDate(long dateMillis) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(dateMillis);
        showEventsForDate(dateStr);
    }

    private void showEventsForDate(String date) {
        List<String> eventsForDay = new ArrayList<>();
        for (EventRegistrationWithName item : allReservations) {
            Event event = db.eventDao().getEventById(item.registration.getEventId());
            if (event != null && date.equals(event.getDate())) {
                eventsForDay.add(event.getName());
            }
        }

        if (eventsForDay.isEmpty()) {
            tvEvents.setText("Aucun événement ce jour.");
        } else {
            tvEvents.setText("Événements du jour : " + String.join(", ", eventsForDay));
        }
    }
}
