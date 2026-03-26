package com.example.findhobbies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findhobbies.R;
import com.example.findhobbies.db.HobbyDatabase;
import com.example.findhobbies.entity.Event;
import com.example.findhobbies.entity.EventRegistration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReservationsActivity extends AppCompatActivity {

    private RecyclerView recyclerReservations;
    private ReservationAdapter adapter;
    private int userId;
    private HobbyDatabase db;
    private List<EventRegistrationWithName> allReservations;

    private Button btnAll, btnPast, btnUpcoming, btnViewCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);

        // Initialisation des vues
        recyclerReservations = findViewById(R.id.recyclerReservations);
        recyclerReservations.setLayoutManager(new LinearLayoutManager(this));

        btnAll = findViewById(R.id.btnAll);
        btnPast = findViewById(R.id.btnPast);
        btnUpcoming = findViewById(R.id.btnUpcoming);
        btnViewCalendar = findViewById(R.id.btnViewCalendar);

        // Récupérer l'utilisateur connecté
        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db = HobbyDatabase.getInstance(this);
        loadUserReservations();

        // Clics des boutons
        btnAll.setOnClickListener(v -> filterReservations("all"));
        btnPast.setOnClickListener(v -> filterReservations("past"));
        btnUpcoming.setOnClickListener(v -> filterReservations("upcoming"));
        btnViewCalendar.setOnClickListener(v -> {
            Intent intent = new Intent(this, CalendarActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
    }

    // Charger toutes les réservations de l'utilisateur
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

        if (allReservations.isEmpty()) {
            Toast.makeText(this, "Aucune réservation trouvée", Toast.LENGTH_SHORT).show();
        }

        adapter = new ReservationAdapter(
                new ArrayList<>(allReservations),
                (item, position) -> {
                    // Annuler réservation
                    db.eventRegistrationDao().delete(item.registration);
                    allReservations.remove(item);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(this, "Réservation annulée", Toast.LENGTH_SHORT).show();
                },
                db
        );
        recyclerReservations.setAdapter(adapter);
    }

    // Filtrer les réservations
    private void filterReservations(String type) {
        List<EventRegistrationWithName> filtered = new ArrayList<>();
        long now = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (EventRegistrationWithName item : allReservations) {
            Event event = db.eventDao().getEventById(item.registration.getEventId());
            if (event == null || event.getDate() == null) continue;

            long eventTimestamp;
            try {
                eventTimestamp = sdf.parse(event.getDate()).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
                continue;
            }

            if ("all".equals(type)) {
                filtered.add(item);
            } else if ("past".equals(type) && eventTimestamp < now) {
                filtered.add(item);
            } else if ("upcoming".equals(type) && eventTimestamp >= now) {
                filtered.add(item);
            }
        }

        adapter = new ReservationAdapter(
                filtered,
                adapter.getCancelClickListener(),
                db
        );
        recyclerReservations.setAdapter(adapter);
    }
}
