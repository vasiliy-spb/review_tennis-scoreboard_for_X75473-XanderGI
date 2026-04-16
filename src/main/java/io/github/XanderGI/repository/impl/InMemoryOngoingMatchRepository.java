package io.github.XanderGI.repository.impl;

import io.github.XanderGI.model.MatchScore;
import io.github.XanderGI.repository.OngoingMatchRepository;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
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

    @Override
    public void removeStaleMatches(long expirationMinutes) {
        int removedCount = 0;
        Instant thresholdTime = Instant.now().minus(expirationMinutes, ChronoUnit.MINUTES);

        for (Map.Entry<UUID, MatchScore> entry : matches.entrySet()) {
            if (entry.getValue().getLastActivityAt().isBefore(thresholdTime)) {
                matches.remove(entry.getKey());
                removedCount++;
            }
        }

        if (removedCount > 0) {
            log.info("Cleaned up {} stale matches", removedCount);
        }
    }
}