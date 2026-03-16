package io.github.XanderGI.repository;

import io.github.XanderGI.entity.Match;
import io.github.XanderGI.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class MatchRepository {
    public List<Match> findMatches(int offset, int limit, String filterName) {
        boolean isFilterSet = filterName != null && !filterName.isBlank();
        String hql = "FROM Match";

        if (isFilterSet) {
            hql = "FROM Match WHERE playerOne.name = :playerName OR playerTwo.name = :playerName";
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            Query<Match> query = session.createQuery(hql, Match.class);

            if (isFilterSet) {
                query.setParameter("playerName", filterName);
            }

            List<Match> matches = query.setFirstResult(offset).setMaxResults(limit).getResultList();

            session.getTransaction().commit();

            return matches;
        }
    }

    public Long countByPlayerName(String filterName) {
        boolean isFilterSet = filterName != null && !filterName.isBlank();
        String hql = "SELECT COUNT(*) FROM Match";

        if (isFilterSet) {
            hql = "SELECT COUNT(*) FROM Match WHERE playerOne.name = :playerName OR playerTwo.name = :playerName";
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            Query<Long> query = session.createQuery(hql, Long.class);

            if (isFilterSet) {
                query.setParameter("playerName", filterName);
            }

            Long countOfPage = query.getSingleResult();

            session.getTransaction().commit();

            return countOfPage;
        }
    }
}