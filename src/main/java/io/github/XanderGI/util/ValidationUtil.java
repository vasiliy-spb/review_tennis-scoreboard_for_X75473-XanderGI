package io.github.XanderGI.util;

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
}