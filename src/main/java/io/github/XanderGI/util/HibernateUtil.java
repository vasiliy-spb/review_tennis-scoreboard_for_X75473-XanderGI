package io.github.XanderGI.util;

import io.github.XanderGI.config.AppConfig;
import io.github.XanderGI.entity.Match;
import io.github.XanderGI.entity.Player;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HibernateUtil {
    private static final Configuration configuration = new Configuration();
    @Getter
    private static SessionFactory sessionFactory;

    public static void init(AppConfig config) {
        configuration.setProperty("hibernate.connection.url", config.dbUrl());
        configuration.setProperty("hibernate.connection.username", config.dbUsername());
        configuration.setProperty("hibernate.connection.password", config.dbPassword());

        configuration.addAnnotatedClasses(Player.class, Match.class);
        sessionFactory = configuration.buildSessionFactory();
    }

    public static void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public static Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}