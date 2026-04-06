package io.github.XanderGI.repository;

import io.github.XanderGI.entity.Match;
import io.github.XanderGI.entity.Player;
import io.github.XanderGI.model.MatchScore;
import io.github.XanderGI.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class MatchRepository {

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

    public List<Match> findMatches(int offset, int limit, List<String> tokens) {
        Session session = HibernateUtil.getSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Match> criteria = criteriaBuilder.createQuery(Match.class);
        Root<Match> matchRoot = criteria.from(Match.class);

        criteria.select(matchRoot);

        Predicate[] predicates = buildSearchPredicates(criteriaBuilder, matchRoot, tokens);

        if (predicates.length > 0) {
            criteria.where(criteriaBuilder.and(predicates));
        }

        return session.createQuery(criteria)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .list();
    }

    public Long countMatchesByTokens(List<String> tokens) {
        Session session = HibernateUtil.getSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
        Root<Match> matchRoot = criteria.from(Match.class);

        criteria.select(criteriaBuilder.count(matchRoot));

        Predicate[] predicates = buildSearchPredicates(criteriaBuilder, matchRoot, tokens);

        if (predicates.length > 0) {
            criteria.where(criteriaBuilder.and(predicates));
        }

        return session.createQuery(criteria).getSingleResult();
    }

    private Predicate[] buildSearchPredicates(CriteriaBuilder criteriaBuilder, Root<Match> matchRoot, List<String> tokens) {
        List<Predicate> conditions = new ArrayList<>();

        for (String token : tokens) {
            String pattern = "%" + token + "%";
            Predicate firstPlayerName = criteriaBuilder.like(criteriaBuilder.lower(matchRoot.get("playerOne").get("name")), pattern);
            Predicate secondPlayerName = criteriaBuilder.like(criteriaBuilder.lower(matchRoot.get("playerTwo").get("name")), pattern);

            conditions.add(criteriaBuilder.or(firstPlayerName, secondPlayerName));
        }

        return conditions.toArray(new Predicate[0]);
    }
}