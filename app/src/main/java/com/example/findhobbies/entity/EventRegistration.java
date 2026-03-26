package com.example.findhobbies.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "event_registrations")
public class EventRegistration {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int eventId;
    private int userId;


    private String firstName;
    private String lastName;
    private Integer age;
    private String interests;
    private long createdAt;

    public EventRegistration(int eventId, int userId, String firstName, String lastName, Integer age, String interests, long createdAt) {
        this.eventId = eventId;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.interests = interests;
        this.createdAt = createdAt;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getInterests() { return interests; }
    public void setInterests(String interests) { this.interests = interests; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
