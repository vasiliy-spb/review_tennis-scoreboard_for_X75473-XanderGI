package io.github.XanderGI.repository;

import io.github.XanderGI.model.MatchScore;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchRepository {
    private final ConcurrentHashMap<UUID, MatchScore> matches = new ConcurrentHashMap<>();

    public Optional<MatchScore> get(UUID matchId) {
        return Optional.ofNullable(matches.get(matchId));
    }

    public UUID add(MatchScore matchScore) {
        UUID uuid = UUID.randomUUID();
        matches.put(uuid, matchScore);
        return uuid;
    }

    public void remove(UUID matchId) {
        matches.remove(matchId);
    }
}