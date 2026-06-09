package io.github.XanderGI.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MatchScoreDto {

    // Можно сделать record

    // isMatchOver можно удалить: если в матче есть победитель (winnerName != null), значит он завершён

    private final PlayerItemDto playerOne;
    private final PlayerItemDto playerTwo;
    private final String winnerName;
    private final boolean isMatchOver;
}