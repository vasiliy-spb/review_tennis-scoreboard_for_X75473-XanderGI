package io.github.XanderGI.service;

import io.github.XanderGI.entity.Player;
import io.github.XanderGI.exception.InvalidMatchException;
import io.github.XanderGI.exception.MatchRepositoryException;
import io.github.XanderGI.model.MatchScore;
import io.github.XanderGI.model.PlayerScore;
import io.github.XanderGI.repository.OngoingMatchRepository;
import io.github.XanderGI.repository.PlayerRepository;
import io.github.XanderGI.util.HibernateUtil;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class OngoingMatchesService {
    private static final int MAX_RETRIES = 3;
    private final OngoingMatchRepository ongoingMatchRepository;
    private final PlayerRepository playerRepository;

    //todo: add logging framework
    public UUID create(String nameOne, String nameTwo) {
        if (nameOne.equalsIgnoreCase(nameTwo)) {
            throw new InvalidMatchException("The names of the players must be different");
        }

        for (int i = 1; i <= MAX_RETRIES; i++) {
            Transaction transaction = null;

            try {
                Session session = HibernateUtil.getSession();
                transaction = session.beginTransaction();

                MatchScore matchScore = executeMatchCreationLogic(nameOne, nameTwo);

                transaction.commit();

                return ongoingMatchRepository.add(matchScore);
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }

                if (isConstraintViolation(e) && i < MAX_RETRIES) {
                    //replace logging
                    System.err.printf("Collision detected for %s and %s. Retrying attempt %d%n", nameOne, nameTwo, i);
                    continue;
                }

                throw new MatchRepositoryException("Failed to create match", e);
            }
        }

        throw new IllegalStateException("Unreachable code");
    }

    public Optional<MatchScore> get(UUID matchId) {
        return ongoingMatchRepository.get(matchId);
    }

    public void remove(UUID matchId) {
        ongoingMatchRepository.remove(matchId);
    }

    private Player getOrCreatePlayer(String name) {
        return playerRepository.findByName(name)
                .orElseGet(() -> playerRepository.save(new Player(null, name)));
    }

    private MatchScore executeMatchCreationLogic(String nameOne, String nameTwo) {
        Player playerOne = getOrCreatePlayer(nameOne);
        Player playerTwo = getOrCreatePlayer(nameTwo);

        return new MatchScore(
                playerOne,
                playerTwo,
                PlayerScore.matchStart(),
                PlayerScore.matchStart()
        );
    }

    private boolean isConstraintViolation(Exception e) {
        Throwable currentCause = e;

        while (currentCause != null) {
            if (currentCause instanceof ConstraintViolationException) {
                return true;
            }
            currentCause = currentCause.getCause();
        }

        return false;
    }
}