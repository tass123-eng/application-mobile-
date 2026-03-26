package com.example.findhobbies.repository;

import android.content.Context;

import com.example.findhobbies.dao.HobbyDao;
import com.example.findhobbies.dao.UserHobbyDao;
import com.example.findhobbies.db.HobbyDatabase;
import com.example.findhobbies.entity.User;
import com.example.findhobbies.entity.UserHobby;

import java.util.List;

public class UserHobbyRepository {

    private UserHobbyDao dao;
    private HobbyDao hobbyDao; // instance pour accéder aux hobbies

    public UserHobbyRepository(Context context) {
        HobbyDatabase db = HobbyDatabase.getInstance(context);
        dao = db.userHobbyDao();
        hobbyDao = db.hobbyDao(); // <-- créer l'instance de HobbyDao
    }

    /** Sauvegarder toute la liste des hobbies choisis */
    public void saveHobbiesForUser(int userId, List<Integer> hobbyIds) {
        dao.deleteByUser(userId);
        for (int id : hobbyIds) {
            dao.insert(new UserHobby(userId, id));
        }
    }

    /** Récupérer tous les hobbies du user */
    public List<Integer> getHobbiesByUser(int userId) {
        return dao.getHobbiesByUser(userId);
    }

    /** Supprimer un seul hobby */
    public void removeHobbyForUser(int userId, int hobbyId) {
        dao.deleteOne(userId, hobbyId);
    }

    /** Récupérer tous les utilisateurs ayant un hobby spécifique */
    public List<User> getUsersByHobby(int hobbyId) {
        return dao.getUsersByHobby(hobbyId);
    }

    /** Récupérer le nom d'un hobby par son id */
    public String getHobbyNameById(int hobbyId) {
        return hobbyDao.getHobbyNameById(hobbyId);
    }
}
