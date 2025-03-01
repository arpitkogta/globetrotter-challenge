package com.globetrotter.service;

import com.globetrotter.model.GameSession;
import com.globetrotter.model.User;
import com.globetrotter.repository.GameSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GameSessionService {

    @Autowired
    private GameSessionRepository gameSessionRepository;

    public GameSession createSession(User user) {
        GameSession session = new GameSession();
        session.setUser(user);
        session.setStartTime(LocalDateTime.now());
        session.setCorrectAnswers(0);
        return gameSessionRepository.save(session);
    }

    public GameSession recordAnswer(GameSession session, Long destinationId, boolean correct) {
        session.getAnsweredDestinations().add(destinationId);
        if (correct) {
            session.setCorrectAnswers(session.getCorrectAnswers() + 1);
        }
        session.setLastActivityTime(LocalDateTime.now());
        return gameSessionRepository.save(session);
    }

    public List<GameSession> getUserSessions(User user) {
        return gameSessionRepository.findByUserOrderByStartTimeDesc(user);
    }

    public Optional<GameSession> findById(Long sessionId) {
        return gameSessionRepository.findById(sessionId);
    }

    public Optional<GameSession> findActiveSessionForUser(User user) {
        List<GameSession> userSessions = gameSessionRepository.findByUserAndActiveIsTrueOrderByStartTimeDesc(user);

        if (userSessions.isEmpty()) {
            return Optional.empty();
        }

        GameSession mostRecentSession = userSessions.get(0);

        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        LocalDateTime lastActivity = mostRecentSession.getLastActivityTime() != null ?
                mostRecentSession.getLastActivityTime() :
                mostRecentSession.getStartTime();

        if (lastActivity.isAfter(oneDayAgo)) {
            return Optional.of(mostRecentSession);
        } else {
            mostRecentSession.setActive(false);
            gameSessionRepository.save(mostRecentSession);
            return Optional.empty();
        }
    }

}
