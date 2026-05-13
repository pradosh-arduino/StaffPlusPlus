package org.apache.commons.lang;

/**
 * Small compatibility subset for the Apache Commons Lang 2.x StringUtils API used by Staff++.
 */
public final class StringUtils {

    private StringUtils() {
    }

    public static boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    public static boolean isBlank(String value) {
        if (value == null) {
            return true;
        }
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    public static int getLevenshteinDistance(String left, String right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        int leftLength = left.length();
        int rightLength = right.length();

        if (leftLength == 0) {
            return rightLength;
        }
        if (rightLength == 0) {
            return leftLength;
        }

        if (leftLength > rightLength) {
            String tmp = left;
            left = right;
            right = tmp;
            leftLength = left.length();
            rightLength = right.length();
        }

        int[] previous = new int[leftLength + 1];
        int[] current = new int[leftLength + 1];

        for (int i = 0; i <= leftLength; i++) {
            previous[i] = i;
        }

        for (int j = 1; j <= rightLength; j++) {
            char rightChar = right.charAt(j - 1);
            current[0] = j;

            for (int i = 1; i <= leftLength; i++) {
                int cost = left.charAt(i - 1) == rightChar ? 0 : 1;
                current[i] = Math.min(Math.min(current[i - 1] + 1, previous[i] + 1), previous[i - 1] + cost);
            }

            int[] tmp = previous;
            previous = current;
            current = tmp;
        }

        return previous[leftLength];
    }
}
