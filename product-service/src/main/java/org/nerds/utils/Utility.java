package org.nerds.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
final public class Utility {
    private Utility() {
    }

    public static boolean isStringEmptyOrNull(String text) {
        return text == null || text.trim().isEmpty();
    }

    public static boolean isStringNonEmpty(String text){
        return !isStringEmptyOrNull(text);
    }

    public static String extractSortOrder(String sortOrder) {
        if (isStringEmptyOrNull(sortOrder) || !Set.of("asc", "desc").contains(sortOrder)) {
            log.warn("Provided invalid sortOrder {}, the only values allowed are asc and desc", sortOrder);
            sortOrder = "asc";
        }
        return sortOrder;
    }
}
