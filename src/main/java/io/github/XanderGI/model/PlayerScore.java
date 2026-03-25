package io.github.XanderGI.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerScore {
    private static final int INITIAL_VALUE = 0;
    private Point point;
    private Integer game;
    private Integer set;
    private Integer tieBreakPoint;

    public static PlayerScore matchStart() {
        return new PlayerScore(Point.ZERO, INITIAL_VALUE, INITIAL_VALUE, INITIAL_VALUE);
    }

    void incrementPoint() {
        point = point.next();
    }

    void decrementPoint() {
        point = point.prev();
    }

    void clearPoints() {
        point = Point.ZERO;
    }

    void incrementGame() {
        game++;
    }

    void clearGames() {
        game = INITIAL_VALUE;
    }

    void incrementSet() {
        set++;
    }

    void incrementTieBreakPoint() {
        tieBreakPoint++;
    }

    void clearTieBreakPoints() {
        tieBreakPoint = INITIAL_VALUE;
    }
}