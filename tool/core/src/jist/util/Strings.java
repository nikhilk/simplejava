// Strings.java
// jist/core
//

package jist.util;

import java.util.*;

public final class Strings {

    private static final Random random = new Random(System.currentTimeMillis());
    private static final char[] alphabet;

    static {
        StringBuilder sb = new StringBuilder();

        for (char ch = '0'; ch <= '9'; ch++) {
            sb.append(ch);
        }
        for (char ch = 'a'; ch <= 'z'; ch++) {
            sb.append(ch);
        }
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            sb.append(ch);
        }

        alphabet = sb.toString().toCharArray();
    }

    private Strings() {
    }

    public static String escape(String s) {
        return s.replace("\n", "\\n").replace("\"", "\\\"");
    }

    public static boolean hasValue(String s) {
        return (s != null) && (s.length() != 0);
    }

    public static boolean isNullOrEmpty(String s) {
        return (s == null) || (s.length() == 0);
    }

    public static String randomString(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
           sb.append(alphabet[random.nextInt(alphabet.length)]);
        }
        return sb.toString();
    }
}
