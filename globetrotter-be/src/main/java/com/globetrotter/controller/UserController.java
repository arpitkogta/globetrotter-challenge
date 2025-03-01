package com.globetrotter.controller;

import com.globetrotter.model.GameSession;
import com.globetrotter.model.User;
import com.globetrotter.service.GameSessionService;
import com.globetrotter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GameSessionService gameSessionService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam String username) {
        Optional<User> existingUserOpt = userService.findByUsername(username);

        User user;
        GameSession session;
        boolean isNewUser = false;

        if (existingUserOpt.isPresent()) {
            // Existing user
            user = existingUserOpt.get();

            // Check for active session
            Optional<GameSession> activeSessionOpt = gameSessionService.findActiveSessionForUser(user);

            if (activeSessionOpt.isPresent()) {
                // Use existing active session
                session = activeSessionOpt.get();
            } else {
                // Create new session for returning user
                session = gameSessionService.createSession(user);
            }
        } else {
            // New user registration
            isNewUser = true;
            user = userService.registerUser(username);

            session = gameSessionService.createSession(user);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("inviteCode", user.getInviteCode());
        response.put("score", user.getScore());
        response.put("sessionId", session.getId());
        response.put("isNewUser", isNewUser);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserInfo(@PathVariable String username) {
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();

        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("inviteCode", user.getInviteCode());
        response.put("score", user.getScore());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/invite/{inviteCode}")
    public ResponseEntity<?> getUserByInviteCode(@PathVariable String inviteCode) {
        Optional<User> userOpt = userService.findByInviteCode(inviteCode);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();

        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("score", user.getScore());

        return ResponseEntity.ok(response);
    }
}