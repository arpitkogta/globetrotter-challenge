package com.globetrotter.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "app_user") // Avoid reserved keyword "user"
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private int score;

    @Column(unique = true)
    private String inviteCode;

    public User() {
        // Generate unique invite code
        this.inviteCode = UUID.randomUUID().toString().substring(0, 8);
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getInviteCode() { return inviteCode; }
    public void setInviteCode(String inviteCode) { this.inviteCode = inviteCode; }
}
