package com.example.findhobbies.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findhobbies.R;
import com.example.findhobbies.db.HobbyDatabase;
import com.example.findhobbies.entity.Event;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class SuggestedNearEventsActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private RecyclerView recyclerEvents;
    private EventAdapter adapter;
    private HobbyDatabase db;
    private static final int LOCATION_REQUEST_CODE = 102;
    private static final double MAX_DISTANCE_KM = 20.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_near_events);

        recyclerEvents = findViewById(R.id.recyclerEvents);
        recyclerEvents.setLayoutManager(new LinearLayoutManager(this));

        db = HobbyDatabase.getInstance(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        checkPermissionAndFetchEvents();
    }

    private void checkPermissionAndFetchEvents() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fetchNearbyEvents();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }
    }

    private void fetchNearbyEvents() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission de localisation non accordée", Toast.LENGTH_SHORT).show();
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                double userLat = location.getLatitude();
                double userLon = location.getLongitude();

                List<Event> allEvents = db.eventDao().getAllEvents();
                List<Event> nearbyEvents = new ArrayList<>();

                for (Event e : allEvents) {
                    double distance = distance(userLat, userLon, e.getLatitude(), e.getLongitude());
                    if (distance <= MAX_DISTANCE_KM) {
                        nearbyEvents.add(e);
                    }
                }

                if (!nearbyEvents.isEmpty()) {
                    adapter = new EventAdapter(nearbyEvents);
                    recyclerEvents.setAdapter(adapter);
                } else {
                    Toast.makeText(this, "Aucun événement proche trouvé", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Impossible de récupérer la localisation", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchNearbyEvents();
            } else {
                Toast.makeText(this, "Permission de localisation refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
