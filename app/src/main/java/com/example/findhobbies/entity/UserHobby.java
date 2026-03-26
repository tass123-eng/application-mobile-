// entity/UserHobby.java
package com.example.findhobbies.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_hobby")
public class UserHobby {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private int hobbyId;

    public UserHobby(int userId, int hobbyId) {
        this.userId = userId;
        this.hobbyId = hobbyId;
    }

    // getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public int getHobbyId() { return hobbyId; }
}
