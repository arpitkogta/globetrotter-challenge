package com.globetrotter.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "destination",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "country"})
)
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String country;
    private String imageUrl;

    @ElementCollection
    private List<String> clues = new ArrayList<>();

    @ElementCollection
    private List<String> funFacts = new ArrayList<>();

    @ElementCollection
    private List<String> trivia = new ArrayList<>();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public List<String> getClues() { return clues; }
    public void setClues(List<String> clues) { this.clues = clues; }

    public List<String> getFunFacts() { return funFacts; }
    public void setFunFacts(List<String> funFacts) { this.funFacts = funFacts; }

    public List<String> getTrivia() { return trivia; }
    public void setTrivia(List<String> trivia) { this.trivia = trivia; }
}
