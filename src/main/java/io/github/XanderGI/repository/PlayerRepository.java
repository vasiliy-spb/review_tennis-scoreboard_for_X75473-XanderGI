package io.github.XanderGI.repository;

import io.github.XanderGI.entity.Player;
import io.github.XanderGI.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Optional;

public class PlayerRepository {
    private static final String FIND_BY_NAME = "FROM Player WHERE name = :playerName";

    public Optional<Player> findByName(String name) {
        Session session = HibernateUtil.getSession();

        return session.createQuery(FIND_BY_NAME, Player.class)
                .setParameter("playerName", name)
                .uniqueResultOptional();
    }

    public Player save(Player player) {
        Session session = HibernateUtil.getSession();

        session.persist(player);

        return player;
    }
}