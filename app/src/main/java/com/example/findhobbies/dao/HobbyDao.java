package com.example.findhobbies.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.findhobbies.entity.Hobby;

import java.util.List;

@Dao
public interface HobbyDao {

    @Insert
    void insert(Hobby hobby);

    @Update
    void update(Hobby hobby);

    @Query("SELECT * FROM hobby")
    List<Hobby> getAll();

    @Query("DELETE FROM hobby")
    void deleteAll();
    @Insert
    void insertAll(List<Hobby> hobbies);
    @Query("SELECT * FROM Hobby WHERE id = :id LIMIT 1")
    Hobby getById(int id);
    @Query("SELECT name FROM Hobby WHERE id = :hobbyId LIMIT 1")
    String getHobbyNameById(int hobbyId);

}
