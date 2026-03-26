package com.example.findhobbies.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.findhobbies.R;
import com.example.findhobbies.entity.User;
import com.example.findhobbies.repository.UserRepository;

public class SettingsActivity extends AppCompatActivity {

    private EditText edtUsername, edtEmail;
    private Button btnSave;

    private UserRepository userRepo;
    private User currentUser;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        btnSave = findViewById(R.id.btnSave);

        userRepo = new UserRepository(this);

        // Récupérer userId depuis intent
        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "Utilisateur non trouvé", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentUser = userRepo.getUserById(userId);
        if (currentUser == null) {
            Toast.makeText(this, "Utilisateur introuvable", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Remplir les champs avec les infos actuelles
        edtUsername.setText(currentUser.getUsername());
        edtEmail.setText(currentUser.getEmail());

        btnSave.setOnClickListener(v -> saveChanges());
    }

    private void saveChanges() {
        String newUsername = edtUsername.getText().toString().trim();
        String newEmail = edtEmail.getText().toString().trim();

        if (TextUtils.isEmpty(newUsername) || TextUtils.isEmpty(newEmail)) {
            Toast.makeText(this, "Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        currentUser.setUsername(newUsername);
        currentUser.setEmail(newEmail);

        userRepo.updateUser(currentUser);

        Toast.makeText(this, "Profil mis à jour avec succès", Toast.LENGTH_SHORT).show();
        finish();
    }
}
