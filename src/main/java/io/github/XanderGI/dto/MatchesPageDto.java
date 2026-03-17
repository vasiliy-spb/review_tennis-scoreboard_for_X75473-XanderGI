package io.github.XanderGI.dto;

import io.github.XanderGI.entity.Match;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MatchesPageDto {
    private final List<Match> matches;
    private final int totalPages;
}