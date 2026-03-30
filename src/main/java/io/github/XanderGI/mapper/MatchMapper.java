package io.github.XanderGI.mapper;

import io.github.XanderGI.dto.MatchDto;
import io.github.XanderGI.entity.Match;

public class MatchMapper {
    public static MatchDto toMatchDto(Match match) {
        return new MatchDto(
                match.getPlayerOne().getName(),
                match.getPlayerTwo().getName(),
                match.getWinner().getName()
        );
    }
}