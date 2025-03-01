package com.globetrotter.service;

import com.globetrotter.model.User;
import com.globetrotter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setScore(0);
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByInviteCode(String inviteCode) {
        return userRepository.findByInviteCode(inviteCode);
    }

    public User updateScore(User user, int points) {
        user.setScore(user.getScore() + points);
        return userRepository.save(user);
    }
}
