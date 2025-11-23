package com.quiz.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "players")
public class Player {
    @Id
    private String id;          // Unique ID (UUID or generated)
    private String userId;      // Optional: links to the User account
    private String name;        // Display name chosen by player

    public Player(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        // this.userId could be set if a logged-in user joins
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
