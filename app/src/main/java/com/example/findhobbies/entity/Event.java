package com.example.findhobbies.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "events")
public class Event implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String location;
    private String date;
    private String category;


    private double latitude;
    private double longitude;


    private String description;

    public Event(String name, String location, String date, String category,
                 double latitude, double longitude, String description) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getDate() { return date; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
