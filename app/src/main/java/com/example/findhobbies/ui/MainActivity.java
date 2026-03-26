package com.example.findhobbies.ui;

import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.findhobbies.R;

public class MainActivity extends AppCompatActivity {

    private CardView cardHobbies, cardClubs, cardLocation, cardEvents, cardReservations, cardFriends;
    private LottieAnimationView logoAnimation;
    private ImageView profileIcon;
    private Button btnShowLocation;
    private boolean isFirstLoad = true;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        setupAnimations();
        setupClickListeners();
    }

    private void initializeViews() {
        cardHobbies = findViewById(R.id.cardHobbies);
        cardClubs = findViewById(R.id.cardClubs);
        cardLocation = findViewById(R.id.cardLocation);
        cardEvents = findViewById(R.id.cardEvents);
        cardReservations = findViewById(R.id.cardReservations);
        cardFriends = findViewById(R.id.cardFriends);
        logoAnimation = findViewById(R.id.logoAnimation);
        profileIcon = findViewById(R.id.btnProfile);
        btnShowLocation = findViewById(R.id.btnShowLocation);
    }

    private void setupAnimations() {
        logoAnimation.setAlpha(0f);
        logoAnimation.animate()
                .alpha(1f)
                .setDuration(1000)
                .setStartDelay(300)
                .start();

        if (isFirstLoad) {
            new Handler().postDelayed(() -> animateCardEntrance(cardHobbies), 500);
            new Handler().postDelayed(() -> animateCardEntrance(cardClubs), 700);
            new Handler().postDelayed(() -> animateCardEntrance(cardEvents), 900);
            new Handler().postDelayed(() -> animateCardEntrance(cardFriends), 1000); // Animation carte Amis
            new Handler().postDelayed(() -> animateCardEntrance(cardReservations), 1100);
            new Handler().postDelayed(() -> animateCardEntrance(cardLocation), 1300);
            isFirstLoad = false;
        }
    }

    private void animateCardEntrance(CardView card) {
        card.setAlpha(0f);
        card.setTranslationY(50f);

        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(card, "alpha", 0f, 1f);
        ObjectAnimator transYAnim = ObjectAnimator.ofFloat(card, "translationY", 50f, 0f);

        alphaAnim.setDuration(600);
        transYAnim.setDuration(600);
        transYAnim.setInterpolator(new AccelerateDecelerateInterpolator());

        alphaAnim.start();
        transYAnim.start();
    }

    private void setupClickListeners() {

        cardHobbies.setOnClickListener(v -> animateCardClick(cardHobbies, () -> {
            Intent intent = new Intent(MainActivity.this, HobbyActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        }));


        cardClubs.setOnClickListener(v -> animateCardClick(cardClubs, () -> {
            Intent intent = new Intent(MainActivity.this, SuggestedNearEventsActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        }));


        cardEvents.setOnClickListener(v -> animateCardClick(cardEvents, () -> {
            Intent intent = new Intent(MainActivity.this, EventSearchActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        }));


        cardFriends.setOnClickListener(v -> animateCardClick(cardFriends, () -> {
            Intent intent = new Intent(MainActivity.this, FriendsActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        }));



        cardReservations.setOnClickListener(v -> animateCardClick(cardReservations, () -> {
            Intent intent = new Intent(MainActivity.this, ReservationsActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        }));


        cardLocation.setOnClickListener(v -> animateCardClick(cardLocation, () -> {
            Toast.makeText(this, "Gestion de la localisation", Toast.LENGTH_SHORT).show();
        }));


        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });


        btnShowLocation.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
    }

    private void animateCardClick(CardView card, Runnable action) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(card, "scaleX", 0.95f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(card, "scaleY", 0.95f);

        scaleDownX.setDuration(100);
        scaleDownY.setDuration(100);

        scaleDownX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(card, "scaleX", 1f);
                ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(card, "scaleY", 1f);

                scaleUpX.setDuration(100);
                scaleUpY.setDuration(100);

                scaleUpX.start();
                scaleUpY.start();

                action.run();
            }
        });

        scaleDownX.start();
        scaleDownY.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirstLoad) {
            logoAnimation.resumeAnimation();
        }
    }
}
