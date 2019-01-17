package fr.inra.urgi.gpds.utils;

import java.io.UnsupportedEncodingException;

/**
 * @author gcornut
 *
 *         Copyright INRA-URGI 2019
 *
 */
public final class StringFunctions {

	/**
	 * Fixes identifier encoding (in case we have accents in it)
	 */
	public static String asUTF8(String string) throws UnsupportedEncodingException {
		if (string == null) {
			return null;
		}
		return new String(string.getBytes("ISO-8859-1"), "UTF-8");
	}
}
