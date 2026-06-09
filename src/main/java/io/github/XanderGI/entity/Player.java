package io.github.XanderGI.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor // TODO: Такой конструктор не нужен, т.к. позволяет создать объект с установленным ID.
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Players") // можно задать индекс через аннотацию, чтобы у него было понятное имя — @Table(name = "Players", indexes = @Index(...)) или в файле миграции
public class Player {

    // TODO: Для корректного и безопасного создания новых, ещё не сохранённых в БД игроков, стоит создать и использовать конструктор со всеми полями, кроме ID.

    // TODO: Сейчас ограничения на длину имени противоречивы:
        // - в классе Player — 255 (по умолчанию)
        // - в скрипте миграции — 255 (по умолчанию)
        // - в ValidationUtil — 50
        // Стоит привести их в согласованное состояние. Длина 50 выглядит разумной для этого проекта.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @EqualsAndHashCode.Include
    @Column(unique = true) // Стоит добавить length = 50 и nullable = false
    private String name;
}