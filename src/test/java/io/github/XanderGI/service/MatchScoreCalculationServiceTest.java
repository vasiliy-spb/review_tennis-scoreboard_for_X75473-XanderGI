package io.github.XanderGI.service;

import io.github.XanderGI.model.MatchScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MatchScoreCalculationServiceTest {
    private Integer playerId;

    @BeforeEach
    void setUp() {
        playerId = 1;
    }

    @InjectMocks
    MatchScoreCalculationService calculationService;

    @Test
    void shouldMatchIsOverReturnTrue() {
        MatchScore matchMock = Mockito.mock(MatchScore.class);

        calculationService.addPoint(matchMock, playerId);

        verify(matchMock).pointWonBy(playerId);
    }
}