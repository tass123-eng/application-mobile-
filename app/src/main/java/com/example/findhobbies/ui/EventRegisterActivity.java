package com.example.findhobbies.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.findhobbies.R;
import com.example.findhobbies.db.HobbyDatabase;
import com.example.findhobbies.entity.Event;
import com.example.findhobbies.entity.EventRegistration;
import com.example.findhobbies.entity.User;

public class EventRegisterActivity extends AppCompatActivity {

    public static final String EXTRA_EVENT = "extra_event";
    public static final String EXTRA_USER_ID = "extra_user_id";

    private TextView tvEventName;
    private EditText etFirstName, etLastName, etAge, etInterests;
    private Button btnSave;

    private HobbyDatabase db;
    private Event currentEvent;
    private User currentUser;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_register);

        tvEventName = findViewById(R.id.tvEventName);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etAge = findViewById(R.id.etAge);
        etInterests = findViewById(R.id.etInterests);
        btnSave = findViewById(R.id.btnSaveRegistration);

        db = HobbyDatabase.getInstance(this);

        // Récupérer event + userId depuis l'Intent
        currentEvent = (Event) getIntent().getSerializableExtra(EXTRA_EVENT);
        userId = getIntent().getIntExtra(EXTRA_USER_ID, -1);

        if (currentEvent == null || userId == -1) {
            Toast.makeText(this, "Erreur : événement ou utilisateur manquant", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        currentUser = db.userDao().getUserById(userId);
        if (currentUser == null) {
            Toast.makeText(this, "Utilisateur introuvable", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        tvEventName.setText(currentEvent.getName());


        String username = currentUser.getUsername();
        if (!TextUtils.isEmpty(username) && username.contains(" ")) {
            String[] parts = username.split("\\s+", 2);
            etFirstName.setText(parts[0]);
            etLastName.setText(parts.length > 1 ? parts[1] : "");
        } else {
            etFirstName.setText(username != null ? username : "");
            etLastName.setText("");
        }

        btnSave.setOnClickListener(v -> {
            String firstName = etFirstName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String ageStr = etAge.getText().toString().trim();
            String interests = etInterests.getText().toString().trim();

            Integer age = null;
            if (!ageStr.isEmpty()) {
                try {
                    age = Integer.parseInt(ageStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "L'âge doit être un nombre", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (firstName.isEmpty()) {
                Toast.makeText(this, "Veuillez saisir votre prénom", Toast.LENGTH_SHORT).show();
                return;
            }


            long now = System.currentTimeMillis();
            EventRegistration reg = new EventRegistration(
                    currentEvent.getId(),
                    currentUser.getId(),
                    firstName,
                    lastName,
                    age,
                    interests,
                    now
            );

            db.eventRegistrationDao().insert(reg);

            Toast.makeText(this, "Inscription enregistrée pour l'événement", Toast.LENGTH_SHORT).show();
            finish(); // retourne
        });
    }
}
