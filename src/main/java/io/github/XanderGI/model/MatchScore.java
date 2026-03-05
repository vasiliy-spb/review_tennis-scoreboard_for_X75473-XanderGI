package io.github.XanderGI.model;

import io.github.XanderGI.entity.Player;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MatchScore {
    private final Player playerOne;
    private final Player playerTwo;
    private PlayerScore playerScoreOne;
    private PlayerScore playerScoreTwo;
}