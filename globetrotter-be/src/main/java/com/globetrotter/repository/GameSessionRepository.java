package com.globetrotter.repository;

import com.globetrotter.model.GameSession;
import com.globetrotter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
    List<GameSession> findByUserOrderByStartTimeDesc(User user);
    /**
     * Find active sessions for a user, ordered by start time (newest first)
     */
    List<GameSession> findByUserAndActiveIsTrueOrderByStartTimeDesc(User user);

    /**
     * Find a session by ID
     */
    Optional<GameSession> findById(Long id);
}
