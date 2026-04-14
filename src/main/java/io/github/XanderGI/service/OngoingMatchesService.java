package io.github.XanderGI.service;

import io.github.XanderGI.entity.Player;
import io.github.XanderGI.exception.InvalidMatchException;
import io.github.XanderGI.infrastructure.transaction.TransactionRunner;
import io.github.XanderGI.model.MatchScore;
import io.github.XanderGI.model.PlayerScore;
import io.github.XanderGI.repository.OngoingMatchRepository;
import io.github.XanderGI.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class OngoingMatchesService {
    private final OngoingMatchRepository ongoingMatchRepository;
    private final PlayerRepository playerRepository;
    private final TransactionRunner transactionRunner;

    public UUID create(String nameOne, String nameTwo) {
        if (nameOne.equalsIgnoreCase(nameTwo)) {
            throw new InvalidMatchException("The names of the players must be different");
        }

        MatchScore matchScore = executeMatchCreationLogic(nameOne, nameTwo);

        return ongoingMatchRepository.add(matchScore);
    }

    public Optional<MatchScore> get(UUID matchId) {
        return ongoingMatchRepository.get(matchId);
    }

    public void remove(UUID matchId) {
        ongoingMatchRepository.remove(matchId);
    }

    private Player getOrCreatePlayer(String name) {
        try {
            return transactionRunner.execute(() -> playerRepository.findByName(name)
                    .orElseGet(() -> playerRepository.save(new Player(null, name))));
        } catch (RuntimeException e) {
            if (isConstraintViolation(e)) {
                return transactionRunner.execute(() -> playerRepository.findByName(name))
                        .orElseThrow(() -> new IllegalStateException("Player must exist after constraint violation: " + name));
            }

            throw e;
        }
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