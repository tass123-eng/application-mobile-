package com.example.findhobbies.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.findhobbies.entity.User;
import com.example.findhobbies.entity.UserHobby;

import java.util.List;

@Dao
public interface UserHobbyDao {

    @Insert
    void insert(UserHobby userHobby);

    @Query("DELETE FROM user_hobby WHERE userId = :userId")
    void deleteByUser(int userId);

    @Query("DELETE FROM user_hobby WHERE userId = :userId AND hobbyId = :hobbyId")
    void deleteOne(int userId, int hobbyId);

    @Query("SELECT hobbyId FROM user_hobby WHERE userId = :userId")
    List<Integer> getHobbiesByUser(int userId);

    // Vérifie si un hobby est préféré par un user
    @Query("SELECT COUNT(*) FROM user_hobby WHERE userId = :userId AND hobbyId = :hobbyId")
    int exists(int userId, int hobbyId);
    @Query("SELECT u.* FROM user u " +
            "INNER JOIN user_hobby uh ON u.id = uh.userId " +
            "WHERE uh.hobbyId = :hobbyId")
    List<User> getUsersByHobby(int hobbyId);
}


