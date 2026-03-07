package io.github.XanderGI.service;

import io.github.XanderGI.exception.MatchNotFoundException;
import io.github.XanderGI.model.MatchScore;
import io.github.XanderGI.repository.OngoingMatchRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class MatchScoreCalculationService {
    private final OngoingMatchRepository ongoingMatchRepository;
    private final FinishedMatchesPersistenceService finishedMatchesService;

    public void addPoint(UUID matchId, Integer playerId) {
        MatchScore matchScore = ongoingMatchRepository.get(matchId)
                .orElseThrow(() -> new MatchNotFoundException("Match not found"));

        matchScore.pointWonBy(playerId);

        if (matchScore.isMatchOver()) {
            finishedMatchesService.save(matchScore);
            ongoingMatchRepository.remove(matchId);
        }
    }
}