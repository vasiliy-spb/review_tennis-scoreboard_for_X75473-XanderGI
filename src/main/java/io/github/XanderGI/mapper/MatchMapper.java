package io.github.XanderGI.mapper;

import io.github.XanderGI.dto.MatchDto;
import io.github.XanderGI.dto.MatchScoreDto;
import io.github.XanderGI.dto.PlayerItemDto;
import io.github.XanderGI.entity.Match;
import io.github.XanderGI.entity.Player;
import io.github.XanderGI.model.MatchScore;
import io.github.XanderGI.model.PlayerScore;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MatchMapper {
    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    @Mapping(source = "playerOne.name", target = "playerOneName")
    @Mapping(source = "playerTwo.name", target = "playerTwoName")
    @Mapping(source = "winner.name", target = "winnerName")
    MatchDto toMatchDto(Match match);

    @Mapping(source = "displayPoints", target = "displayPoints")
    @Mapping(source = "playerScore.game", target = "games")
    @Mapping(source = "playerScore.set", target = "sets")
    PlayerItemDto buildPlayerDto(Player player, PlayerScore playerScore, String displayPoints);

    default MatchScoreDto toMatchScoreDto(MatchScore matchScore) {
        String pointsByPlayerOne = getDisplayPoints(matchScore, matchScore.getPlayerScoreOne());
        String pointsByPlayerTwo = getDisplayPoints(matchScore, matchScore.getPlayerScoreTwo());

        PlayerItemDto playerOneDto = buildPlayerDto(matchScore.getPlayerOne(), matchScore.getPlayerScoreOne(), pointsByPlayerOne);
        PlayerItemDto playerTwoDto = buildPlayerDto(matchScore.getPlayerTwo(), matchScore.getPlayerScoreTwo(), pointsByPlayerTwo);

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

    private String getDisplayPoints(MatchScore matchScore, PlayerScore playerScore) {
        if (matchScore.isTieBreak()) {
            return String.valueOf(playerScore.getTieBreakPoint());
        } else {
            return String.valueOf(playerScore.getPoint().getValue());
        }
    }
}