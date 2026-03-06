package io.github.XanderGI.model;

import io.github.XanderGI.entity.Player;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MatchScore {
    private final Player playerOne;
    private final Player playerTwo;
    private PlayerScore playerScoreOne;
    private PlayerScore playerScoreTwo;

    public void pointWonBy(Integer playerId) {
        if (playerId.equals(playerOne.getId())) {
            updateScore(playerScoreOne, playerScoreTwo);
        } else {
            updateScore(playerScoreTwo, playerScoreOne);
        }
    }

    private void updateScore(PlayerScore winner, PlayerScore loser) {
        switch (winner.getPoint()) {
            case ZERO, FIFTEEN, THIRTY -> winner.incrementPoint();
            case FORTY -> {
                switch (loser.getPoint()) {
                    case FORTY -> winner.incrementPoint();
                    case ADVANTAGE -> {
                        loser.decrementPoint();
                    }
                    case ZERO, FIFTEEN, THIRTY -> {
                        winner.incrementGame();
                        winner.clearPoints();
                        loser.clearPoints();
                    }
                }
            }
            case ADVANTAGE -> {
                switch (loser.getPoint()) {
                    case FORTY -> {
                        winner.incrementGame();
                        winner.clearPoints();
                        loser.clearPoints();
                    }
                }
            }
        }
    }
}