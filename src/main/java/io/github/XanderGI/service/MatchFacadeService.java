package io.github.XanderGI.service;

import io.github.XanderGI.exception.MatchNotFoundException;
import io.github.XanderGI.model.MatchScore;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class MatchFacadeService {
    private final OngoingMatchesService ongoingMatchesService;
    private final MatchScoreCalculationService calculationMatchService;
    private final FinishedMatchesPersistenceService finishedMatchesService;

    public MatchScore playRally(UUID matchId, Integer playerId) {
        MatchScore matchScore = ongoingMatchesService.get(matchId)
                .orElseThrow(() -> new MatchNotFoundException("Match not found"));

        synchronized (matchScore) {
            if (matchScore.isMatchOver()) {
                return matchScore;
            }

            calculationMatchService.addPoint(matchScore, playerId);

            if (matchScore.isMatchOver()) {
                finishedMatchesService.save(matchScore);
                ongoingMatchesService.remove(matchId);
            }
        }

        return matchScore;
    }
}