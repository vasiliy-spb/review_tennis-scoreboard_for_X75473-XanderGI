package io.github.XanderGI.service;

import io.github.XanderGI.dto.MatchesPageDto;
import io.github.XanderGI.entity.Match;
import io.github.XanderGI.exception.MatchNotFinishedException;
import io.github.XanderGI.exception.MatchPersistenceException;
import io.github.XanderGI.exception.MatchRepositoryException;
import io.github.XanderGI.model.MatchScore;
import io.github.XanderGI.repository.MatchRepository;
import io.github.XanderGI.utils.HibernateUtil;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@RequiredArgsConstructor
public class FinishedMatchesPersistenceService {
    private static final int PAGE_SIZE = 5;
    private final MatchRepository matchRepository;

    public void save(MatchScore matchScore) {
        if (!matchScore.isMatchOver()) {
            throw new MatchNotFinishedException("Match not finished for finishMatch");
        }

        Transaction transaction = null;
        try {
            Session session = HibernateUtil.getSession();
            transaction = session.beginTransaction();

            matchRepository.save(matchScore);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new MatchPersistenceException("Couldn't finishMatch match", e);
        }
    }

    public MatchesPageDto getMatchesPage(int page, String filterName) {
        Transaction transaction = null;

        try {
            int offset = (page - 1) * PAGE_SIZE;

            Session session = HibernateUtil.getSession();
            transaction = session.beginTransaction();

            List<Match> matches = matchRepository.findMatches(offset, PAGE_SIZE, filterName);
            Long countOfMatches = matchRepository.countByPlayerName(filterName);

            transaction.commit();

            int totalPages = (int) Math.ceil((double) countOfMatches / PAGE_SIZE);

            return new MatchesPageDto(
                    matches,
                    totalPages
            );
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            String message = String.format("Couldn't get matches for page %d with filter '%s'", page, filterName);
            throw new MatchRepositoryException(message, e);
        }
    }
}