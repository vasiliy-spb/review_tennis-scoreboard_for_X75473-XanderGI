package io.github.XanderGI.repository;

import io.github.XanderGI.entity.Match;
import io.github.XanderGI.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class MatchRepository {
    private static final String FIND_ALL_MATCHES = "FROM Match ";
    private static final String COUNT_ALL_MATCHES = "SELECT COUNT(*) FROM Match ";
    private static final String FILTER_BY_NAME_CLAUSE = "WHERE playerOne.name = :playerName OR playerTwo.name = :playerName";

    public List<Match> findMatches(int offset, int limit, String filterName) {
        String hql = buildHql(FIND_ALL_MATCHES, filterName);

        Session session = HibernateUtil.getSession();

        Query<Match> query = session.createQuery(hql, Match.class);
        applyFilterParameter(query, filterName);
        List<Match> matches = query.setFirstResult(offset).setMaxResults(limit).list();

        return matches;
    }

    public Long countByPlayerName(String filterName) {
        String hql = buildHql(COUNT_ALL_MATCHES, filterName);

        Session session = HibernateUtil.getSession();

        Query<Long> query = session.createQuery(hql, Long.class);
        applyFilterParameter(query, filterName);
        Long countOfMatches = query.getSingleResult();

        return countOfMatches;
    }

    private String buildHql(String baseQuery, String filterName) {
        boolean isFilterSet = filterName != null && !filterName.isBlank();
        String hql = baseQuery;

        if (isFilterSet) {
            hql += FILTER_BY_NAME_CLAUSE;
        }

        return hql;
    }

    private void applyFilterParameter(Query<?> query, String filterName) {
        if (filterName != null && !filterName.isBlank()) {
            query.setParameter("playerName", filterName);
        }
    }
}