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

    public void incrementPoint() {
        point = point.next();
    }

    public void decrementPoint() {
        point = point.prev();
    }

    public void clearPoints() {
        point = Point.ZERO;
    }

    public void incrementGame() {
        game++;
    }

    public void clearGames() {
        game = 0;
    }

    public void incrementSet() {
        set++;
    }

    public void clearSet() {
        set = 0;
    }

    public void incrementTieBreakPoint() {
        tieBreakPoint++;
    }

    public void clearTieBreakPoints() {
        tieBreakPoint = 0;
    }
}