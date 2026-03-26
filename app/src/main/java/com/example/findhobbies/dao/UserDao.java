package com.example.findhobbies.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.findhobbies.entity.User;

import java.util.List;

@Dao
public interface UserDao {


    @Insert
    void insert(User user);

    @Query("SELECT * FROM user WHERE email = :email AND password = :password LIMIT 1")
    User login(String email, String password);

    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);


    @Query("SELECT * FROM user WHERE id = :id LIMIT 1")
    User getUserById(int id);

    @Query("SELECT * FROM user")
    List<User> getAll();


    @Delete
    void delete(User user);
    @Update
    void updateUser(User user);
}
