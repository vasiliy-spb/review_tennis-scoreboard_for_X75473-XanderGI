package io.github.XanderGI.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MatchesPageDto {

    // Можно сделать record

    // Можно добавить поля с номером страницы и фильтром по имени

    private final List<MatchDto> matches;
    private final int totalPages;
}