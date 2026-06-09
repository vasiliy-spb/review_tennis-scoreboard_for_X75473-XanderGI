package io.github.XanderGI.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchUtil {

    // Класс спроектирован как утилитный, но при этом не объявлен как final.

    public static List<String> tokenize(String filterName) {
        if (filterName == null || filterName.isBlank()) {
            return Collections.emptyList();
        }

        String clearFilterName = filterName.replace("-", " ").strip().replaceAll("\\s+"," ").toLowerCase();
        String[] tokens = clearFilterName.split(" ");

        return Arrays.asList(tokens);
    }
}