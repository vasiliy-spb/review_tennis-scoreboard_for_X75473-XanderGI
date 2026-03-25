package io.github.XanderGI.repository;

import io.github.XanderGI.entity.Match;
import io.github.XanderGI.entity.Player;
import io.github.XanderGI.model.MatchScore;
import io.github.XanderGI.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class MatchRepository {
    private static final String FIND_ALL_MATCHES = "FROM Match";
    private static final String COUNT_ALL_MATCHES = "SELECT COUNT(*) FROM Match";
    private static final String FILTER_BY_NAME_CLAUSE = "WHERE playerOne.name = :playerName OR playerTwo.name = :playerName";

    public void save(MatchScore matchScore) {
        Session session = HibernateUtil.getSession();

        Player winner = matchScore.getWinner()
                .orElseThrow(() -> new IllegalStateException("Match winner is absent but match is finished"));

        Match match = new Match(
                null,
                matchScore.getPlayerOne(),
                matchScore.getPlayerTwo(),
                winner
        );

        session.persist(match);
    }

    public List<Match> findMatches(int offset, int limit, String filterName) {
        String hql = buildHql(FIND_ALL_MATCHES, filterName);

        Session session = HibernateUtil.getSession();

        Query<Match> query = session.createQuery(hql, Match.class);
        applyFilterParameter(query, filterName);

        return query.setFirstResult(offset).setMaxResults(limit).list();
    }

    public Long countByPlayerName(String filterName) {
        String hql = buildHql(COUNT_ALL_MATCHES, filterName);

        Session session = HibernateUtil.getSession();

        Query<Long> query = session.createQuery(hql, Long.class);
        applyFilterParameter(query, filterName);

        return query.getSingleResult();
    }

    private String buildHql(String baseQuery, String filterName) {
        if (hasFilter(filterName)) {
            return baseQuery + " " + FILTER_BY_NAME_CLAUSE;
        }

        return baseQuery;
    }

    private void applyFilterParameter(Query<?> query, String filterName) {
        if (hasFilter(filterName)) {
            query.setParameter("playerName", filterName);
        }
    }

    private boolean hasFilter(String filterName) {
        return filterName != null && !filterName.isBlank();
    }
}