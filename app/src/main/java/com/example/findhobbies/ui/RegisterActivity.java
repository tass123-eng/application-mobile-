package com.example.findhobbies.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.findhobbies.R;
import com.example.findhobbies.db.HobbyDatabase;
import com.example.findhobbies.entity.User;

import org.mindrot.jbcrypt.BCrypt;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword;
    private Button btnRegister;
    private HobbyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        db = HobbyDatabase.getInstance(this);

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();


        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }


        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email non valide", Toast.LENGTH_SHORT).show();
            return;
        }


        User existUser = db.userDao().getUserByEmail(email);
        if (existUser != null) {
            Toast.makeText(this, "Email déjà utilisé", Toast.LENGTH_SHORT).show();
            return;
        }


        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());


        User newUser = new User(username, email, hashedPassword);
        db.userDao().insert(newUser);

        Toast.makeText(this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
        finish();
    }
}
