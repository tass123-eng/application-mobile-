package com.example.findhobbies.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findhobbies.R;
import com.example.findhobbies.entity.Hobby;
import com.example.findhobbies.repository.HobbyRepository;
import com.example.findhobbies.repository.UserHobbyRepository;

import java.util.ArrayList;
import java.util.List;

public class HobbyActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HobbyAdapter adapter;
    private Button btnAdd;

    private HobbyRepository hobbyRepo;
    private UserHobbyRepository userHobbyRepo;

    private List<Hobby> userHobbies = new ArrayList<>();
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobby);

        recyclerView = findViewById(R.id.recyclerViewUserHobbies);
        btnAdd = findViewById(R.id.btnAddHobbies);

        hobbyRepo = new HobbyRepository(this);
        userHobbyRepo = new UserHobbyRepository(this);

        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeHobbiesIfNeeded();
        loadUserHobbies();

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Adapter avec listener pour supprimer un hobby de la liste
        adapter = new HobbyAdapter(userHobbies, hobby -> {
            userHobbies.remove(hobby);
            userHobbyRepo.removeHobbyForUser(userId, hobby.getId());
            adapter.notifyDataSetChanged();
        });

        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> showAddHobbiesDialog());
    }

    private void initializeHobbiesIfNeeded() {
        List<Hobby> all = hobbyRepo.getAll();
        if (all.isEmpty()) {
            hobbyRepo.insertAll(List.of(
                    new Hobby("Sport", "Activités sportives"),
                    new Hobby("Musique", "Instruments, chant, rythme"),
                    new Hobby("Lecture", "Livres, romans, bandes dessinées"),
                    new Hobby("Voyage", "Découverte et tourisme"),
                    new Hobby("Cuisine", "Préparation des repas, recettes"),
                    new Hobby("Dessin", "Art et créativité"),
                    new Hobby("Programmation", "Développement logiciel"),
                    new Hobby("Jeux Vidéo", "Gaming et e-sport"),
                    new Hobby("Fitness", "Entraînement physique"),
                    new Hobby("Photographie", "Capture d’images"),
                    new Hobby("Jardinage", "Plantes et extérieur"),
                    new Hobby("Cinéma", "Films et séries"),
                    new Hobby("Yoga", "Relaxation et méditation")
            ));
        }
    }

    private void loadUserHobbies() {
        userHobbies.clear();
        List<Integer> userHobbyIds = userHobbyRepo.getHobbiesByUser(userId);
        for (Hobby h : hobbyRepo.getAll()) {
            if (userHobbyIds.contains(h.getId())) {
                h.setSelected(true);
                userHobbies.add(h);
            }
        }
    }

    private void showAddHobbiesDialog() {
        List<Hobby> allHobbies = hobbyRepo.getAll();
        List<Integer> alreadySelected = userHobbyRepo.getHobbiesByUser(userId);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);

        List<CheckBox> checkBoxes = new ArrayList<>();

        for (Hobby hobby : allHobbies) {
            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setOrientation(LinearLayout.VERTICAL);
            itemLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            itemLayout.setPadding(8, 8, 8, 8);

            // Image
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(new LinearLayout.LayoutParams(150, 150));
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setImageResource(getImageResourceForHobby(hobby.getName()));
            itemLayout.addView(iv);

            // Nom du hobby
            TextView tv = new TextView(this);
            tv.setText(hobby.getName());
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(16f);
            tv.setTextColor(Color.BLACK);
            tv.setPadding(0, 8, 0, 4);
            itemLayout.addView(tv);

            // Checkbox
            CheckBox cb = new CheckBox(this);
            cb.setChecked(alreadySelected.contains(hobby.getId()));
            itemLayout.addView(cb);

            // Toggle checkbox en cliquant sur tout l'item
            itemLayout.setOnClickListener(v -> cb.setChecked(!cb.isChecked()));

            layout.addView(itemLayout);
            checkBoxes.add(cb);
        }

        new AlertDialog.Builder(this)
                .setTitle("Choisir vos hobbies")
                .setView(layout)
                .setPositiveButton("OK", (dialog, which) -> {
                    List<Integer> selectedIds = new ArrayList<>();
                    for (int i = 0; i < allHobbies.size(); i++) {
                        if (checkBoxes.get(i).isChecked()) {
                            selectedIds.add(allHobbies.get(i).getId());
                        }
                    }
                    userHobbyRepo.saveHobbiesForUser(userId, selectedIds);
                    loadUserHobbies();
                    adapter.notifyDataSetChanged();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private int getImageResourceForHobby(String name) {
        switch (name) {
            case "Musique": return R.drawable.music;
            case "Sport": return R.drawable.sport;
            case "Voyage": return R.drawable.voyage;
            case "Cuisine": return R.drawable.cooking;
            case "Dessin": return R.drawable.art;
            case "Programmation": return R.drawable.tech;
            case "Photographie": return R.drawable.photography;

            default: return R.drawable.default_ic;
        }
    }
}
