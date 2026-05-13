package org.apache.commons.lang;

import java.util.Locale;

/**
 * Small compatibility subset for the Apache Commons Lang 2.x WordUtils API used by Staff++.
 */
public final class WordUtils {

    private WordUtils() {
    }

    public static String capitalizeFully(String value) {
        if (value == null || value.length() == 0) {
            return value;
        }

        String lowerCaseValue = value.toLowerCase(Locale.ROOT);
        StringBuilder builder = new StringBuilder(lowerCaseValue.length());
        boolean capitalizeNext = true;

        for (int i = 0; i < lowerCaseValue.length(); i++) {
            char current = lowerCaseValue.charAt(i);
            if (Character.isWhitespace(current)) {
                capitalizeNext = true;
                builder.append(current);
            } else if (capitalizeNext) {
                builder.append(Character.toTitleCase(current));
                capitalizeNext = false;
            } else {
                builder.append(current);
            }
        }

        return builder.toString();
    }
}
