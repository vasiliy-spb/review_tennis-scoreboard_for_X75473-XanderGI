package io.github.XanderGI.repository;

import io.github.XanderGI.entity.Match;
import io.github.XanderGI.model.MatchScore;

import java.util.List;

public interface MatchRepository {
    void save(MatchScore matchScore);

    List<Match> findMatches(int offset, int limit, List<String> tokens);

    Long countMatchesByTokens(List<String> tokens);
}