package com.example.findhobbies.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.findhobbies.entity.Event;
import java.util.List;

@Dao
public interface EventDao {
    @Insert
    void insert(Event event);

    @Query("SELECT * FROM events")
    List<Event> getAllEvents();

    // Recherche par localisation insensible à la casse
    @Query("SELECT * FROM events WHERE LOWER(location) LIKE LOWER(:location)")
    List<Event> getEventsByLocation(String location);

    @Query("SELECT * FROM events WHERE category = :category")
    List<Event> getEventsByCategory(String category);

    @Query("SELECT * FROM events WHERE id = :eventId LIMIT 1")
    Event getEventById(int eventId);
}
