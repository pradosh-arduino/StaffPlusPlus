package org.apache.commons.lang;

/**
 * Small compatibility subset for the Apache Commons Lang 2.x Validate API used by Staff++ tests.
 */
public final class Validate {

    private Validate() {
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
