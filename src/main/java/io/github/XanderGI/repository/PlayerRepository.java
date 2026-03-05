package io.github.XanderGI.repository;

import io.github.XanderGI.entity.Match;
import io.github.XanderGI.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Optional;

public class PlayerRepository {
    private static Configuration configuration = new Configuration().addAnnotatedClasses(Player .class, Match .class);
    private static SessionFactory sessionFactory = configuration.buildSessionFactory();

    public Optional<Player> findByName(String name) {
        String hql = "FROM Player WHERE name = :playerName";
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            Optional<Player> player = session.createQuery(hql, Player.class)
                    .setParameter("playerName", name)
                    .uniqueResultOptional();

            session.getTransaction().commit();

            return player;
        }
    }

    public Player save(String name) {
        Player player = new Player(null, name);

        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();

            session.persist(player);

            session.getTransaction().commit();
        }

        return player;
    }
}