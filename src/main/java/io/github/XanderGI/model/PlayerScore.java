package io.github.XanderGI.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerScore {

    // Счёт в гейме, сете и матче не может быть null, поэтому лучше использовать примитивный тип int, вместо обёртки Integer

    private static final int INITIAL_VALUE = 0;
    private Point point;
    private Integer game;
    private Integer set;
    private Integer tieBreakPoint;

    // Этот класс не должен ничего знать о матче и том, как он начинается, поэтому лучше подобрать другое название.
        // Создавать PlayerScore с нужными значениями — обязанность более верхнего уровня.
    // Нет причин использовать в этом классе статический метод, вместо конструктора, который устанавливает значения по умолчанию.
    public static PlayerScore matchStart() {
        return new PlayerScore(Point.ZERO, INITIAL_VALUE, INITIAL_VALUE, INITIAL_VALUE);
    }

    void incrementPoint() {
        point = point.next();
    }

    // Поскольку point.prev() бросает исключение на всех значениях, кроме AD и
        // нет метода для проверки, можно ли безопасно выполнить point.prev(),
        // клиентский код будет вынужден использовать исключения для управления потоком выполнения,
        // если по какой-то причине понадобится вызвать decrementPoint на объекте, где значение point != ADVANTAGE.
    void decrementPoint() {
        point = point.prev();
    }

    // Возможно более понятным было бы название 'reset*'
    void clearPoints() {
        point = Point.ZERO;
    }

    void incrementGame() {
        game++;
    }

    // Возможно более понятным было бы название 'reset*'
    void clearGames() {
        game = INITIAL_VALUE;
    }

    void incrementSet() {
        set++;
    }

    void incrementTieBreakPoint() {
        tieBreakPoint++;
    }

    // Возможно более понятным было бы название 'reset*'
    void clearTieBreakPoints() {
        tieBreakPoint = INITIAL_VALUE;
    }
}