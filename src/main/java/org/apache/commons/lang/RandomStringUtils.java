package org.apache.commons.lang;

import java.security.SecureRandom;

/**
 * Small compatibility subset for the Apache Commons Lang 2.x RandomStringUtils API used by Staff++.
 */
public final class RandomStringUtils {

    private static final char[] ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    private static final SecureRandom RANDOM = new SecureRandom();

    private RandomStringUtils() {
    }

    public static String randomAlphanumeric(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Requested random string length must not be negative");
        }

        StringBuilder builder = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            builder.append(ALPHANUMERIC[RANDOM.nextInt(ALPHANUMERIC.length)]);
        }
        return builder.toString();
    }
}
