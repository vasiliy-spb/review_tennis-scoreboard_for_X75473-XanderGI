package io.github.XanderGI.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerScore {
    private Point point;
    private Integer game;
    private Integer set;
    private Integer tieBreakPoint;

    public static PlayerScore matchStart() {
        return new PlayerScore(Point.ZERO, 0, 0, 0);
    }

    protected void incrementPoint() {
        point = point.next();
    }

    protected void decrementPoint() {
        point = point.prev();
    }

    protected void clearPoints() {
        point = Point.ZERO;
    }

    protected void incrementGame() {
        game++;
    }

    protected void clearGames() {
        game = 0;
    }

    protected void incrementSet() {
        set++;
    }

    protected void incrementTieBreakPoint() {
        tieBreakPoint++;
    }

    protected void clearTieBreakPoints() {
        tieBreakPoint = 0;
    }
}