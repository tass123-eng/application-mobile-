package com.example.findhobbies.repository;

import android.content.Context;

import com.example.findhobbies.db.HobbyDatabase;
import com.example.findhobbies.dao.HobbyDao;
import com.example.findhobbies.entity.Hobby;

import java.util.List;

public class HobbyRepository {

    private HobbyDao hobbyDao;

    public HobbyRepository(Context context) {
        HobbyDatabase db = HobbyDatabase.getInstance(context);
        hobbyDao = db.hobbyDao();
    }

    public void insert(Hobby hobby) {
        hobbyDao.insert(hobby);
    }

    public void update(Hobby hobby) {
        hobbyDao.update(hobby);
    }

    public List<Hobby> getAll() {
        return hobbyDao.getAll();
    }

    public void insertAll(List<Hobby> hobbies) {
        hobbyDao.insertAll(hobbies);
    }

    // 🔹 Nouvelle méthode pour récupérer un hobby par son ID
    public Hobby getById(int id) {
        return hobbyDao.getById(id);
    }
}
