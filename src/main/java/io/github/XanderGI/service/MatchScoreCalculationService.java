package io.github.XanderGI.service;

import io.github.XanderGI.model.MatchScore;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MatchScoreCalculationService {
    public void addPoint(MatchScore matchScore, Integer playerId) {
        matchScore.pointWonBy(playerId);
    }
}