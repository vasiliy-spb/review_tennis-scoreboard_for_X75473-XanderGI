package io.github.XanderGI.mapper;

import io.github.XanderGI.dto.MatchDto;
import io.github.XanderGI.dto.MatchScoreDto;
import io.github.XanderGI.dto.PlayerItemDto;
import io.github.XanderGI.entity.Match;
import io.github.XanderGI.entity.Player;
import io.github.XanderGI.model.MatchScore;
import io.github.XanderGI.model.PlayerScore;

public class MatchMapper {
    public static MatchDto toMatchDto(Match match) {
        return new MatchDto(
                match.getPlayerOne().getName(),
                match.getPlayerTwo().getName(),
                match.getWinner().getName()
        );
    }

    public static MatchScoreDto toMatchScoreDto(MatchScore matchScore) {
        String pointsByPlayerOne = getDisplayPoints(matchScore, matchScore.getPlayerScoreOne());
        String pointsByPlayerTwo = getDisplayPoints(matchScore, matchScore.getPlayerScoreTwo());

        PlayerItemDto playerOneDto = toPlayerItemDto(matchScore.getPlayerOne(), matchScore.getPlayerScoreOne(), pointsByPlayerOne);
        PlayerItemDto playerTwoDto = toPlayerItemDto(matchScore.getPlayerTwo(), matchScore.getPlayerScoreTwo(), pointsByPlayerTwo);

        String winnerName = matchScore.getWinner()
                .map(Player::getName)
                .orElse(null);

        return new MatchScoreDto(
                playerOneDto,
                playerTwoDto,
                winnerName,
                matchScore.isMatchOver()
        );
    }

    private static PlayerItemDto toPlayerItemDto(Player player, PlayerScore playerScore, String displayPoints) {
        return new PlayerItemDto(
                player.getId(),
                player.getName(),
                displayPoints,
                playerScore.getGame(),
                playerScore.getSet()
        );
    }

    private static String getDisplayPoints(MatchScore matchScore, PlayerScore playerScore) {
        if (matchScore.isTieBreak()) {
            return String.valueOf(playerScore.getTieBreakPoint());
        } else {
            return String.valueOf(playerScore.getPoint().getValue());
        }
    }
}