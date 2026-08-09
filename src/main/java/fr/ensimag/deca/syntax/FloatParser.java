package fr.ensimag.deca.syntax;

import java.math.BigDecimal;

/**
 * Utility class for parsing float literals with checks for underflow.
 */
public final class FloatParser {

    private FloatParser() {}

    /**
     * Parses a string as a float and checks for underflow.
     *
     * @param str the string to parse
     * @return the parsed float value
     * @throws NumberFormatException if the string cannot be parsed or if it represents an underflow
     */
    public static float checkedParseFloat(String str) throws NumberFormatException {
        float x = Float.parseFloat(str);
        if (Float.compare(x, 0.0f) != 0) {
            return x;
        }

        // As described in https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Float.html#valueOf(java.lang.String)
        // > if the exact value of s is small enough in magnitude (less than or equal to MIN_VALUE/2),
        // > rounding to float will result in a zero.
        // So we need to detect a non-zero number that was rounded to 0.
        // Unfortunately, there is no universal way to detect this in Java.
        if (!str.startsWith("0x") && !str.startsWith("0X")) {
            if (isNonNullDecimal(str)) {
                throw new NumberFormatException("Float literal underflow");
            }
        } else {
            if (isNonNullHex(str)) {
                throw new NumberFormatException("Float literal underflow");
            }
        }
        return x;
    }

    private static boolean isNonNullDecimal(String str) {
        if (str.endsWith("f") || str.endsWith("F")) {
            str = str.substring(0, str.length() - 1);
        }
        return new BigDecimal(str).compareTo(BigDecimal.ZERO) != 0;
    }

    private static boolean isNonNullHex(String str) {
        String hex = str.substring(2);
        int p = hex.indexOf("p");
        if (p < 0) {
            p = hex.indexOf("P");
        }
        String mantissa = hex.substring(0, p);
        for (char c : mantissa.toCharArray()) {
            if (c != '0' && c != '.') {
                return true; // Non-zero hex digit found
            }
        }
        return false;
    }
}
