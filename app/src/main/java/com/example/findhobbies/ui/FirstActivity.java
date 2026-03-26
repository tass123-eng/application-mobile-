package com.example.findhobbies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.findhobbies.R;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class FirstActivity extends AppCompatActivity {

    Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        btnContinue = findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(v -> {
            // Naviguer vers HomeActivity
            Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
