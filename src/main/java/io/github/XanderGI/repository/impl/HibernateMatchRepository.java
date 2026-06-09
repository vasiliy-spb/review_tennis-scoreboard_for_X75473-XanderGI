package io.github.XanderGI.repository.impl;

import io.github.XanderGI.entity.Match;
import io.github.XanderGI.entity.Player;
import io.github.XanderGI.model.MatchScore;
import io.github.XanderGI.repository.MatchRepository;
import io.github.XanderGI.util.HibernateUtil;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class HibernateMatchRepository implements MatchRepository {

    // Название каждого именованного параметра тоже можно вынести в константу с понятным названием.

    // TODO: Тело каждого метода стоит обернуть в try-catch и отлавливать HibernateException или PersistenceException.
        // Слой DAO должен перехватывать специфичные для технологии исключения (например, `HibernateException`)
        // и оборачивать их в свои, более общие исключения слоя доступа к данным (например, `DataAccessException`).
        // Это скрывает детали реализации от верхних слоёв и делает их независимыми от деталей реализации DAO.

    // Нарушение Принципа Инверсии Зависимостей (Dependency Inversion Principle). HibernateUtil (или SessionFactory)
        // лучше принимать в конструктор в качестве аргумента, а не обращаться к статическим методам утилитного класса.

    // В HQL запросе в методе `findMatches` используется `JoinType.INNER`.
        //
        // `INNER JOIN` вернёт только те записи о матчах, у которых все связанные сущности (`Player1`, `Player2`)
        // гарантированно существуют в базе. Если по какой-либо причине (например, ошибка при импорте или
        // ручное вмешательство) в таблице `Matches` окажется запись со значением `NULL` в колонке `Player1`,
        // то такой матч будет молчаливо исключён из выборки. `LEFT JOIN` является более безопасным подходом:
        //
            //  - Он вернёт все матчи, даже если у них нарушена связь с игроком.
            //  - Это позволит приложению либо упасть с `NullPointerException` (что явно укажет на проблему
                //  с целостностью данных), либо корректно обработать такую ситуацию, если она допустима.
                //  "Падать громко и рано" часто лучше, чем молча скрывать проблемы.
        //
        // Стоит заменить `JoinType.INNER` на `JoinType.LEFT` для обоих игроков
        // для большей устойчивости запроса к потенциально некорректным данным.
        //
        // (см. файл "join-fetch-left-join-fetch.md" в этом же пакете)

    // Метод findMatches (и countMatchesByTokens) принимают List<String> tokens — список частей имён,
        // по которым производится поиск. Возможно, идея была в том, чтобы реализовать поиск матчей сразу по нескольким именам,
        // но из-за того, что условия собираются через 'AND' (criteria.where(criteriaBuilder.and(predicates))),
        // поиск работает только если совпадают обе части имени (то есть в имени игрока есть каждый из токенов, а не один из них).
        // То есть сейчас при фильтрации по строке "Andy Stan" приложение не найдёт ни один матч, так как игрока с именем,
        // содержащим одновременно оба этих слова нет в БД.
        // Чтобы это исправить надо использовать условие 'OR' (criteria.where(criteriaBuilder.or(predicates)))
        // Тогда при фильтрации по строке "Andy Stan" приложение не найдёт все матчи, где в имени игрока
        // встречается ИЛИ одно ИЛИ другое слово.

    // С Criteria API можно использовать JPA Metamodel (зависимость hibernate-jpamodelgen),
        // тогда вместо передачи именованных параметров: matchRoot.fetch("playerOne", JoinType.INNER);
        // будет обращение к полям сгенерированного мета-класса: matchRoot.fetch(Match_.playerOne, JoinType.INNER);

    // TODO: Передача доменной модели MatchScore в DAO нарушает Принцип разделения ответственности (Separation of Concerns)
        // (см. файл "separation-of-concerns-principle.md" в пакете 'repository')
        // Слой DAO не должен ничего знать о доменных моделях и работать с ними.
        // Преобразовывать доменные модели в JPA Entity — это задача сервисного слоя.
    @Override
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

    // То, что пустой список означает "вернуть всё" — является негласным контрактом (хотя и понятным)
        // Лучше иметь разные методы для выборки матчей с фильтром по имени и без него,
        // чем собирать эту логику в одном методе. Если правила фильтрации поменяются,
        // то нужно будет изменить/дописать только некоторые методы, оставив логику выборки без фильтра без изменений.
    @Override
    public List<Match> findMatches(int offset, int limit, List<String> tokens) {
        Session session = HibernateUtil.getSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Match> criteria = criteriaBuilder.createQuery(Match.class);
        Root<Match> matchRoot = criteria.from(Match.class);

        matchRoot.fetch("playerOne", JoinType.INNER);
        matchRoot.fetch("playerTwo", JoinType.INNER);
        matchRoot.fetch("winner", JoinType.LEFT);

        criteria.select(matchRoot);

        Predicate[] predicates = buildSearchPredicates(criteriaBuilder, matchRoot, tokens);

        if (predicates.length > 0) {
            // Для поиска сразу по нескольким именам нужно использовать: criteria.where(criteriaBuilder.or(predicates));
            criteria.where(criteriaBuilder.and(predicates));
        }

        criteria.orderBy(criteriaBuilder.desc(matchRoot.get("id")));

        return session.createQuery(criteria)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .list();
    }

    // То, что пустой список означает "посчитать всё" — является негласным контрактом (хотя и понятным)
        // Лучше иметь разные методы для подсчёта количества матчей с фильтром по имени и без него,
        // чем собирать эту логику в одном методе. Если правила фильтрации поменяются,
        // то нужно будет изменить/дописать только некоторые методы, оставив логику выборки без фильтра без изменений.
    @Override
    public Long countMatchesByTokens(List<String> tokens) {
        Session session = HibernateUtil.getSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
        Root<Match> matchRoot = criteria.from(Match.class);

        criteria.select(criteriaBuilder.count(matchRoot));

        Predicate[] predicates = buildSearchPredicates(criteriaBuilder, matchRoot, tokens);

        if (predicates.length > 0) {
            // Для подсчёта сразу по нескольким именам нужно использовать: criteria.where(criteriaBuilder.or(predicates));
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