package io.github.XanderGI.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor // TODO: Такой конструктор не нужен, т.к. позволяет создать объект с установленным ID.
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Matches", check = {
        @CheckConstraint(name = "chk_matches_players_not_equal", constraint = "Player1 <> Player2"),
        @CheckConstraint(name = "chk_matches_valid_winner", constraint = "Winner = Player1 OR Winner = Player2")
}) // "Matches" является зарезервированным словом в некоторых СУБД.
    // Здесь проблем не будет, но лучше не выбирать такие названия. (см. файл "sql-keywords.md" в этом же пакете)
public class Match {

    // TODO: Для корректного и безопасного создания новых, ещё не сохранённых в БД, матчей стоит создать и использовать конструктор со всеми полями, кроме ID.

    // Связи `@ManyToOne` не имеют явного указания о стратегии загрузки.
        // По умолчанию для `@ManyToOne` используется `FetchType.EAGER`, что приводит к немедленной загрузке связанных сущностей при загрузке `Match`.
        // Это может вызывать проблемы производительности (N+1 запросов) и излишнюю загрузку данных, особенно если связанные объекты не всегда нужны.

    // Колонки игроков и победителя в `@JoinColumn` названы `Player1`, `Player2`, `Winner`.
        // Для колонок, хранящих внешний ключ, уместно добавлять суффикс `_id`, чтобы было очевидно, что в них хранится идентификатор, а не какая-то другая информация.
        // А также использовать более идиоматичный в SQL стиль lower_snake_case для названий колонок в БД.

    // Для обязательных полей стоит добавить `optional = false` в `@ManyToOne` или `nullable = false` в `@JoinColumn` (можно добавить оба параметра).
        // Целостность данных должна обеспечиваться на всех уровнях: в приложении (валидация) и в БД (constraints). Отсутствие ограничений в БД означает,
        // что данные могут быть испорчены из-за ошибок в приложении или при прямом доступе к БД.
        //
        // А также можно добавить атрибут `updatable = false`. Это атрибут запрещает изменять колонку после её первоначальной вставки.
        // Игроки матча и победитель не должны меняться, поэтому эти колонки можно защитить от обновлений.

    // Если equals и hashCode не используются в проекте, их можно не переопределять.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne // Стоит добавить FetchType.LAZY, а также optional = false
    @JoinColumn(name = "Player1") // Можно добавить nullable = false и updatable = false
    private Player playerOne;
    @ManyToOne // Стоит добавить FetchType.LAZY, а также optional = false
    @JoinColumn(name = "Player2") // Можно добавить nullable = false и updatable = false
    private Player playerTwo;
    @ManyToOne // Стоит добавить FetchType.LAZY, а также optional = false
    @JoinColumn(name = "Winner") // Можно добавить nullable = false и updatable = false
    private Player winner;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Match match)) {
            return false;
        }

        return getId() != null && getId().equals(match.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}