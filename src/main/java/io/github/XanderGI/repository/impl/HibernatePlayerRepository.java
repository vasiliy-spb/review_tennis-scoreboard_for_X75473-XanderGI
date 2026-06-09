package io.github.XanderGI.repository.impl;

import io.github.XanderGI.entity.Player;
import io.github.XanderGI.repository.PlayerRepository;
import io.github.XanderGI.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Optional;

public class HibernatePlayerRepository implements PlayerRepository {

    // Текст HQL запроса удобнее читать, когда он логично разбит на строки, даже если он короткий.
        // Для визуального разделения запросов на строки лучше использовать текстовые блоки

    // Название именованного параметра тоже можно вынести в константу с понятным названием.

    // TODO: Тело каждого метода стоит обернуть в try-catch и отлавливать HibernateException или PersistenceException.
        // Слой DAO должен перехватывать специфичные для технологии исключения (например, `HibernateException`)
        // и оборачивать их в свои, более общие исключения слоя доступа к данным (например, `DataAccessException`).
        // Это скрывает детали реализации от верхних слоёв и делает их независимыми от деталей реализации DAO.

    // Нарушение Принципа Инверсии Зависимостей (Dependency Inversion Principle). HibernateUtil (или SessionFactory)
        // лучше принимать в конструктор в качестве аргумента, а не обращаться к статическим методам утилитного класса.

    // Для единообразия кодовой базы в этом классе тоже можно использовать Criteria API (как в HibernateMatchRepository)

    // Можно добавить суффикс '_QHL' или '_QUERY'
    private static final String FIND_BY_NAME = "FROM Player WHERE LOWER(name) = LOWER(:playerName)";

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