package io.github.XanderGI.util;

import io.github.XanderGI.exception.InvalidMatchException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
}