package io.github.XanderGI.service;

import io.github.XanderGI.entity.Player;
import io.github.XanderGI.exception.InvalidMatchException;
import io.github.XanderGI.model.MatchScore;
import io.github.XanderGI.model.PlayerScore;
import io.github.XanderGI.repository.OngoingMatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class OngoingMatchesService {
    private final OngoingMatchRepository ongoingMatchRepository;
    private final PlayerService playerService;

    public UUID create(String nameOne, String nameTwo) {
        if (nameOne.equalsIgnoreCase(nameTwo)) {
            throw new InvalidMatchException("The names of the players must be different");
        }

        Player playerOne = playerService.getOrCreatePlayer(nameOne);
        Player playerTwo = playerService.getOrCreatePlayer(nameTwo);

        MatchScore matchScore = new MatchScore(
                playerOne,
                playerTwo,
                PlayerScore.matchStart(),
                PlayerScore.matchStart()
        );

        UUID uuid = ongoingMatchRepository.add(matchScore);

        log.info("[Match {}] Created: {} vs {}", uuid, playerOne.getName(), playerTwo.getName());

        return uuid;
    }

    public Optional<MatchScore> get(UUID matchId) {
        return ongoingMatchRepository.get(matchId);
    }

    public void remove(UUID matchId) {
        ongoingMatchRepository.remove(matchId);
    }
}