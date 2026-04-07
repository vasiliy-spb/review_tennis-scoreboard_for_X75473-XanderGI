package io.github.XanderGI.util;

import io.github.XanderGI.exception.InvalidMatchException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtil {
    private static final Integer MAX_NAME_LENGTH = 50;
    private static final String NAME_PATTERN = "^[a-zA-Z0-9\\s-']+$";

    public static int parsePageNumber(String page) {
        if (page == null || page.isBlank()) {
            return 1;
        }

        try {
            int pageNumber = Integer.parseInt(page);
            return Math.max(pageNumber, 1);
        } catch (NumberFormatException e) {
            throw new InvalidMatchException("Invalid page number format");
        }
    }

    public static void checkNamesIsValid(String firstName, String secondName) {
        if (firstName == null || secondName == null || firstName.isBlank() || secondName.isBlank()) {
            throw new InvalidMatchException("The names of the players cannot be empty");
        }

        if (firstName.length() > MAX_NAME_LENGTH || secondName.length() > MAX_NAME_LENGTH) {
            throw new InvalidMatchException("The player's name cannot exceed 50 characters");
        }

        if (!firstName.matches(NAME_PATTERN) || !secondName.matches(NAME_PATTERN)) {
            throw new InvalidMatchException("The name of the players must contain only the latin alphabet");
        }
    }

    public static UUID parseUUID(String matchId) {
        try {
            return UUID.fromString(matchId);
        } catch (IllegalArgumentException e) {
            throw new InvalidMatchException("Incorrect matchId from path");
        }
    }

    public static Integer parsePlayerId(String playerId) {
        try {
            return Integer.valueOf(playerId);
        } catch (NumberFormatException e) {
            throw new InvalidMatchException("Invalid playerId format");
        }
    }
}