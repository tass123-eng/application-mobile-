package com.example.findhobbies.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.findhobbies.entity.EventRegistration;

import java.util.List;

@Dao
public interface EventRegistrationDao {

    @Insert
    void insert(EventRegistration registration);

    @Query("SELECT * FROM event_registrations WHERE eventId = :eventId")
    List<EventRegistration> getRegistrationsForEvent(int eventId);

    @Query("SELECT * FROM event_registrations WHERE userId = :userId")
    List<EventRegistration> getRegistrationsForUser(int userId);

    @Delete
    void delete(EventRegistration registration);
}
