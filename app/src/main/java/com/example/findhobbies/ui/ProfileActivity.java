package com.example.findhobbies.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.findhobbies.R;
import com.example.findhobbies.db.HobbyDatabase;
import com.example.findhobbies.entity.User;
import com.example.findhobbies.repository.UserHobbyRepository;
import com.example.findhobbies.repository.UserRepository;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUsername, tvEmail, tvHobbiesCount, tvClubsCount, tvEventsCount;
    private CardView cardMyHobbies, cardSettings, cardLogout;
    private ImageView ivAvatar;

    private UserRepository userRepo;
    private UserHobbyRepository userHobbyRepo;
    private HobbyDatabase db;

    private int userId;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeViews();

        // Init DB + repo
        userRepo = new UserRepository(this);
        userHobbyRepo = new UserHobbyRepository(this);
        db = HobbyDatabase.getInstance(this);

        // Récupérer l'id de l'utilisateur connecté
        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentUser = userRepo.getUserById(userId);
        if (currentUser == null) {
            Toast.makeText(this, "Utilisateur introuvable", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        displayUserInfo();
        setupClickListeners();
        setupAnimations();
    }

    private void initializeViews() {
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvHobbiesCount = findViewById(R.id.tvHobbiesCount);
        tvClubsCount = findViewById(R.id.tvClubsCount);
        tvEventsCount = findViewById(R.id.tvEventsCount);

        cardMyHobbies = findViewById(R.id.cardMyHobbies);
        cardSettings = findViewById(R.id.cardSettings);
        cardLogout = findViewById(R.id.cardLogout);

        ivAvatar = findViewById(R.id.ivAvatar);
    }

    private void displayUserInfo() {
        tvUsername.setText(currentUser.getUsername());
        tvEmail.setText(currentUser.getEmail());
        displayStatistics();
    }

    private void displayStatistics() {
        int hobbiesCount = userHobbyRepo.getHobbiesByUser(userId).size();
        int eventsCount = db.eventRegistrationDao().getRegistrationsForUser(userId).size();
        int clubsCount = 0; // si tu veux ajouter club DAO plus tard

        animateCounter(tvHobbiesCount, hobbiesCount);
        animateCounter(tvClubsCount, clubsCount);
        animateCounter(tvEventsCount, eventsCount);
    }

    private void animateCounter(final TextView textView, int value) {
        ValueAnimator animator = ValueAnimator.ofInt(0, value);
        animator.setDuration(1200);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(a -> textView.setText(String.valueOf(a.getAnimatedValue())));
        animator.start();
    }

    private void setupClickListeners() {
        // Mes Hobbies
        cardMyHobbies.setOnClickListener(v ->
                animateCardClick(cardMyHobbies, () -> {
                    Intent intent = new Intent(ProfileActivity.this, HobbyActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                })
        );


        cardSettings.setOnClickListener(v ->
                animateCardClick(cardSettings, () -> {
                    Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                })
        );

        // Déconnexion
        cardLogout.setOnClickListener(v ->
                animateCardClick(cardLogout, this::showLogoutConfirmation)
        );
    }

    private void setupAnimations() {
        animateCardEntrance(cardMyHobbies, 0);
        animateCardEntrance(cardSettings, 1);
        animateCardEntrance(cardLogout, 2);
    }

    private void animateCardEntrance(CardView card, int delayIndex) {
        card.setAlpha(0f);
        card.setTranslationY(50f);

        ObjectAnimator alpha = ObjectAnimator.ofFloat(card, "alpha", 0f, 1f);
        ObjectAnimator transY = ObjectAnimator.ofFloat(card, "translationY", 50f, 0f);

        alpha.setDuration(600);
        transY.setDuration(600);
        alpha.setStartDelay(delayIndex * 150);
        transY.setStartDelay(delayIndex * 150);

        alpha.start();
        transY.start();
    }

    private void animateCardClick(CardView card, Runnable action) {
        ObjectAnimator sx = ObjectAnimator.ofFloat(card, "scaleX", 0.95f);
        ObjectAnimator sy = ObjectAnimator.ofFloat(card, "scaleY", 0.95f);
        sx.setDuration(100);
        sy.setDuration(100);

        sx.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ObjectAnimator sxUp = ObjectAnimator.ofFloat(card, "scaleX", 1f);
                ObjectAnimator syUp = ObjectAnimator.ofFloat(card, "scaleY", 1f);
                sxUp.setDuration(100);
                syUp.setDuration(100);

                sxUp.start();
                syUp.start();

                action.run();
            }
        });

        sx.start();
        sy.start();
    }

    private void showLogoutConfirmation() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Déconnexion")
                .setMessage("Êtes-vous sûr de vouloir vous déconnecter ?")
                .setPositiveButton("Déconnexion", (d, w) -> performLogout())
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void performLogout() {
        Toast.makeText(this, "Déconnexion réussie", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Réactualiser les données après modification
        currentUser = userRepo.getUserById(userId);
        displayUserInfo();
    }
}
