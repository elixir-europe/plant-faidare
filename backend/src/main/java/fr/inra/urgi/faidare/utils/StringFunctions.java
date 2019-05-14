package fr.inra.urgi.faidare.utils;

import java.nio.charset.StandardCharsets;

/**
 * @author gcornut
 * <p>
 * Copyright INRA-URGI 2019
 */
public final class StringFunctions {

    /**
     * Fixes identifier encoding (in case we have accents in it)
     */
    public static String asUTF8(String string) {
        if (string == null) {
            return null;
        }
        return new String(string.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }
}

