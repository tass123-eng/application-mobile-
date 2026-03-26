package com.example.findhobbies.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.findhobbies.entity.Message;

import java.util.List;

@Dao
public interface MessageDao {

    @Insert
    void insert(Message message);

    @Query("SELECT * FROM message WHERE (senderId = :user1 AND receiverId = :user2) OR (senderId = :user2 AND receiverId = :user1) ORDER BY timestamp ASC")
    List<Message> getConversation(int user1, int user2);


    @Query("SELECT * FROM message WHERE senderId = :userId OR receiverId = :userId ORDER BY timestamp ASC")
    List<Message> getMessagesOfUser(int userId);
}
