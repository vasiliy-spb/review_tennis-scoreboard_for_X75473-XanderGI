package io.github.XanderGI.service;

import io.github.XanderGI.entity.Player;
import io.github.XanderGI.exception.InvalidMatchException;
import io.github.XanderGI.exception.PlayerPersistenceException;
import io.github.XanderGI.model.MatchScore;
import io.github.XanderGI.model.PlayerScore;
import io.github.XanderGI.repository.OngoingMatchRepository;
import io.github.XanderGI.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class OngoingMatchesService {
    private final OngoingMatchRepository ongoingMatchRepository;
    private final PlayerRepository playerRepository;

    public UUID create(String nameOne, String nameTwo) {
        if (nameOne.equalsIgnoreCase(nameTwo)) {
            throw new InvalidMatchException("The names of the players must be different");
        }

        Player playerOne = getOrCreatePlayer(nameOne);
        Player playerTwo = getOrCreatePlayer(nameTwo);

        MatchScore matchScore = new MatchScore(
                playerOne,
                playerTwo,
                PlayerScore.matchStart(),
                PlayerScore.matchStart()
        );

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
            return playerRepository.findByName(name)
                    .orElseGet(() -> playerRepository.save(name));
        } catch (PlayerPersistenceException e) {
            return playerRepository.findByName(name)
                    .orElseThrow(() -> new PlayerPersistenceException("Failed to save or retrieve player:" + name, e));
        }
    }
}