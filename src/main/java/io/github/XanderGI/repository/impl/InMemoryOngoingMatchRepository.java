package io.github.XanderGI.repository.impl;

import io.github.XanderGI.model.MatchScore;
import io.github.XanderGI.repository.OngoingMatchRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOngoingMatchRepository implements OngoingMatchRepository {
    private final Map<UUID, MatchScore> matches = new ConcurrentHashMap<>();

    @Override
    public Optional<MatchScore> get(UUID matchId) {
        return Optional.ofNullable(matches.get(matchId));
    }

    @Override
    public UUID add(MatchScore matchScore) {
        UUID uuid = UUID.randomUUID();
        matches.put(uuid, matchScore);
        return uuid;
    }

    @Override
    public void remove(UUID matchId) {
        matches.remove(matchId);
    }
}