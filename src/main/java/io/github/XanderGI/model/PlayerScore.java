package io.github.XanderGI.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlayerScore {
    private Point point;
    private Integer game;
    private Integer set;

    public void incrementPoint() {
        point = point.next();
    }

    public void incrementGame() {
        game++;
    }

    public void decrementPoint() {
        point = point.prev();
    }

    public void clearPoints() {
        point = Point.ZERO;
    }
}