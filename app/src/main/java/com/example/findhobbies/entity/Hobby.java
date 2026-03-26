package com.example.findhobbies.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "hobby")
public class Hobby {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String name;

    private String description;

    private boolean selected;


    public Hobby(@NonNull String name, String description) {
        this.name = name;
        this.description = description;
        this.selected = false;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @NonNull
    public String getName() { return name; }
    public void setName(@NonNull String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }

    @Override
    public String toString() {
        return name;
    }
}
