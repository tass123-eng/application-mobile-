package com.example.findhobbies.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "message")
public class Message {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int senderId;
    private int receiverId;
    private String content;
    private long timestamp;

    public Message(int senderId, int receiverId, String content, long timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSenderId() { return senderId; }
    public int getReceiverId() { return receiverId; }
    public String getContent() { return content; }
    public long getTimestamp() { return timestamp; }
}
