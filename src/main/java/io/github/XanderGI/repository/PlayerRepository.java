package io.github.XanderGI.repository;

import io.github.XanderGI.entity.Player;
import io.github.XanderGI.exception.PlayerPersistenceException;
import io.github.XanderGI.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;

public class PlayerRepository {
    public Optional<Player> findByName(String name) {
        String hql = "FROM Player WHERE name = :playerName";
        try (Session session = HibernateUtil.getSessionFactory().openSession()){

            Optional<Player> player = session.createQuery(hql, Player.class)
                    .setParameter("playerName", name)
                    .uniqueResultOptional();

            return player;
        }
    }

    public Player save(String name) {
        Player player = new Player(null, name);
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();

            session.persist(player);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new PlayerPersistenceException("Couldn't save the player", e);
        }

        return player;
    }
}