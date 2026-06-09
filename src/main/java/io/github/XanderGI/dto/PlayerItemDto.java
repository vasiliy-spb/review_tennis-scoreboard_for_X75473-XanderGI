package io.github.XanderGI.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PlayerItemDto {

    // Можно сделать record

    private final Integer id;
    private final String name;
    private final String displayPoints;
    private final Integer games;
    private final Integer sets;
}