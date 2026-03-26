package com.example.findhobbies.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.findhobbies.dao.EventDao;
import com.example.findhobbies.dao.EventRegistrationDao;
import com.example.findhobbies.dao.HobbyDao;
import com.example.findhobbies.dao.MessageDao;
import com.example.findhobbies.dao.UserDao;
import com.example.findhobbies.dao.UserHobbyDao;
import com.example.findhobbies.entity.Event;
import com.example.findhobbies.entity.EventRegistration;
import com.example.findhobbies.entity.Hobby;
import com.example.findhobbies.entity.Message;
import com.example.findhobbies.entity.User;
import com.example.findhobbies.entity.UserHobby;

@Database(entities = {User.class, Hobby.class, UserHobby.class, Event.class, EventRegistration.class, Message.class}, version = 18)

public abstract class HobbyDatabase extends RoomDatabase {

    private static HobbyDatabase instance;

    public abstract EventDao eventDao();
    public abstract UserDao userDao();
    public abstract HobbyDao hobbyDao();
    public abstract UserHobbyDao userHobbyDao();
    public abstract EventRegistrationDao eventRegistrationDao();
    public abstract MessageDao messageDao();

    public static synchronized HobbyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            HobbyDatabase.class, "hobby_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
