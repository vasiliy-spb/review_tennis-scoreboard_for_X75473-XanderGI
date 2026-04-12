package io.github.XanderGI.repository.impl;

import io.github.XanderGI.entity.Player;
import io.github.XanderGI.repository.PlayerRepository;
import io.github.XanderGI.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Optional;

public class HibernatePlayerRepository implements PlayerRepository {
    private static final String FIND_BY_NAME = "FROM Player WHERE name = :playerName";

    @Override
    public Optional<Player> findByName(String name) {
        Session session = HibernateUtil.getSession();

        return session.createQuery(FIND_BY_NAME, Player.class)
                .setParameter("playerName", name)
                .uniqueResultOptional();
    }

    @Override
    public Player save(Player player) {
        Session session = HibernateUtil.getSession();

        session.persist(player);

        return player;
    }
}