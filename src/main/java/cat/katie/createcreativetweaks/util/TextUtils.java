package cat.katie.createcreativetweaks.util;

import java.util.Locale;

public class TextUtils {
    private TextUtils() {
        throw new UnsupportedOperationException("Cannot instantiate TextUtils");
    }

    public static String snakeCaseToPrettyName(String snakeCase) {
        // Modified from Apache Commons Lang3 (licensed under Apache2.0)
        final char[] buffer = snakeCase.toLowerCase(Locale.ROOT).toCharArray();
        boolean capitalizeNext = true;

        for (int i = 0; i < buffer.length; i++) {
            final char ch = buffer[i];
            if (ch == '_') {
                buffer[i] = ' ';
                capitalizeNext = true;
            } else if (capitalizeNext) {
                buffer[i] = Character.toTitleCase(ch);
                capitalizeNext = false;
            }
        }

        return new String(buffer);
    }
}
