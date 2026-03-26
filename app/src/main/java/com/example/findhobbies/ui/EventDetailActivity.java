package com.example.findhobbies.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.findhobbies.R;
import com.example.findhobbies.entity.Event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventDetailActivity extends AppCompatActivity {

    public static final String EXTRA_EVENT = "extra_event";

    private TextView tvEventName, tvEventLocation, tvEventDate, tvEventCategory,
            tvEventDescription, tvEventParticipants, tvEventPrice;
    private Button btnRegister, btnShowMap;
    private ImageView ivEventImage;
    private int userId;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail); // ton layout amélioré

        // Initialisation des vues
        tvEventName = findViewById(R.id.tvDetailEventName);
        tvEventLocation = findViewById(R.id.tvDetailEventLocation);
        tvEventDate = findViewById(R.id.tvDetailEventDate);
        tvEventCategory = findViewById(R.id.tvDetailEventCategory);
        tvEventDescription = findViewById(R.id.tvDetailEventDescription);
        tvEventParticipants = findViewById(R.id.tvDetailEventParticipants);
        tvEventPrice = findViewById(R.id.tvDetailEventPrice);
        ivEventImage = findViewById(R.id.ivDetailEventImage);

        btnRegister = findViewById(R.id.btnRegister);
        btnShowMap = findViewById(R.id.btnShowMap);

        // Récupération des données
        event = (Event) getIntent().getSerializableExtra(EXTRA_EVENT);
        userId = getIntent().getIntExtra("userId", -1);

        if (event == null || userId == -1) {
            Toast.makeText(this, "Événement ou utilisateur manquant", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Remplissage des vues avec les données
        tvEventName.setText(event.getName());
        tvEventLocation.setText(event.getLocation());
        tvEventDate.setText(formatDate(event.getDate()));
        tvEventCategory.setText(event.getCategory());
        tvEventDescription.setText(event.getDescription());

        // Champs statiques comme dans l'adapter
        tvEventParticipants.setText("50+ participants");
        tvEventPrice.setText("Gratuit");

        // Image selon type d'événement
        setEventImage(event.getName());

        // Bouton s'inscrire
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(EventDetailActivity.this, EventRegisterActivity.class);
            intent.putExtra(EventRegisterActivity.EXTRA_EVENT, event);
            intent.putExtra(EventRegisterActivity.EXTRA_USER_ID, userId);
            startActivity(intent);
        });

        // Bouton voir sur la map
        btnShowMap.setOnClickListener(v -> {
            String uri = "geo:" + event.getLatitude() + "," + event.getLongitude() + "?q=" +
                    event.getLatitude() + "," + event.getLongitude() + "(" + event.getName() + ")";
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Toast.makeText(EventDetailActivity.this, "Google Maps non installé", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Formater la date en "15 Déc"
    private String formatDate(String date) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            Date parsedDate = inputFormat.parse(date);
            return outputFormat.format(parsedDate);
        } catch (Exception e) {
            return date;
        }
    }

    // Image selon type d'événement
    private void setEventImage(String eventName) {
        int imageRes = R.drawable.ic_event;
        String name = eventName.toLowerCase();
        if (name.contains("concert") || name.contains("jazz")) imageRes = R.drawable.ic_music;
        else if (name.contains("marathon")) imageRes = R.drawable.ic_sport;
        else if (name.contains("exposition") || name.contains("art")) imageRes = R.drawable.ic_art;
        else if (name.contains("festival") || name.contains("cinéma")) imageRes = R.drawable.ic_movie;
        else if (name.contains("conférence")) imageRes = R.drawable.ic_education;

        ivEventImage.setImageResource(imageRes);
    }
}
