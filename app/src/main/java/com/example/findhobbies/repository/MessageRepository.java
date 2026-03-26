package com.example.findhobbies.repository;

import android.content.Context;

import androidx.room.Room;

import com.example.findhobbies.db.HobbyDatabase;
import com.example.findhobbies.entity.Message;
import com.example.findhobbies.entity.User;
import com.example.findhobbies.dao.MessageDao;
import com.example.findhobbies.dao.UserDao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessageRepository {

    private MessageDao messageDao;
    private UserDao userDao;

    public MessageRepository(Context context) {
        HobbyDatabase db = HobbyDatabase.getInstance(context);
        messageDao = db.messageDao();
        userDao = db.userDao();
    }

    /** Envoyer un message */
    public void sendMessage(Message message) {
        messageDao.insert(message);
    }

    /** Récupérer tous les messages entre deux utilisateurs */
    public List<Message> getConversation(int userId1, int userId2) {
        return messageDao.getConversation(userId1, userId2);
    }

    /** Récupérer tous les utilisateurs avec qui userId a échangé des messages */
    public List<User> getUsersWithMessages(int userId) {
        List<Message> messages = messageDao.getMessagesOfUser(userId);
        Set<Integer> userIds = new HashSet<>();
        for (Message m : messages) {
            if (m.getSenderId() != userId) userIds.add(m.getSenderId());
            if (m.getReceiverId() != userId) userIds.add(m.getReceiverId());
        }

        List<User> users = new ArrayList<>();
        for (int id : userIds) {
            User u = userDao.getUserById(id);
            if (u != null) users.add(u);
        }
        return users;
    }
}
