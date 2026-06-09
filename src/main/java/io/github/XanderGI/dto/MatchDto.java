package io.github.XanderGI.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MatchDto {

    // Можно сделать record

    private final String playerOneName;
    private final String playerTwoName;
    private final String winnerName;
}