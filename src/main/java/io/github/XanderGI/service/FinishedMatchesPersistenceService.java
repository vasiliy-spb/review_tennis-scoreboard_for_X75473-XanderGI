package io.github.XanderGI.service;

import io.github.XanderGI.entity.Match;
import io.github.XanderGI.model.MatchScore;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class FinishedMatchesPersistenceService {

    public void save(MatchScore matchScore) {

    }

    public Optional<Match> get(UUID matchId) {
        return Optional.empty(); // пока не реализовал
    }
}