package com.globetrotter.controller;

import com.globetrotter.model.Destination;
import com.globetrotter.model.GameSession;
import com.globetrotter.model.User;
import com.globetrotter.service.DestinationService;
import com.globetrotter.service.GameSessionService;
import com.globetrotter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "*")
public class GameController {

    @Autowired
    private DestinationService destinationService;

    @Autowired
    private UserService userService;

    @Autowired
    private GameSessionService gameSessionService;

    @GetMapping("/question")
    public ResponseEntity<?> getQuestion(@RequestParam String username) {
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOpt.get();
        Optional<GameSession> activeSessionOpt = gameSessionService.findActiveSessionForUser(user);
        GameSession session= activeSessionOpt.orElseGet(() -> gameSessionService.createSession(user));

        Destination destination = destinationService.getRandomDestination();
        List<String> clues = destinationService.getRandomClues(destination, 2);
        List<Destination> options = destinationService.getRandomOptions(destination, 4);

        Map<String, Object> response = new HashMap<>();
        response.put("sessionId", session.getId());
        response.put("clues", clues);
        response.put("options", options.stream()
                .map(d -> Map.of(
                        "id", d.getId(),
                        "name", d.getName(),
                        "country", d.getCountry()
                ))
                .collect(Collectors.toList()));
        response.put("correctId", destination.getId());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/answer")
    public ResponseEntity<?> submitAnswer(
            @RequestParam Long sessionId,
            @RequestParam Long destinationId,
            @RequestParam Long answerId) {

        Optional<GameSession> sessionOpt = gameSessionService.findById(sessionId);
        if (sessionOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Session not found");
        }

        Optional<Destination> destinationOpt = destinationService.findById(destinationId);
        if (destinationOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Destination not found");
        }

        Destination destination = destinationOpt.get();
        GameSession session = sessionOpt.get();
        boolean isCorrect = destinationId.equals(answerId);

        gameSessionService.recordAnswer(session, destinationId, isCorrect);

        if (isCorrect) {
            userService.updateScore(session.getUser(), 10);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("correct", isCorrect);
        response.put("destination", Map.of(
                "id", destination.getId(),
                "name", destination.getName(),
                "country", destination.getCountry(),
                "imageUrl", Objects.isNull(destination.getImageUrl())?"default img": destination.getImageUrl(),
                "funFacts", destination.getFunFacts()
        ));
        response.put("score", session.getUser().getScore());

        return ResponseEntity.ok(response);
    }
}
