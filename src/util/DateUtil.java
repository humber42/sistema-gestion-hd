package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Help function to convert dates
 */
public class DateUtil {
    /**
     * The date pattern
     */
    private static final String DATE_PATTERN = "dd/MM/yyyy";

    /**
     * Formatter
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    /**
     * Returns the given date as well formatted String. The above defined
     * {@link DateUtil#DATE_PATTERN} is used.
     *
     * @param date the date to be returned as a string
     * @return formatted string
     */
    public static String format(LocalDate date) {
        if (date == null)
            return null;
        return DATE_TIME_FORMATTER.format(date);
    }


}
