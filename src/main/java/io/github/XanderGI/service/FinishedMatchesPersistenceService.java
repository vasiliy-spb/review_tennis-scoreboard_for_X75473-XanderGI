package io.github.XanderGI.service;

import io.github.XanderGI.entity.Match;
import io.github.XanderGI.entity.Player;
import io.github.XanderGI.exception.MatchNotFinishedException;
import io.github.XanderGI.exception.MatchPersistenceException;
import io.github.XanderGI.model.MatchScore;
import io.github.XanderGI.utils.HibernateUtil;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class FinishedMatchesPersistenceService {

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

    public Optional<Match> get(UUID matchId) {
        return Optional.empty(); // пока не реализовал
    }
}