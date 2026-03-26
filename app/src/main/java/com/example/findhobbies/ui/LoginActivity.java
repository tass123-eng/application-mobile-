package com.example.findhobbies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.findhobbies.R;
import com.example.findhobbies.db.HobbyDatabase;
import com.example.findhobbies.entity.User;

import org.mindrot.jbcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvGoRegister;

    private HobbyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoRegister = findViewById(R.id.tvGoRegister);

        db = HobbyDatabase.getInstance(this);

        setupListeners();
    }

    private void setupListeners() {

        tvGoRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );


        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }


        User user = db.userDao().getUserByEmail(email);

        if (user == null) {
            Toast.makeText(this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
            return;
        }


        if (BCrypt.checkpw(password, user.getPassword())) {
            Toast.makeText(this, "Connexion réussie !", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("userId", user.getId());
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
        }
    }
}
