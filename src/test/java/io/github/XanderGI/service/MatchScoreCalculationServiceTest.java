package io.github.XanderGI.service;

import io.github.XanderGI.entity.Player;
import io.github.XanderGI.exception.MatchNotFoundException;
import io.github.XanderGI.model.MatchScore;
import io.github.XanderGI.model.PlayerScore;
import io.github.XanderGI.repository.OngoingMatchRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchScoreCalculationServiceTest {
    private UUID matchId;
    private Integer playerId;

    @BeforeEach
    void setUp() {
        matchId = UUID.randomUUID();
        playerId = 1;
    }

    @Mock
    OngoingMatchRepository ongoingMatchRepository;

    @Mock
    FinishedMatchesPersistenceService finishedMatchesService;

    @InjectMocks
    MatchScoreCalculationService calculationService;

    @Test
    void shouldThrowMatchNotFoundException() {
        when(ongoingMatchRepository.get(matchId)).thenReturn(Optional.empty());

        Assertions.assertThrows(MatchNotFoundException.class, () -> calculationService.addPoint(matchId, playerId));
    }

    @Test
    void shouldMatchIsOverReturnFalse() {
        Player playerOne = new Player(playerId, "player1");
        Player playerTwo = new Player(2, "player2");
        MatchScore matchScore = new MatchScore(
                playerOne,
                playerTwo,
                PlayerScore.matchStart(),
                PlayerScore.matchStart()
        );
        when(ongoingMatchRepository.get(matchId)).thenReturn(Optional.of(matchScore));

        calculationService.addPoint(matchId, playerId);

        verify(finishedMatchesService, Mockito.never()).save(matchScore);
        verify(ongoingMatchRepository, Mockito.never()).remove(matchId);

    }

    @Test
    void shouldMatchIsOverReturnTrue() {
        MatchScore matchMock = Mockito.mock(MatchScore.class);

        when(ongoingMatchRepository.get(matchId)).thenReturn(Optional.of(matchMock));
        when(matchMock.isMatchOver()).thenReturn(true);

        calculationService.addPoint(matchId, playerId);

        verify(matchMock).pointWonBy(playerId);
        verify(finishedMatchesService).save(matchMock);
        verify(ongoingMatchRepository).remove(matchId);
    }
}