package io.github.XanderGI.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MatchScoreDto {
    private final PlayerItemDto playerOne;
    private final PlayerItemDto playerTwo;
    private final String winnerName;
    private final boolean isMatchOver;
}