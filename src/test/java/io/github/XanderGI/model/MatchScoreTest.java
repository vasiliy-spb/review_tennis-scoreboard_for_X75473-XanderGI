package io.github.XanderGI.model;

import io.github.XanderGI.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatchScoreTest {
    private final static Integer PLAYER_ONE_ID = 1;
    private final static Integer PLAYER_TWO_ID = 2;
    private Player playerOne;
    private Player playerTwo;
    private PlayerScore playerScoreOne;
    private PlayerScore playerScoreTwo;
    private MatchScore matchScore;

    @BeforeEach
    void setUp() {
        playerOne = new Player(PLAYER_ONE_ID, "Alex");
        playerTwo = new Player(PLAYER_TWO_ID, "Xander");

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
        winPoints(PLAYER_ONE_ID, 3);

        matchScore.pointWonBy(PLAYER_ONE_ID);

        assertScore(playerScoreOne, Point.ZERO, 1, 0);
        assertScore(playerScoreTwo, Point.ZERO, 0, 0);
    }

    @Test
    void shouldGetAdvantageWhenPlayerWinsPointAtFortyForty() {
        winPoints(PLAYER_ONE_ID, 3);
        winPoints(PLAYER_TWO_ID, 3);

        matchScore.pointWonBy(PLAYER_ONE_ID);

        assertScore(playerScoreOne, Point.ADVANTAGE, 0, 0);
        assertScore(playerScoreTwo, Point.FORTY, 0, 0);
    }

    @Test
    void shouldReturnToDeuceWhenPlayerWinsPointAgainstAdvantage() {
        winPoints(PLAYER_ONE_ID, 3);
        winPoints(PLAYER_TWO_ID, 4);

        matchScore.pointWonBy(PLAYER_ONE_ID);

        assertScore(playerScoreOne, Point.FORTY, 0, 0);
        assertScore(playerScoreTwo, Point.FORTY, 0, 0);
    }

    @Test
    void shouldIncreaseSetCountWhenPlayerWinsSixGames() {
        winGames(PLAYER_ONE_ID, 5);
        winPoints(PLAYER_ONE_ID, 3);

        matchScore.pointWonBy(PLAYER_ONE_ID);

        assertScore(playerScoreOne, Point.ZERO, 0, 1);
        assertScore(playerScoreTwo, Point.ZERO, 0, 0);
    }

    @Test
    void shouldStartNewGameInNewSetWhenPointWonAfterWinningSet() {
        winSets(PLAYER_ONE_ID, 1);
        winPoints(PLAYER_ONE_ID, 4);

        matchScore.pointWonBy(PLAYER_ONE_ID);

        assertScore(playerScoreOne, Point.FIFTEEN, 1, 1);
        assertScore(playerScoreTwo, Point.ZERO, 0, 0);
    }

    @Test
    void shouldStartTieBreakWhenPlayerWinsGameAtSixAll() {
        winGames(PLAYER_ONE_ID, 5);
        winGames(PLAYER_TWO_ID, 5);
        winGames(PLAYER_ONE_ID, 1);
        winPoints(PLAYER_TWO_ID, 3);

        matchScore.pointWonBy(PLAYER_TWO_ID);

        assertTrue(matchScore.isTieBreak());
        assertEquals(0, playerScoreOne.getTieBreakPoint());
        assertEquals(0, playerScoreTwo.getTieBreakPoint());
    }

    @Test
    void shouldWinSetWhenPlayerWinsSevenTieBreakPoints() {
        reachSixAll();

        winPoints(PLAYER_ONE_ID, 6);

        matchScore.pointWonBy(PLAYER_ONE_ID);

        assertFalse(matchScore.isTieBreak());
        assertEquals(0, playerScoreOne.getTieBreakPoint());
        assertEquals(0, playerScoreTwo.getTieBreakPoint());
        assertScore(playerScoreOne, Point.ZERO, 0, 1);
        assertScore(playerScoreTwo, Point.ZERO, 0, 0);
    }

    @Test
    void shouldWinSetWhenPlayerWinsPointAtSixAllInTieBreak() {
        reachSixAll();

        winPoints(PLAYER_ONE_ID, 6);
        winPoints(PLAYER_TWO_ID, 6);

        matchScore.pointWonBy(PLAYER_ONE_ID);

        assertFalse(matchScore.isTieBreak());
        assertEquals(0, playerScoreOne.getTieBreakPoint());
        assertEquals(0, playerScoreTwo.getTieBreakPoint());
        assertScore(playerScoreOne, Point.ZERO, 0, 1);
        assertScore(playerScoreTwo, Point.ZERO, 0, 0);
    }

    @Test
    void shouldWinMatchWhenPlayerWinsTwoSets() {
        winSets(PLAYER_ONE_ID, 1);
        winGames(PLAYER_ONE_ID, 5);
        winPoints(PLAYER_ONE_ID, 3);

        matchScore.pointWonBy(PLAYER_ONE_ID);

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
        winGames(PLAYER_ONE_ID, 5);
        winGames(PLAYER_TWO_ID, 5);
        winGames(PLAYER_ONE_ID, 1);
        winGames(PLAYER_TWO_ID, 1);
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