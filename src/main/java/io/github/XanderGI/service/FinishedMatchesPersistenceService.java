package io.github.XanderGI.service;

import io.github.XanderGI.dto.MatchesPageDto;
import io.github.XanderGI.entity.Match;
import io.github.XanderGI.entity.Player;
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

    // todo: вынести метод save в MatchRepository
    public void save(MatchScore matchScore) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Player winner = matchScore.getWinner()
                    .orElseThrow(() -> new MatchNotFinishedException("Match not finished for save"));

            Match match = new Match(
                    null,
                    matchScore.getPlayerOne(),
                    matchScore.getPlayerTwo(),
                    winner
            );

            session.persist(match);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new MatchPersistenceException("Couldn't save match", e);
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