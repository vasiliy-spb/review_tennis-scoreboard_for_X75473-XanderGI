package io.github.XanderGI.util;

import io.github.XanderGI.exception.InvalidMatchException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtil {

    public static int parsePageNumber(String page) throws NumberFormatException {
        if (page == null || page.isBlank()) {
            return 1;
        }

        int pageNumber = Integer.parseInt(page);

        return Math.max(pageNumber, 1);
    }

    public static void checkNamesIsValid(String firstName, String secondName) {
        if (firstName == null || secondName == null || firstName.isBlank() || secondName.isBlank()) {
            throw new InvalidMatchException("The names of the players cannot be empty");
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