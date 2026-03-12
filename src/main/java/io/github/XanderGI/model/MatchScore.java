package io.github.XanderGI.model;

import io.github.XanderGI.entity.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public class MatchScore {
    private final static int GAMES_TO_WIN_SET = 6;
    private final static int SETS_TO_WIN_MATCH = 2;
    private final static int MIN_GAMES_DIFFERENCE = 2;
    private final static int POINTS_TO_WIN_TIEBREAK = 7;

    private final Player playerOne;
    private final Player playerTwo;
    private PlayerScore playerScoreOne;
    private PlayerScore playerScoreTwo;

    public boolean isMatchOver() {
        return playerScoreOne.getSet().equals(SETS_TO_WIN_MATCH) || playerScoreTwo.getSet().equals(SETS_TO_WIN_MATCH);
    }

    public Optional<Player> getWinner() {
        if (playerScoreOne.getSet().equals(SETS_TO_WIN_MATCH)) {
            return Optional.of(playerOne);
        } else if (playerScoreTwo.getSet().equals(SETS_TO_WIN_MATCH)) {
            return Optional.of(playerTwo);
        }

        return Optional.empty();
    }

    public void pointWonBy(Integer playerId) {
        if (isMatchOver()) {
            return;
        }

        PlayerScore winner;
        PlayerScore loser;

        if (playerId.equals(playerOne.getId())) {
            winner = playerScoreOne;
            loser = playerScoreTwo;
        } else {
            winner = playerScoreTwo;
            loser = playerScoreOne;
        }

        if (isTieBreak()) {
            updateTieBreakScore(winner, loser);
        } else {
            updateScore(winner, loser);
        }
    }

    private void updateScore(PlayerScore winner, PlayerScore loser) {
        switch (winner.getPoint()) {
            case ZERO, FIFTEEN, THIRTY -> winner.incrementPoint();
            case FORTY -> handleWinnerForty(winner, loser);
            case ADVANTAGE -> processGameWin(winner, loser);
        }
    }

    private void handleWinnerForty(PlayerScore winner, PlayerScore loser) {
        switch (loser.getPoint()) {
            case FORTY -> winner.incrementPoint();
            case ADVANTAGE -> loser.decrementPoint();
            case ZERO, FIFTEEN, THIRTY -> processGameWin(winner, loser);
        }
    }

    private void processGameWin(PlayerScore winner, PlayerScore loser) {
        finishGame(winner, loser);

        if (isSetWin(winner, loser)) {
            finishSet(winner, loser);
        }
    }

    private void finishGame(PlayerScore winner, PlayerScore loser) {
        winner.incrementGame();
        winner.clearPoints();
        loser.clearPoints();
    }

    private void finishSet(PlayerScore winner, PlayerScore loser) {
        winner.incrementSet();
        winner.clearGames();
        loser.clearGames();
    }

    private void updateTieBreakScore(PlayerScore winner, PlayerScore loser) {
        winner.incrementTieBreakPoint();

        if (isTieBreakWin(winner)) {
            finishSet(winner, loser);

            winner.clearTieBreakPoints();
            loser.clearTieBreakPoints();
        }
    }

    public boolean isTieBreak() {
        return playerScoreOne.getGame().equals(GAMES_TO_WIN_SET) && playerScoreTwo.getGame().equals(GAMES_TO_WIN_SET);
    }

    private boolean isSetWin(PlayerScore winner, PlayerScore loser) {
        return winner.getGame() >= GAMES_TO_WIN_SET && Math.abs(winner.getGame() - loser.getGame()) >= MIN_GAMES_DIFFERENCE;
    }

    private boolean isTieBreakWin(PlayerScore winner) {
        return winner.getTieBreakPoint().equals(POINTS_TO_WIN_TIEBREAK);
    }
}