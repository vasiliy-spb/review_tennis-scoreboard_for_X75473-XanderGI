package io.github.XanderGI.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MatchesPageDto {
    private final List<MatchDto> matches;
    private final int totalPages;
}