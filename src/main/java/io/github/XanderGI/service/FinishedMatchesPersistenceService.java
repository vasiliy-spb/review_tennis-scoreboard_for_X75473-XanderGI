package io.github.XanderGI.service;

import io.github.XanderGI.dto.MatchDto;
import io.github.XanderGI.dto.MatchesPageDto;
import io.github.XanderGI.exception.MatchNotFinishedException;
import io.github.XanderGI.exception.MatchPersistenceException;
import io.github.XanderGI.exception.MatchRepositoryException;
import io.github.XanderGI.infrastructure.transaction.TransactionRunner;
import io.github.XanderGI.mapper.MatchMapper;
import io.github.XanderGI.model.MatchScore;
import io.github.XanderGI.repository.MatchRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class FinishedMatchesPersistenceService {
    private static final int PAGE_SIZE = 5;
    private final MatchRepository matchRepository;
    private final TransactionRunner transactionRunner;

    public void save(MatchScore matchScore) {
        if (!matchScore.isMatchOver()) {
            throw new MatchNotFinishedException("Match not finished for finishMatch");
        }

        try {
            transactionRunner.execute(() -> matchRepository.save(matchScore));
        } catch (RuntimeException e) {
            throw new MatchPersistenceException("Couldn't finishMatch match", e);
        }
    }

    public MatchesPageDto getMatchesPage(int page, String filterName) {
        if (page < 1) {
            throw new IllegalArgumentException("Page must be positive");
        }

        try {
            int offset = calculateOffset(page);

            return transactionRunner.execute(() -> {
                        List<MatchDto> matches = matchRepository.findMatches(offset, PAGE_SIZE, filterName).stream()
                                .map(MatchMapper::toMatchDto)
                                .toList();
                        Long countOfMatches = matchRepository.countByPlayerName(filterName);
                        int totalPages = calculateTotalPages(countOfMatches);

                        return new MatchesPageDto(
                                matches,
                                totalPages
                        );
                    }
            );
        } catch (RuntimeException e) {
            String message = String.format("Couldn't get matches for page %d with filter '%s'", page, filterName);
            throw new MatchRepositoryException(message, e);
        }
    }

    private int calculateOffset(int page) {
        return (page - 1) * PAGE_SIZE;
    }

    private int calculateTotalPages(Long countOfMatches) {
        return (int) Math.ceil((double) countOfMatches / PAGE_SIZE);
    }
}