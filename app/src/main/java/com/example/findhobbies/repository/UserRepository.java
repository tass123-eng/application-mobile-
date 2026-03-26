package com.example.findhobbies.repository;

import android.content.Context;
import com.example.findhobbies.dao.UserDao;
import com.example.findhobbies.db.HobbyDatabase;
import com.example.findhobbies.entity.User;
import java.util.List;

public class UserRepository {

    private final UserDao userDao;
    private final HobbyDatabase db;

    public UserRepository(Context context) {
        db = HobbyDatabase.getInstance(context);
        userDao = db.userDao();
    }

    public void insert(User user) {
        new Thread(() -> userDao.insert(user)).start();
    }

    public User login(String email, String password) {
        return userDao.login(email, password);
    }

    public boolean emailExists(String email) {
        return userDao.getUserByEmail(email) != null;
    }

    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    public List<User> getAllUsers() {
        return userDao.getAll();
    }

    public void deleteUser(User user) {
        new Thread(() -> userDao.delete(user)).start();
    }

    public void updateUser(User user) {
        new Thread(() -> userDao.updateUser(user)).start();
    }
}
