package io.github.XanderGI.model;

import io.github.XanderGI.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatchScoreTest {
    private final static Integer playerOneId = 1;
    private final static Integer playerTwoId = 2;
    private Player playerOne;
    private Player playerTwo;
    private PlayerScore playerScoreOne;
    private PlayerScore playerScoreTwo;
    private MatchScore matchScore;

    @BeforeEach
    void setUp() {
        playerOne = new Player(playerOneId, "Alex");
        playerTwo = new Player(playerTwoId, "Xander");

        playerScoreOne = PlayerScore.matchStart();
        playerScoreTwo = PlayerScore.matchStart();

        matchScore = new MatchScore(
                playerOne,
                playerTwo,
                playerScoreOne,
                playerScoreTwo
        );
    }

    @Test
    void shouldIncreaseGameCountWhenPlayerWinsPointAtFortyZero() {
        winPoints(playerOneId, 3);

        matchScore.pointWonBy(playerOneId);

        assertScore(playerScoreOne, Point.ZERO, 1, 0);
        assertScore(playerScoreTwo, Point.ZERO, 0, 0);
    }

    @Test
    void shouldGetAdvantageWhenPlayerWinsPointAtFortyForty() {
        winPoints(playerOneId, 3);
        winPoints(playerTwoId, 3);

        matchScore.pointWonBy(playerOneId);

        assertScore(playerScoreOne, Point.ADVANTAGE, 0, 0);
        assertScore(playerScoreTwo, Point.FORTY, 0, 0);
    }

    @Test
    void shouldReturnToDeuceWhenPlayerWinsPointAgainstAdvantage() {
        winPoints(playerOneId, 3);
        winPoints(playerTwoId, 4);

        matchScore.pointWonBy(playerOneId);

        assertScore(playerScoreOne, Point.FORTY, 0, 0);
        assertScore(playerScoreTwo, Point.FORTY, 0, 0);
    }

    @Test
    void shouldIncreaseSetCountWhenPlayerWinsSixGames() {
        winGames(playerOneId, 5);
        winPoints(playerOneId, 3);

        matchScore.pointWonBy(playerOneId);

        assertScore(playerScoreOne, Point.ZERO, 0, 1);
        assertScore(playerScoreTwo, Point.ZERO, 0, 0);
    }

    @Test
    void shouldStartNewGameInNewSetWhenPointWonAfterWinningSet() {
        winSets(playerOneId,1);
        winPoints(playerOneId, 4);

        matchScore.pointWonBy(playerOneId);

        assertScore(playerScoreOne, Point.FIFTEEN, 1, 1);
        assertScore(playerScoreTwo, Point.ZERO, 0, 0);
    }

    @Test
    void shouldStartTieBreakWhenPlayerWinsGameAtSixAll() {
        winGames(playerOneId, 5);
        winGames(playerTwoId, 5);
        winGames(playerOneId, 1);
        winPoints(playerTwoId, 3);

        matchScore.pointWonBy(playerTwoId);

        assertTrue(matchScore.isTieBreak());
        assertEquals(0, playerScoreOne.getTieBreakPoint());
        assertEquals(0, playerScoreTwo.getTieBreakPoint());
    }

    @Test
    void shouldWinSetWhenPlayerWinsSevenTieBreakPoints() {
        reachSixAll();

        winPoints(playerOneId, 6);

        matchScore.pointWonBy(playerOneId);

        assertFalse(matchScore.isTieBreak());
        assertEquals(0, playerScoreOne.getTieBreakPoint());
        assertEquals(0, playerScoreTwo.getTieBreakPoint());
        assertScore(playerScoreOne, Point.ZERO, 0, 1);
        assertScore(playerScoreTwo, Point.ZERO, 0, 0);
    }

    @Test
    void shouldWinSetWhenPlayerWinsPointAtSixAllInTieBreak() {
        reachSixAll();

        winPoints(playerOneId, 6);
        winPoints(playerTwoId, 6);

        matchScore.pointWonBy(playerOneId);

        assertFalse(matchScore.isTieBreak());
        assertEquals(0, playerScoreOne.getTieBreakPoint());
        assertEquals(0, playerScoreTwo.getTieBreakPoint());
        assertScore(playerScoreOne, Point.ZERO, 0, 1);
        assertScore(playerScoreTwo, Point.ZERO, 0, 0);
    }

    @Test
    void shouldWinMatchWhenPlayerWinsTwoSets() {
        winSets(playerOneId, 1);
        winGames(playerOneId, 5);
        winPoints(playerOneId, 3);

        matchScore.pointWonBy(playerOneId);

        assertTrue(matchScore.isMatchOver());
        assertScore(playerScoreOne, Point.ZERO, 0, 2);
        assertScore(playerScoreTwo, Point.ZERO, 0, 0);
    }

    private void assertScore(PlayerScore playerScore, Point point, Integer game, Integer set) {
        assertEquals(set, playerScore.getSet());
        assertEquals(game, playerScore.getGame());
        assertEquals(point, playerScore.getPoint());

    }

    private void reachSixAll() {
        winGames(playerOneId, 5);
        winGames(playerTwoId, 5);
        winGames(playerOneId, 1);
        winGames(playerTwoId, 1);
    }

    private void winSets(Integer playerId, int countSets) {
        winGames(playerId, countSets * 6);
    }

    private void winGames(Integer playerId, int countGames) {
        winPoints(playerId, countGames * 4);
    }

    private void winPoints(Integer playerId, int countPoints) {
        for (int i = 0; i < countPoints; i++) {
            matchScore.pointWonBy(playerId);
        }
    }
}