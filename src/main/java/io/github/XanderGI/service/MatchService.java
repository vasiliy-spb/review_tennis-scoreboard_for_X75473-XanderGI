package io.github.XanderGI.service;

import io.github.XanderGI.entity.Player;
import io.github.XanderGI.exception.InvalidMatchException;
import io.github.XanderGI.model.MatchScore;
import io.github.XanderGI.model.PlayerScore;
import io.github.XanderGI.model.Point;
import io.github.XanderGI.repository.OngoingMatchRepository;
import io.github.XanderGI.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

// todo: нет констистентости между нейминогом репозиториев? один PlayerRepository, а второй должен быть MatchRepository

@RequiredArgsConstructor
public class MatchService {
    private final OngoingMatchRepository ongoingMatchRepository;
    private final PlayerRepository playerRepository;

    public UUID create(String nameOne, String nameTwo) {
        if (nameOne.equals(nameTwo)) {
            throw new InvalidMatchException("The names of the players must be different");
        }

        Player playerOne = playerRepository.findByName(nameOne)
                .orElseGet(() -> playerRepository.save(nameOne));

        Player playerTwo = playerRepository.findByName(nameTwo)
                .orElseGet(() -> playerRepository.save(nameTwo));

        PlayerScore playerScoreOne = new PlayerScore(Point.ZERO, 0,0);
        PlayerScore playerScoreTwo = new PlayerScore(Point.ZERO, 0,0);

        MatchScore matchScore = new MatchScore(
                playerOne,
                playerTwo,
                playerScoreOne,
                playerScoreTwo
        );

        return ongoingMatchRepository.add(matchScore);
    }
}