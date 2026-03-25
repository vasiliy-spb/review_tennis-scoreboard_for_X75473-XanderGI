package io.github.XanderGI.repository;

import io.github.XanderGI.model.MatchScore;

import java.util.Optional;
import java.util.UUID;

public interface OngoingMatchRepository {
    Optional<MatchScore> get(UUID matchId);

    UUID add(MatchScore matchScore);

    void remove(UUID matchId);
}