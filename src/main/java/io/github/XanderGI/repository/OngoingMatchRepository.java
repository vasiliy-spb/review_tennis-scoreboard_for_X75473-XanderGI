package io.github.XanderGI.repository;

import io.github.XanderGI.model.MatchScore;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchRepository {
    private final ConcurrentHashMap<UUID, MatchScore> matches = new ConcurrentHashMap<>();

    public MatchScore get(UUID uuid) {
        return matches.get(uuid);
    }

    public UUID add(MatchScore matchScore) {
        UUID uuid = UUID.randomUUID();
        matches.put(uuid, matchScore);
        return uuid;
    }
}